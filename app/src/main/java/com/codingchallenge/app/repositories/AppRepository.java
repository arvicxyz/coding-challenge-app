package com.codingchallenge.app.repositories;

import android.content.Context;

public class AppRepository {

    private static AppRepository instance;

    public static AppRepository getInstance(Context context) {
        if (instance == null) {
            instance = new AppRepository(context);
        }
        return instance;
    }

    public AppRepository(Context context) {
        trackRepository = new TrackRepository(context);
    }

    // Repositories

    private TrackRepository trackRepository;

    public TrackRepository getTrackRepository() {
        return trackRepository;
    }
}
