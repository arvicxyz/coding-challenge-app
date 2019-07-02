package com.codingchallenge.app.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

public class NetworkUtil {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null)
            return false;
        final Network[] networks = connectivityManager.getAllNetworks();
        for (Network network : networks) {
            NetworkCapabilities netCap = connectivityManager.getNetworkCapabilities(network);
            if (netCap != null) {
                int downSpeed = netCap.getLinkDownstreamBandwidthKbps();
                if (downSpeed > 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
