package com.bfs.mbistro.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Connectivity
 *
 * @author Remel Pugh
 * @version 1.0
 */
public final class Connectivity {

    /**
     * Hidden default constructor
     */
    private Connectivity() {
    }

    /**
     * Determines if an active network connection is available.
     *
     * @param context {@link android.content.Context} used to create the {@link android.net.ConnectivityManager}.
     * @return true if connected, otherwise false.
     */
    public static boolean isConnected(final Context context) {
        return Connectivity.checkConnection(context);
    }

    /**
     * Determines if an active network connection is available.
     *
     * @param context {@link android.content.Context} used to create the {@link android.net.ConnectivityManager}.
     * @return true if connected, otherwise false.
     */
    private static boolean checkConnection(final Context context) {
        return Connectivity.checkConnection(context, null);
    }

    /**
     * Determines if an active network connection is available.
     *
     * @param context        {@link android.content.Context} used to create the {@link android.net.ConnectivityManager}.
     * @param connectionType The connection type to check.
     * @return true if connected, otherwise false.
     */
    private static boolean checkConnection(final Context context, final Integer connectionType) {
        final NetworkInfo info;

        final ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectionType == null) {
            info = manager.getActiveNetworkInfo();

            return info != null && info.isAvailable() && info.isConnected();

        } else {
            info = manager.getNetworkInfo(connectionType);

            return info != null && info.isAvailable() && info.isConnected();

        }
    }

    public static boolean isConnectedToInternet(final Context context) {
        return Connectivity.isConnectedToWifi(context) || Connectivity.isConnectedToMobile(context);
    }

    /**
     * Determines if an active WiFi connection is available.
     *
     * @param context {@link android.content.Context} used to create the {@link android.net.ConnectivityManager}.
     * @return true if connected to WiFi, otherwise false.
     */
    public static boolean isConnectedToWifi(final Context context) {
        return Connectivity.checkConnection(context, ConnectivityManager.TYPE_WIFI);
    }

    /**
     * Determines if an active mobile network connection is available.
     *
     * @param context {@link android.content.Context} used to create the {@link android.net.ConnectivityManager}.
     * @return true if connected, otherwise false.
     */
    public static boolean isConnectedToMobile(final Context context) {
        return Connectivity.checkConnection(context, ConnectivityManager.TYPE_MOBILE);
    }

}