package com.bfs.mbistro.location;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationApi {

  private final FragmentActivity activity;
  private final GoogleApiClient googleApiClient;

  private final FusedLocationProviderClient fusedLocationClient;

  public LocationApi(FragmentActivity activity) {
    this.activity = activity;
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);

    googleApiClient = new GoogleApiClient.Builder(activity).addApi(Places.GEO_DATA_API)
        .addApi(Places.PLACE_DETECTION_API)
        .addApi(LocationServices.API)
        .enableAutoManage(activity, null)
        .build();
  }

  @NonNull private static LocationRequest createLocationRequest() {
    LocationRequest locationRequest = LocationRequest.create();
    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    return locationRequest;
  }

  @SuppressLint("MissingPermission") void requestLatestLocation(
      OnSuccessListener<Location> listener) {
    fusedLocationClient.getLastLocation().addOnSuccessListener(activity, listener);
  }

  void requestSettingsChange(LocationPresenter.Callback resultCallback) {
    final LocationRequest locationRequest = createLocationRequest();
    LocationSettingsRequest.Builder builder =
        new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
    builder.setAlwaysShow(true);
    final PendingResult<LocationSettingsResult> result =
        LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
    resultCallback.setLocationRequest(locationRequest);
    result.setResultCallback(resultCallback);
  }

  @SuppressLint("MissingPermission") void requestLocationUpdates(LocationRequest locationRequest,
      LocationCallback locationCallback) {
    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback,
        Looper.myLooper());
  }

  void removeLocationUpdates(LocationPresenter.Callback resultCallback) {
    fusedLocationClient.removeLocationUpdates(resultCallback);
  }
}

