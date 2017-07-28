package com.bfs.mbistro.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

public class AndroidLocationConditionsChecker implements LocationConditionsChecker {

  private final Context context;

  public AndroidLocationConditionsChecker(Context context) {
    this.context = context;
  }

  @Override public boolean areLocationPermissionsGranted() {
    int permissionState =
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
    return permissionState == PackageManager.PERMISSION_GRANTED;
  }
}
