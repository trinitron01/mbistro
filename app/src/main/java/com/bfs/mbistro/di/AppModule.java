package com.bfs.mbistro.di;

import android.content.Context;
import com.bfs.mbistro.analytics.AnalyticsTracker;
import com.bfs.mbistro.analytics.FirebaseAnalyticsTracker;
import com.bfs.mbistro.crashreporting.CrashReportingEngine;
import com.bfs.mbistro.crashreporting.FabricReportingEngine;
import com.bfs.mbistro.location.AndroidLocationPermissionsChecker;
import com.bfs.mbistro.location.LocationPermissionsChecker;
import com.bfs.mbistro.network.AndroidNetworkMonitor;
import com.bfs.mbistro.network.NetworkMonitor;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * Scopes have overheads - Using scope only when necessary
 */
@Module public class AppModule {

  private final Context appContext;

  public AppModule(Context appContext) {
    this.appContext = appContext.getApplicationContext();
  }

  @Provides @Singleton Context provideAppContext() {
    return appContext;
  }

  @Provides @Singleton CrashReportingEngine provideCrashReportingEngine(Context context) {
    return new FabricReportingEngine(context);
  }

  @Provides NetworkMonitor provideNetworkMonitor(Context context) {
    return new AndroidNetworkMonitor(context);
  }

  @Provides LocationPermissionsChecker provideLocationEngine(Context context) {
    return new AndroidLocationPermissionsChecker(context);
  }

  @Provides AnalyticsTracker provideAnalyticsTracker() {
    return new FirebaseAnalyticsTracker();
  }
}
