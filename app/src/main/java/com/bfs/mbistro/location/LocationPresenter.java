package com.bfs.mbistro.location;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationSettingsResult;

public class LocationPresenter extends LocationContract.Presenter {

  private final LocationPermissionsChecker conditionsChecker;
  private final LocationApi locationApi;

  private final Callback resultCallback;

  public LocationPresenter(LocationPermissionsChecker conditionsChecker, LocationApi locationApi) {
    super();
    this.conditionsChecker = conditionsChecker;
    this.locationApi = locationApi;
    resultCallback = new Callback();
  }

  /**
   * Gets the last known location. Here we only check if permissions are enabled
   */
  @Override public void checkSettingsAndRequestLocation() {
    if (conditionsChecker.areLocationPermissionsGranted()) {
      locationApi.requestLatestLocation(this);
    } else {
      getView().askForLocationPermissions();
    }
  }

  @Override public void onPermissionRequestCompleted(int[] grantResults) {
    if (grantResults.length <= 0) {
      // If user interaction was interrupted, the permission request is cancelled and you
      // receive empty arrays.
    } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      // Permission granted.
      checkSettingsAndRequestLocation();
    } else {
      // Permission denied.

      // Notify the user via a SnackBar that they have rejected a core permission for the
      // app, which makes the Activity useless. In a real app, core permissions would
      // typically be best requested during a welcome-screen flow.

      // Additionally, it is important to remember that a permission might have been
      // rejected without asking the user for permission (device policy or "Never ask
      // again" prompts). Therefore, a user interface affordance is typically implemented
      // when permissions are denied. Otherwise, your app could appear unresponsive to
      // touches or interactions which have required permissions.
      getView().showPermissionsDenied();
    }
  }

  @Override public void onLocationSettingsChanged(boolean isLocationNowAvailable) {
    if (isLocationNowAvailable) {
      checkSettingsAndRequestLocation();
    } else {
      getView().showLocationDisabled();
    }
  }

  @Override public void requestLocationSettingsChange() {
    getView().showLocationSearchingProgress();
    locationApi.requestSettingsChange(resultCallback);
  }

  @Override public void detachView(boolean retainInstance) {
    super.detachView(retainInstance);
    locationApi.removeLocationUpdates(resultCallback);
  }

  @Override public void onSuccess(Location location) {
    if (location == null) {
      requestLocationSettingsChange();
    } else {
      onLocationObtained(location);
    }
  }

  class Callback extends LocationCallback implements ResultCallback<LocationSettingsResult> {

    private LocationRequest locationRequest;

    Callback() {

    }

    void setLocationRequest(LocationRequest locationRequest) {
      this.locationRequest = locationRequest;
    }

    @SuppressLint("MissingPermission") @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
      final Status status = locationSettingsResult.getStatus();
      switch (status.getStatusCode()) {
        case CommonStatusCodes.RESOLUTION_REQUIRED:
          getView().showLocationSettings(status);
          break;
        case CommonStatusCodes.SUCCESS:
          if (conditionsChecker.areLocationPermissionsGranted()) {
            locationApi.requestLocationUpdates(locationRequest, this);
          } else {
            getView().askForLocationPermissions();
          }
          break;
        default:
          break;
      }
    }

    @Override public void onLocationResult(LocationResult locationResult) {
      super.onLocationResult(locationResult);
      if (locationResult.getLastLocation() != null) {
        onLocationObtained(locationResult.getLastLocation());
        locationApi.removeLocationUpdates(this);
      } else {
        getView().showLocationNotFoundError();
      }
    }
  }
}
