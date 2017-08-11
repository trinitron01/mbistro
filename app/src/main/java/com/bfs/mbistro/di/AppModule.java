package com.bfs.mbistro.di;

import android.content.Context;
import com.bfs.mbistro.crashreporting.CrashReportingEngine;
import com.bfs.mbistro.crashreporting.FabricReportingEngine;
import com.bfs.mbistro.location.AndroidLocationPermissionsChecker;
import com.bfs.mbistro.location.LocationPermissionsChecker;
import com.bfs.mbistro.network.AndroidNetworkMonitor;
import com.bfs.mbistro.network.NetworkMonitor;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module public class AppModule {

  private final Context appContext;

  public AppModule(Context appContext) {
    this.appContext = appContext;
  }

  @Provides public Context provideAppContext() {
    return appContext;
  }

  @Provides @Singleton public CrashReportingEngine provideCrashReportingEngine(Context context) {
    return new FabricReportingEngine(context);
  }

  @Provides @Singleton public NetworkMonitor provideNetworkMonitor(Context context) {
    return new AndroidNetworkMonitor(context);
  }

  @Provides @Singleton public LocationPermissionsChecker provideLocationEngine(Context context) {
    return new AndroidLocationPermissionsChecker(context);
  }
}
