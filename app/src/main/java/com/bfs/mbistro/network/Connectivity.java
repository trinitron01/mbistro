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

  public static boolean isNetworkConnected(Context context) {
    ConnectivityManager cm =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
  }
}