package com.bfs.mbistro.location;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import com.bfs.mbistro.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnSuccessListener;
import java.util.concurrent.TimeUnit;
import timber.log.Timber;

public class AndroidLocationPresenter extends LocationPresenter
    implements OnSuccessListener<Location> {


  private final FragmentActivity activity;
  private final LocationConditionsChecker conditionsChecker;
  private final FusedLocationProviderClient fusedLocationClient;
  private final GoogleApiClient googleApiClient;
  private final LocationSettingsResultResultCallback resultCallback;
  private long lastLocationTimestampMillis;

  public AndroidLocationPresenter(LocationConditionsChecker conditionsChecker,
      FragmentActivity activity) {
    this.conditionsChecker = conditionsChecker;
    this.activity = activity;
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
    googleApiClient = new GoogleApiClient.Builder(activity).addApi(Places.GEO_DATA_API)
        .addApi(Places.PLACE_DETECTION_API)
        .addApi(LocationServices.API)
        .enableAutoManage(activity, new GoogleApiConnectionFailedListener())
        .build();
    resultCallback = new LocationSettingsResultResultCallback();
  }

  @NonNull private static LocationRequest createLocationRequest() {
    LocationRequest locationRequest = LocationRequest.create();
    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    return locationRequest;
  }

  /**
   * Gets the address for the last known location.
   */
  @Override public void checkSettingsAndRequestLocation() {
    if (conditionsChecker.areLocationPermissionsGranted()) {
      //noinspection MissingPermission
      fusedLocationClient.getLastLocation()
          .addOnSuccessListener(activity, AndroidLocationPresenter.this);
    } else {
      getView().askForLocationPermissions();
    }
  }

  @Override public boolean isLocationOutdated() {
    return lastLocationTimestampMillis == 0
        || TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - lastLocationTimestampMillis)
        > AndroidLocationPresenter.LOCATION_REQUEST_INTERVAL_MINUTES;
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
      getView().showDeniedPermissionsView();
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
    final LocationRequest locationRequest = createLocationRequest();
    LocationSettingsRequest.Builder builder =
        new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
    builder.setAlwaysShow(true);
    final PendingResult<LocationSettingsResult> result =
        LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
    resultCallback.setLocationRequest(locationRequest);
    result.setResultCallback(resultCallback);
  }

  @Override public void detachView(boolean retainInstance) {
    super.detachView(retainInstance);
    fusedLocationClient.removeLocationUpdates(resultCallback);
  }

  @Override public void onSuccess(Location location) {
    if (location == null) {
      requestLocationSettingsChange();
    } else {
      onLocationObtained(location);
    }
  }

  private void onLocationObtained(@NonNull Location newLocation) {
    lastLocationTimestampMillis = System.currentTimeMillis();
    getView().hideLocationSearchingProgress();
    getView().showFoundLocation(newLocation);
  }

  private class GoogleApiConnectionFailedListener
      implements GoogleApiClient.OnConnectionFailedListener {

    @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
      getView().showLocationApiError(
          activity.getString(R.string.google_play_api_client_connection_failed));
      Timber.w("GoogleApiClient connection failed "
          + connectionResult.getErrorMessage()
          + ' '
          + connectionResult.getErrorCode());
    }
  }

  private class LocationSettingsResultResultCallback extends LocationCallback
      implements ResultCallback<LocationSettingsResult> {

    private LocationRequest locationRequest;

    LocationSettingsResultResultCallback() {

    }

    void setLocationRequest(LocationRequest locationRequest) {
      this.locationRequest = locationRequest;
    }

    @Override public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
      final Status status = locationSettingsResult.getStatus();
      switch (status.getStatusCode()) {
        case CommonStatusCodes.RESOLUTION_REQUIRED:
          getView().showLocationSettingsView(status);
          break;
        case CommonStatusCodes.SUCCESS:
          if (conditionsChecker.areLocationPermissionsGranted()) {
            //noinspection MissingPermission
            fusedLocationClient.requestLocationUpdates(locationRequest, this, Looper.myLooper());
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
        fusedLocationClient.removeLocationUpdates(this);
      } else {
        getView().showLocationError(activity.getString(R.string.error_location_not_found));
      }
    }
  }
}
