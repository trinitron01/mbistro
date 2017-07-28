package com.bfs.mbistro.location;

import android.location.Location;
import com.google.android.gms.common.api.Status;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface LocationConditionsView extends MvpView {

  void showLocationSettingsView(Status locationSettingsStatus);

  void askForLocationPermissions();

  void showLocation(Location location);

  void showLocationError(String error);

  void showDeniedPermissionsView();

  void showLocationDisabled();

  void showLocationApiError(String message);
}
