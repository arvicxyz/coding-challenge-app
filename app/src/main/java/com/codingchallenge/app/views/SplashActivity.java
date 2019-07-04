package com.codingchallenge.app.views;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.codingchallenge.app.R;
import com.codingchallenge.app.models.TrackModel;
import com.codingchallenge.app.repositories.AppRepository;
import com.codingchallenge.app.repositories.CachedDataRepository;
import com.codingchallenge.app.services.TrackService;
import com.codingchallenge.app.utils.ErrorLogger;
import com.codingchallenge.app.utils.ModelConverter;
import com.codingchallenge.app.utils.NetworkUtil;

import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (NetworkUtil.isNetworkAvailable(this)) {
            // ONLINE

            // Delete data from DB to refresh data during offline use
            AppRepository.getInstance(this).getTrackRepository().deleteAll();

            // Get tracks from API
            TrackService service = new TrackService();
            service.getTracks().observe(this, trackList -> {
                if (trackList != null && trackList.size() > 0) {
                    AppRepository.getInstance(this)
                            .getTrackRepository()
                            .insertAll(ModelConverter.convertModelToEntity(trackList));
                    CachedDataRepository.setCachedTracks(trackList);
                }
                navigateToMainActivity();
            });
        } else {
            // OFFLINE

            // Get tracks from local DB
            AppRepository.getInstance(this).getTrackRepository().getAllTracks().observe(this, trackList -> {
                if (trackList != null && trackList.size() > 0) {
                    List<TrackModel> tracks = ModelConverter.convertEntityToModel(trackList);
                    CachedDataRepository.setCachedTracks(tracks);
                    navigateToMainActivity();
                } else {
                    ErrorLogger.showNoInternetConnectionDialog(this);
                }
            });
        }
    }

    private void navigateToMainActivity() {
        // Display next view after splash loading after 'x' seconds, x = SPLASH_DELAY
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(this, MainActivity.class);
            ActivityOptions options = ActivityOptions.makeCustomAnimation(
                    this, android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(intent, options.toBundle());
        }, SPLASH_DELAY);
    }
}
