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
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codingchallenge.app.R;
import com.codingchallenge.app.models.TrackModel;
import com.codingchallenge.app.models.constants.ArtworkDimensions;
import com.codingchallenge.app.repositories.AppRepository;
import com.codingchallenge.app.repositories.CachedDataRepository;
import com.codingchallenge.app.repositories.SharedPrefRepository;
import com.codingchallenge.app.utils.DateTimeUtil;
import com.codingchallenge.app.utils.ImageUtil;
import com.codingchallenge.app.utils.ModelConverter;
import com.codingchallenge.app.utils.Randomizer;
import com.codingchallenge.app.viewmodels.HomeFragmentViewModel;
import com.codingchallenge.app.views.MainActivity;
import com.codingchallenge.app.views.adapters.TrackAdapter;
import com.codingchallenge.app.views.base.BaseFragment;
import com.codingchallenge.app.views.observers.HomeFragmentObserver;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

public class HomeFragment extends BaseFragment<HomeFragmentObserver, HomeFragmentViewModel> {

    private static final int DISPLAY_DELAY = 200;

    private MainActivity _activity;
    private Unbinder _unbinder;
    private Handler _displayHandler;

    private TrackAdapter _trackAdapter;
    private List<TrackModel> _tracksList;

    private SlidingUpPanelLayout.PanelState _previousState;

    private TrackModel _track;
    private int _lastScreenId;

    @BindView(R.id.appbar)
    AppBarLayout _appbar;

    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout _collapsingToolbar;

    @BindView(R.id.toolbar)
    Toolbar _toolbar;

    @BindView(R.id.imageHeader)
    ImageView _imageHeader;

    @BindView(R.id.lastVisited)
    TextView _lastVisited;

    @BindView(R.id.slidingLayout)
    com.sothree.slidinguppanel.SlidingUpPanelLayout _slidingLayout;

    @BindView(R.id.recyclerViewTracks)
    RecyclerView _recyclerViewTracks;

    @BindView(R.id.dragView)
    ConstraintLayout _dragView;

    @BindString(R.string.app_name)
    String appName;

    // Details page views
    private ScrollView _scrollView;
    private ImageView _trackArtwork;
    private TextView _trackName;
    private TextView _trackGenre;
    private TextView _trackPrice;
    private TextView _trackDescription;
    private Guideline _guidelineImage;
    private Guideline _guidelineDetails;
    private ImageButton _buttonClose;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        _activity = ((MainActivity) getActivity());
        _unbinder = ButterKnife.bind(this, rootView);
        _displayHandler = new Handler();

        // Init UI
        _activity.setSupportActionBar(_toolbar);
        _collapsingToolbar.setTitle(appName);

        // Init adapter
        _trackAdapter = new TrackAdapter(_activity);
        _trackAdapter.setItemClickListener(this::onItemClickListener);

        // Init recycler view
        _recyclerViewTracks.setAdapter(_trackAdapter);
        _recyclerViewTracks.setHasFixedSize(true);
        _recyclerViewTracks.setItemViewCacheSize(20);
        _recyclerViewTracks.setLayoutManager(new LinearLayoutManager(_activity));

        // Get cached tracks
        CachedDataRepository.getCachedTracks().observe(_activity, trackList -> {
            if (trackList != null && trackList.size() > 0) {
                _tracksList = new ArrayList<>(trackList);
            } else {
                _tracksList = new ArrayList<>();
            }

            // Sort list by Track ID from lowest to highest
            Collections.sort(_tracksList, (o1, o2) -> {
                Integer x = o1.getTrackId();
                Integer y = o2.getTrackId();
                return x - y;
            });

            _trackAdapter.setItemList(_tracksList);

            // Display last screen that the user was on
            _lastScreenId = SharedPrefRepository.getInt(
                    SharedPrefRepository.LAST_SCREEN_ID, -1);
            if (_lastScreenId != -1) {
                // Run on UI thread
                new Handler().post(() -> onItemClickListener(null, 0));
            }

            // Random featured track image display on app open
            TrackModel featuredTrack = new Randomizer<TrackModel>().getRandomElement(_tracksList);
            if (!TextUtils.isEmpty(featuredTrack.getTrackArtworkUrl())) {
                String artworkUrl = ImageUtil.getHighDefArtworkUrl(
                        featuredTrack.getTrackArtworkUrl(), ArtworkDimensions.SUPER_HI_DEF_MEDIUM);
                Glide.with(_activity)
                        .load(artworkUrl)
                        .placeholder(R.drawable.img_default)
                        .encodeFormat(Bitmap.CompressFormat.WEBP)
                        .encodeQuality(100)
                        .centerCrop()
                        .into(_imageHeader);
            }
        });

        // Init all details page views
        _scrollView = _dragView.findViewById(R.id.scrollView);
        _trackArtwork = _dragView.findViewById(R.id.trackArtwork);
        _trackName = _dragView.findViewById(R.id.trackName);
        _trackGenre = _dragView.findViewById(R.id.trackGenre);
        _trackPrice = _dragView.findViewById(R.id.trackPrice);
        _trackDescription = _dragView.findViewById(R.id.trackDescription);
        _guidelineImage = _dragView.findViewById(R.id.guidelineImage);
        _guidelineDetails = _dragView.findViewById(R.id.guidelineDetails);
        _buttonClose = _dragView.findViewById(R.id.buttonClose);

        // Slide layout
        _slidingLayout.setScrollableView(_scrollView);
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
                        _previousState = SlidingUpPanelLayout.PanelState.COLLAPSED;
                } else if (newState == SlidingUpPanelLayout.PanelState.ANCHORED) {
                    if (_previousState != null) {
                        _slidingLayout.setPanelState(_previousState);
                    }
                }

                // Activate the recycler view once the sliding panel is in 'collapsed' state
                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    _trackAdapter.setClickable(true);
                    // Save last screen that the user was on, "-1" is home
                    SharedPrefRepository.putInt(SharedPrefRepository.LAST_SCREEN_ID, -1);
                }
            }
        });

        // Drag view
        _dragView.setOnClickListener(v -> {
            // Empty click listener, fixes slide layout
            // collapsing on click outside the scroll view
        });

        // Close button
        _buttonClose.setOnClickListener(v -> _slidingLayout.setPanelState(
                SlidingUpPanelLayout.PanelState.COLLAPSED));

        // Show last visited in list view header
        String lastDateVisited = SharedPrefRepository.getString(
                SharedPrefRepository.LAST_DATE_VISITED, "");
        String formattedDate = DateTimeUtil.formatDate(lastDateVisited,
                "yyyy-MM-dd'T'HH:mm:ss.SSS", "hh:mm a 'on' MMM dd, yyyy");
        _lastVisited.setText(String.format("Last visit: %s", formattedDate));

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

        _trackAdapter.setClickable(false);

        _track = _tracksList.get(position);
        if (view == null && position == 0) {
            AppRepository.getInstance(_activity).getTrackRepository().getTrack(_lastScreenId).observe(_activity, track -> {
                if (track != null) {
                    _track = ModelConverter.convert(track);
                    initTrackDetails();
                }
            });
        } else {
            initTrackDetails();
        }
    }

    private void initTrackDetails() {

        // Artwork
        if (!TextUtils.isEmpty(_track.getTrackArtworkUrl())) {
            String artworkUrl = ImageUtil.getHighDefArtworkUrl(
                    _track.getTrackArtworkUrl(), ArtworkDimensions.SUPER_HI_DEF_MEDIUM);
            Glide.with(_activity)
                    .load(artworkUrl)
                    .placeholder(R.drawable.img_default)
                    .encodeFormat(Bitmap.CompressFormat.WEBP)
                    .encodeQuality(100)
                    .centerCrop()
                    .into(_trackArtwork);
        }

        // Name
        String trackName = _track.getTrackName();
        if (!TextUtils.isEmpty(trackName)) {
            _trackName.setText(trackName);
        } else {
            _trackName.setText(_activity.getString(R.string.not_available_text));
        }

        // Genre
        String trackGenre = _track.getTrackGenre();
        if (!TextUtils.isEmpty(trackGenre)) {
            _trackGenre.setText(trackGenre);
            _trackGenre.setVisibility(View.VISIBLE);
        } else {
            _trackGenre.setVisibility(View.GONE);
        }

        // Price
        float trackPrice = _track.getTrackPrice();
        if (trackPrice > 0) {
            _trackPrice.setText(MessageFormat.format("$ {0}", trackPrice));
        } else {
            _trackPrice.setText(_activity.getString(R.string.free_text));
        }

        // Description
        String trackDescription = _track.getTrackDescription();
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
            // Save last screen that the user was on
            SharedPrefRepository.putInt(SharedPrefRepository.LAST_SCREEN_ID, _track.getTrackId());
        }, DISPLAY_DELAY);
    }

    public SlidingUpPanelLayout getSlidingLayout() {
        return _slidingLayout;
    }
}
