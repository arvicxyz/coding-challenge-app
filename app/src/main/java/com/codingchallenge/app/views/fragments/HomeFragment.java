package com.codingchallenge.app.views.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codingchallenge.app.R;
import com.codingchallenge.app.models.TrackModel;
import com.codingchallenge.app.models.constants.ArtworkDimensions;
import com.codingchallenge.app.services.TrackService;
import com.codingchallenge.app.utils.ImageUtil;
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

    private SlidingUpPanelLayout.PanelState _previousState;

    @BindView(R.id.slidingLayout)
    com.sothree.slidinguppanel.SlidingUpPanelLayout _slidingLayout;

    @BindView(R.id.recyclerViewTracks)
    RecyclerView _recyclerViewTracks;

    @BindView(R.id.dragView)
    ConstraintLayout _dragView;

    @BindView(R.id.guidelineImage)
    Guideline _guidelineImage;

    @BindView(R.id.guidelineDetails)
    Guideline _guidelineDetails;

    @BindView(R.id.buttonClose)
    ImageButton _buttonClose;

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
        _slidingLayout.setTouchEnabled(false);
        _slidingLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {

            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }

            @Override
            public void onPanelStateChanged(View panel,
                                            SlidingUpPanelLayout.PanelState previousState,
                                            SlidingUpPanelLayout.PanelState newState) {
                // Fixes the UI issue on sliding panel layout pausing midway the state change
                if (newState == SlidingUpPanelLayout.PanelState.DRAGGING) {
                    if (previousState == SlidingUpPanelLayout.PanelState.EXPANDED
                            || previousState == SlidingUpPanelLayout.PanelState.COLLAPSED)
                        _previousState = previousState;
                } else if (newState == SlidingUpPanelLayout.PanelState.ANCHORED) {
                    if (_previousState != null) {
                        _slidingLayout.setPanelState(_previousState);
                    }
                }
            }
        });

        // Close button
        _buttonClose.setOnClickListener(v -> _slidingLayout.setPanelState(
                SlidingUpPanelLayout.PanelState.COLLAPSED));

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
            String artworkUrl = ImageUtil.getHighDefArtworkUrl(
                    track.getTrackArtworkUrl(), ArtworkDimensions.SUPER_HI_DEF_MEDIUM);
            Glide.with(_activity)
                    .load(artworkUrl)
                    .centerCrop()
                    .encodeQuality(100)
                    .encodeFormat(Bitmap.CompressFormat.WEBP)
                    .into(_trackArtwork);
        }

        // Name
        String trackName = track.getTrackName();
        if (!TextUtils.isEmpty(trackName)) {
            _trackName.setText(trackName);
        } else {
            _trackName.setText(_activity.getString(R.string.not_available_text));
        }

        // Genre
        String trackGenre = track.getTrackGenre();
        if (!TextUtils.isEmpty(trackGenre)) {
            _trackGenre.setText(trackGenre);
            _trackGenre.setVisibility(View.VISIBLE);
        } else {
            _trackGenre.setVisibility(View.GONE);
        }

        // Price
        float trackPrice = track.getTrackPrice();
        if (trackPrice > 0) {
            _trackPrice.setText(MessageFormat.format("$ {0}", trackPrice));
        } else {
            _trackPrice.setText(_activity.getString(R.string.free_text));
        }

        // Description
        String trackDescription = track.getTrackDescription();
        if (!TextUtils.isEmpty(trackDescription)) {

            _trackDescription.setText(trackDescription);
            _trackDescription.setVisibility(View.VISIBLE);
            _guidelineImage.setGuidelinePercent(0.72f);

            ConstraintSet set = new ConstraintSet();
            set.clone(_dragView);
            set.connect(_scrollView.getId(), ConstraintSet.TOP,
                    _guidelineDetails.getId(), ConstraintSet.BOTTOM);
            set.applyTo(_dragView);
        } else {

            // Pull down views above description if it has no description
            _trackDescription.setText("");
            _trackDescription.setVisibility(View.GONE);
            _guidelineImage.setGuidelinePercent(0.9f);

            ConstraintSet set = new ConstraintSet();
            set.clone(_dragView);
            set.clear(_scrollView.getId(), ConstraintSet.TOP);
            set.applyTo(_dragView);
        }

        _displayHandler.postDelayed(() -> {
            _scrollView.fullScroll(ScrollView.FOCUS_UP);
            _slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        }, DISPLAY_DELAY);
    }
}
