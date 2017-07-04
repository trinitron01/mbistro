package com.bfs.mbistro.di;

import com.bfs.mbistro.crashreporting.CrashReportingEngine;
import com.bfs.mbistro.crashreporting.FabricReportingEngine;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final Context appContext;

    public AppModule(Context appContext) {
        this.appContext = appContext;
    }

    @Provides
    public Context provideAppContext() {
        return appContext;
    }

    @Provides
    @Singleton
    public CrashReportingEngine provideCrashReportingEngine(Context context) {
        return new FabricReportingEngine(context);
    }

}
