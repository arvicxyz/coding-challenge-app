package com.codingchallenge.app.views;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.codingchallenge.app.R;
import com.codingchallenge.app.repositories.SharedPrefRepository;
import com.codingchallenge.app.views.fragments.HomeFragment;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.threeten.bp.LocalDateTime;

import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQ_CODE = 100;

    private int _lastPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init libraries
        ButterKnife.bind(this);
        AndroidThreeTen.init(this);
        Timber.plant(new Timber.DebugTree());
        SharedPrefRepository.init(this);

        _lastPosition = -1;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = {
                    "android.permission.ACCESS_NETWORK_STATE",
                    "android.permission.ACCESS_WIFI_STATE",
                    "android.permission.INTERNET"
            };
            for (String permission : permissions) {
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(permissions, PERMISSION_REQ_CODE);
                    break;
                }
            }
        }

        displayView(0);
    }

    @Override
    protected void onStop() {
        // Save date when user previously visited
        SharedPrefRepository.putString(SharedPrefRepository.LAST_DATE_VISITED, LocalDateTime.now().toString());
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQ_CODE:
                // Handle if user rejects permissions
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Timber.i("onBackPressed");
        switch (_lastPosition) {
            case 0:
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("HomeFragment");
                if (fragment != null) {
                    SlidingUpPanelLayout slidingLayout = ((HomeFragment) fragment).getSlidingLayout();
                    if (slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                        slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        return;
                    }
                }
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Timber.i("onKeyDown");
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return false;
    }

    public void displayView(int position) {

        Timber.i("displayView");
        if (position == _lastPosition)
            return;

        Fragment fragment = null;
        String fragmentTag = "";

        switch (position) {
            case 0:
                fragment = new HomeFragment();
                fragmentTag = "HomeFragment";
                _lastPosition = 0;
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.contentFragment, fragment, fragmentTag);
            transaction.commit();
        }
    }
}
