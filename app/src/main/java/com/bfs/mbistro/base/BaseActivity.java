package com.bfs.mbistro.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.bfs.mbistro.BistroApp;
import com.bfs.mbistro.crashreporting.CrashReportingEngine;
import com.bfs.mbistro.di.BistroComponent;
import com.bfs.mbistro.network.ApiService;
import javax.inject.Inject;

public abstract class BaseActivity extends AppCompatActivity {

  @Inject protected ApiService service;

  @Inject protected CrashReportingEngine crashReportingEngine;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    inject(((BistroApp) getApplication()).component);
    crashReportingEngine.registerCrashReporting();
  }

  protected abstract void inject(BistroComponent component);

  protected <T extends View> T findView(@IdRes int id) {
    //noinspection unchecked
    return (T) findViewById(id);
  }
}
