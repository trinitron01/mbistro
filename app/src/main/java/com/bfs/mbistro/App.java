package com.bfs.mbistro;

import android.app.Application;
import com.bfs.mbistro.di.AppModule;
import com.bfs.mbistro.di.BistroComponent;
import com.bfs.mbistro.di.BistroServiceModule;
import com.bfs.mbistro.di.DaggerBistroComponent;
import com.bfs.mbistro.network.AndroidNetworkMonitor;
import java.io.File;

public class App extends Application {

    private static final String BASE_URL = "https://developers.zomato.com/api/v2.1/";
    public BistroComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
      File externalCacheDir = getExternalCacheDir();
      File cacheDir = externalCacheDir != null ? externalCacheDir : getCacheDir();
        component = DaggerBistroComponent.builder()
            .bistroServiceModule(
                new BistroServiceModule(BASE_URL, cacheDir, new AndroidNetworkMonitor(this)))
            .appModule(new AppModule(this))
                .build();
    }

}
