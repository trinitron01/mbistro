package com.bfs.mbistro;

import com.bfs.mbistro.di.AppModule;
import com.bfs.mbistro.di.BistroComponent;
import com.bfs.mbistro.di.BistroServiceModule;
import com.bfs.mbistro.di.DaggerBistroComponent;
import com.bfs.mbistro.network.AndroidNetworkMonitor;

import android.app.Application;

public class App extends Application {

    private static final String BASE_URL = "https://developers.zomato.com/api/v2.1/";
    public BistroComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerBistroComponent.builder()
                .bistroServiceModule(new BistroServiceModule(BASE_URL, getExternalCacheDir(), new AndroidNetworkMonitor(this))).appModule(new AppModule(this))
                .build();
    }

}
