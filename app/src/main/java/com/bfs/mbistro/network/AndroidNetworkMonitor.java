package com.bfs.mbistro.network;

import android.content.Context;

public class AndroidNetworkMonitor implements NetworkMonitor {

    private final Context context;

    public AndroidNetworkMonitor(Context context) {
        this.context = context;
    }

    @Override
    public boolean isOnline() {
        return Connectivity.isNetworkConnected(context);
    }
}
