package com.codingchallenge.app.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import java.net.InetAddress;

import timber.log.Timber;

public class NetworkUtil {

    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddress = InetAddress.getByName("google.com");
            return !ipAddress.equals(InetAddress.getByName(""));
        } catch (Exception e) {
            Timber.e(e);
        }
        return false;
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null)
            return false;
        return connectivityManager.getActiveNetworkInfo() != null;
    }
}
