package com.codingchallenge.app.viewmodels;

import androidx.lifecycle.MutableLiveData;

import com.codingchallenge.app.models.TrackModel;
import com.codingchallenge.app.repositories.CachedDataRepository;
import com.codingchallenge.app.repositories.SharedPrefRepository;
import com.codingchallenge.app.utils.DateTimeUtil;
import com.codingchallenge.app.viewmodels.base.BaseViewModel;

import java.util.List;

import timber.log.Timber;

public class HomeFragmentViewModel extends BaseViewModel {

    private static final String FROM_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    private static final String TO_DATE_FORMAT = "hh:mm a 'on' MMM dd, yyyy";

    private MutableLiveData<List<TrackModel>> _tracksList;
    private MutableLiveData<String> _lastDateVisited;

    public HomeFragmentViewModel() {
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Timber.i("onCleared");
    }

    public MutableLiveData<List<TrackModel>> getTracksList() {
        if (_tracksList == null) {
            _tracksList = CachedDataRepository.getCachedTracks();
        }
        return _tracksList;
    }

    public MutableLiveData<String> getLastDateVisited() {
        if (_lastDateVisited == null) {
            _lastDateVisited = new MutableLiveData<>();
            updateLastDateVisited();
        }
        return _lastDateVisited;
    }

    public void updateLastDateVisited() {
        String lastDateVisited = SharedPrefRepository.getString(
                SharedPrefRepository.LAST_DATE_VISITED, "");
        String formattedDate = DateTimeUtil.formatDate(lastDateVisited,
                FROM_DATE_FORMAT, TO_DATE_FORMAT);
        _lastDateVisited.setValue(formattedDate);
    }
}
