package com.codingchallenge.app.utils;

import com.codingchallenge.app.models.constants.ApiSettings;

public class BuildConfig {

    public static boolean isDebugMode() {
        return com.codingchallenge.app.BuildConfig.DEBUG;
    }

    public static String getApiBaseUrl() {
        if (isDebugMode())
            return ApiSettings.DEBUG_BASE_URL;
        else
            return ApiSettings.RELEASE_BASE_URL;
    }

    public static String getApiScheme() {
        if (isDebugMode())
            return ApiSettings.DEBUG_SCHEME;
        else
            return ApiSettings.RELEASE_SCHEME;
    }

    public static String getApiHost() {
        if (isDebugMode())
            return ApiSettings.DEBUG_HOST;
        else
            return ApiSettings.RELEASE_HOST;
    }

    public static String getApiPort() {
        if (isDebugMode())
            return ApiSettings.DEBUG_PORT;
        else
            return ApiSettings.RELEASE_PORT;
    }

    public static String getApiRoot() {
        if (isDebugMode())
            return ApiSettings.DEBUG_ROOT;
        else
            return ApiSettings.RELEASE_ROOT;
    }
}
