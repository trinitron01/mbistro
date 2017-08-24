package com.bfs.mbistro.location;

import com.google.android.gms.common.api.Status;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface LocationConditionsView extends MvpView, LocationChangedListener {

  void showLocationSettings(Status locationSettingsStatus);

  void askForLocationPermissions();

  void showLocationSearchingProgress();

  void hideLocationSearchingProgress();

  void showLocationNotFoundError();

  void showPermissionsDenied();

  void showLocationDisabled();

  void showLocationApiError();
}
