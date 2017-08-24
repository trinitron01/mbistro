package com.bfs.mbistro.location;

import android.location.Location;
import android.support.annotation.NonNull;
import com.bfs.mbistro.LocationUtils;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import java.util.concurrent.TimeUnit;

abstract class LocationPresenter extends MvpBasePresenter<LocationConditionsView> {

  static final int LOCATION_REQUEST_INTERVAL_MINUTES = 5;
  private static final int DISTANCE_DIFF_THRESHOLD_METERS = 500;
  private Location lastLocation;
  private long lastLocationTimestampMillis;

  LocationPresenter() {
  }

  public abstract void checkSettingsAndRequestLocation();

  public abstract void onPermissionRequestCompleted(int... grantResults);

  public abstract void onLocationSettingsChanged(boolean isLocationNowAvailable);

  public abstract void requestLocationSettingsChange();

  final void onLocationObtained(@NonNull Location location) {
    lastLocationTimestampMillis = System.currentTimeMillis();
    if (lastLocation == null || locationChangedEnough(location)) {
      lastLocation = location;
      getView().hideLocationSearchingProgress();
      getView().onNewLocation(location.getLatitude(), location.getLongitude());
    }
  }

  private boolean locationChangedEnough(Location location) {
    return LocationUtils.distanceBetweenMeters(lastLocation.getLatitude(),
        lastLocation.getLongitude(), location.getLatitude(), location.getLongitude())
        > DISTANCE_DIFF_THRESHOLD_METERS;
  }

  public boolean isLocationOutdated() {
    return lastLocationTimestampMillis == 0
        || TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - lastLocationTimestampMillis)
        > LocationPresenter.LOCATION_REQUEST_INTERVAL_MINUTES;
  }
}
