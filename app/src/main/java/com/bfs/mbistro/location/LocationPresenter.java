package com.bfs.mbistro.location;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

abstract class LocationPresenter extends MvpBasePresenter<LocationConditionsView> {

  static final int LOCATION_REQUEST_INTERVAL_MINUTES = 5;

  public abstract void checkSettingsAndRequestLocation();

  public abstract boolean isLocationOutdated();

  public abstract void onPermissionRequestCompleted(int... grantResults);

  public abstract void onLocationSettingsChanged(boolean isLocationNowAvailable);

  public abstract void requestLocationSettingsChange();
}
