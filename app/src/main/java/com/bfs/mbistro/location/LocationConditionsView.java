package com.bfs.mbistro.location;

import com.google.android.gms.common.api.Status;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface LocationConditionsView extends MvpView, LocationChangedListener {

  void showLocationSettingsView(Status locationSettingsStatus);

  void askForLocationPermissions();

  void showLocationSearchingProgress();

  void hideLocationSearchingProgress();

  void showLocationError(String error);

  void showDeniedPermissionsView();

  void showLocationDisabled();

  void showLocationApiError(String message);
}
