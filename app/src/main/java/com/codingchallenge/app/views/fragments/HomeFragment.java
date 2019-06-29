package com.codingchallenge.app.views.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codingchallenge.app.R;
import com.codingchallenge.app.models.TrackModel;
import com.codingchallenge.app.services.TrackService;
import com.codingchallenge.app.viewmodels.HomeFragmentViewModel;
import com.codingchallenge.app.views.MainActivity;
import com.codingchallenge.app.views.adapters.TrackAdapter;
import com.codingchallenge.app.views.base.BaseFragment;
import com.codingchallenge.app.views.observers.HomeFragmentObserver;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.text.MessageFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

public class HomeFragment extends BaseFragment<HomeFragmentObserver, HomeFragmentViewModel> {

    private static final int DISPLAY_DELAY = 100;

    private MainActivity _activity;
    private Unbinder _unbinder;
    private Handler _displayHandler;

    private TrackAdapter _trackAdapter;
    private ArrayList<TrackModel> _tracksList;

    @BindView(R.id.slidingLayout)
    com.sothree.slidinguppanel.SlidingUpPanelLayout _slidingLayout;

    @BindView(R.id.recyclerViewTracks)
    RecyclerView _recyclerViewTracks;

    @BindView(R.id.dragView)
    LinearLayout _dragView;

    // Details page views
    private ScrollView _scrollView;
    private ImageView _trackArtwork;
    private TextView _trackName;
    private TextView _trackGenre;
    private TextView _trackPrice;
    private TextView _trackDescription;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        _activity = ((MainActivity) getActivity());
        _unbinder = ButterKnife.bind(this, rootView);
        _displayHandler = new Handler();

        // Init adapter
        _trackAdapter = new TrackAdapter(_activity);
        _trackAdapter.setItemClickListener(this::onItemClickListener);

        // Init recycler view
        _recyclerViewTracks.setAdapter(_trackAdapter);
        _recyclerViewTracks.setHasFixedSize(true);
        _recyclerViewTracks.setItemViewCacheSize(20);
        _recyclerViewTracks.setLayoutManager(new LinearLayoutManager(_activity));

        // Get tracks from API
        TrackService service = new TrackService();
        service.getTracks().observe(this, trackList -> {
            if (trackList != null && trackList.size() > 0) {
                _tracksList = new ArrayList<>(trackList);
            } else {
                _tracksList = new ArrayList<>();
            }
            _trackAdapter.setItemList(trackList);
        });

        // Init all details page views
        _scrollView = _dragView.findViewById(R.id.scrollView);
        _trackArtwork = _dragView.findViewById(R.id.trackArtwork);
        _trackName = _dragView.findViewById(R.id.trackName);
        _trackGenre = _dragView.findViewById(R.id.trackGenre);
        _trackPrice = _dragView.findViewById(R.id.trackPrice);
        _trackDescription = _dragView.findViewById(R.id.trackDescription);

        // Slide layout
        _slidingLayout.setScrollableView(_scrollView);
        _slidingLayout.setFadeOnClickListener(v -> _slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED));

        return rootView;
    }

    @Override
    public void onDestroyView() {
        Timber.i("onDestroyView");
        if (_unbinder != null)
            _unbinder.unbind();
        super.onDestroyView();
    }

    private void onItemClickListener(View view, int position) {

        TrackModel track = _tracksList.get(position);

        // Artwork
        if (!TextUtils.isEmpty(track.getTrackArtworkUrl())) {
            Glide.with(_activity)
                    .load(track.getTrackArtworkUrl())
                    .encodeQuality(100)
                    .into(_trackArtwork);
        }

        // Name
        _trackName.setText(track.getTrackName());

        // Genre
        _trackGenre.setText(track.getTrackGenre());

        // Price
        _trackPrice.setText(MessageFormat.format("$ {0}", track.getTrackPrice()));

        // Description
        _trackDescription.setText(track.getTrackDescription());

        _displayHandler.postDelayed(() -> _slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED), DISPLAY_DELAY);
    }
}
