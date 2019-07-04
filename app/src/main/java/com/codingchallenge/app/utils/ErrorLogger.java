package com.codingchallenge.app.utils;

import android.app.Activity;

import timber.log.Timber;

public class ErrorLogger {

    public static void showNoInternetConnectionDialog(Activity activity) {
        if (activity != null)
            showNoInternetConnectionDialog(activity, null);
    }

    public static void showNoInternetConnectionDialog(Activity activity, Exception exception) {

        if (exception != null)
            Timber.e(exception);

        activity.runOnUiThread(() -> MessageDialog.show(activity,
                "No Internet Connection!",
                "Please check your connection and try again.",
                null, "OK", null,
                null, (dialog, which) -> activity.finish(), null,
                null, false));
    }
}
