package com.codingchallenge.app.repositories;

import androidx.lifecycle.MutableLiveData;

import com.codingchallenge.app.models.TrackModel;

import java.util.List;

public class CachedDataRepository {

    private static MutableLiveData<List<TrackModel>> cachedTracks;

    public static MutableLiveData<List<TrackModel>> getCachedTracks() {
        return cachedTracks;
    }

    public static void setCachedTracks(List<TrackModel> cachedTracks) {
        if (CachedDataRepository.cachedTracks == null)
            CachedDataRepository.cachedTracks = new MutableLiveData<>();
        CachedDataRepository.cachedTracks.setValue(cachedTracks);
    }
}
