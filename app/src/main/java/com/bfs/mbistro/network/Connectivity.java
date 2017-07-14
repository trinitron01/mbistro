package com.bfs.mbistro.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

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
   * @param context {@link Context} used to create the {@link
   * ConnectivityManager}.
   * @return true if connected, otherwise false.
   */
  public static boolean isConnected(final Context context) {
    return Connectivity.checkConnection(context);
  }

  /**
   * Determines if an active network connection is available.
   *
   * @param context {@link Context} used to create the {@link
   * ConnectivityManager}.
   * @return true if connected, otherwise false.
   */
  private static boolean checkConnection(final Context context) {
    return Connectivity.checkConnection(context, null);
  }

  /**
   * Determines if an active network connection is available.
   *
   * @param context {@link Context} used to create the {@link
   * ConnectivityManager}.
   * @param connectionType The connection type to check.
   * @return true if connected, otherwise false.
   */
  private static boolean checkConnection(final Context context, final Integer connectionType) {
    final NetworkInfo info;
    boolean isConnected = false;
    final ConnectivityManager manager =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    if (connectionType == null) {
      info = manager.getActiveNetworkInfo();
      isConnected = info != null && info.isAvailable() && info.isConnected();
    } else {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        Network[] allNetworks = manager.getAllNetworks();
        for (Network allNetwork : allNetworks) {
          NetworkInfo networkInfo = manager.getNetworkInfo(allNetwork);
          if (networkInfo.isAvailable() && networkInfo.isConnected()) {
            isConnected = true;
            break;
          }
        }
      } else {
        //noinspection deprecation
        info = manager.getNetworkInfo(connectionType);
        isConnected = info != null && info.isAvailable() && info.isConnected();
      }
    }
    return isConnected;
  }

  public static boolean isConnectedToInternet(final Context context) {
    return Connectivity.isConnectedToWifi(context) || Connectivity.isConnectedToMobile(context);
  }

  /**
   * Determines if an active WiFi connection is available.
   *
   * @param context {@link Context} used to create the {@link
   * ConnectivityManager}.
   * @return true if connected to WiFi, otherwise false.
   */
  public static boolean isConnectedToWifi(final Context context) {
    return Connectivity.checkConnection(context, ConnectivityManager.TYPE_WIFI);
  }

  /**
   * Determines if an active mobile network connection is available.
   *
   * @param context {@link Context} used to create the {@link
   * ConnectivityManager}.
   * @return true if connected, otherwise false.
   */
  public static boolean isConnectedToMobile(final Context context) {
    return Connectivity.checkConnection(context, ConnectivityManager.TYPE_MOBILE);
  }
}