package com.codingchallenge.app.utils;

import com.codingchallenge.app.repositories.AppRepository;

public class AppSettings {

    public static final int CLIENT_TIMEOUT = 60;

    private static AppRepository appRepository;

    public static AppRepository getAppRepository() {
        return appRepository;
    }

    public static void setAppRepository(AppRepository appRepository) {
        AppSettings.appRepository = appRepository;
    }
}
