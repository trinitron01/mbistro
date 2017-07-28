package com.bfs.mbistro.location;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

abstract class LocationPresenter extends MvpBasePresenter<LocationConditionsView> {

  public abstract void checkSettingsAndRequestLocation();

  public abstract void onPermissionRequestCompleted(int... grantResults);

  public abstract void onLocationSettingsChanged(boolean isLocationNowAvailable);

  public abstract void requestLocationSettingsChange();
}
