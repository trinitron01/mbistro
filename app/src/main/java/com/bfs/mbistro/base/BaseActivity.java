package com.bfs.mbistro.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.bfs.mbistro.App;
import com.bfs.mbistro.crashreporting.CrashReportingEngine;
import com.bfs.mbistro.network.ApiService;
import javax.inject.Inject;

public abstract class BaseActivity extends AppCompatActivity {

    @Inject
    protected ApiService service;

  @Inject protected CrashReportingEngine crashReportingEngine;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).component.inject(this);
        crashReportingEngine.registerCrashReporting();
    }
}
