package com.bfs.mbistro;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;
import com.bfs.mbistro.crashreporting.CrashlyticsTree;
import com.bfs.mbistro.di.AppModule;
import com.bfs.mbistro.di.BistroComponent;
import com.bfs.mbistro.di.BistroServiceModule;
import com.bfs.mbistro.di.DaggerBistroComponent;
import java.io.File;
import timber.log.Timber;

public class BistroApp extends Application {

  private static final String BASE_URL = "https://developers.zomato.com/api/v2.1/";
  private BistroComponent component;

  @Override public void onCreate() {
    super.onCreate();
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
    File externalCacheDir = getExternalCacheDir();
    File cacheDir = externalCacheDir != null ? externalCacheDir : getCacheDir();
    component = DaggerBistroComponent.builder()
        .appModule(new AppModule(this))
        .bistroServiceModule(new BistroServiceModule(BASE_URL, cacheDir))
        .build();

    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    }
    Timber.plant(new CrashlyticsTree());
  }

  public BistroComponent getComponent() {
    return component;
  }
}
