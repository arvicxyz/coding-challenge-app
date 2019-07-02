package com.codingchallenge.app.repositories;

import android.content.Context;

public class AppRepository {

    private TrackRepository trackRepository;

    public TrackRepository getTrackRepository() {
        return trackRepository;
    }

    public AppRepository(Context context) {
        trackRepository = new TrackRepository(context);
    }
}
