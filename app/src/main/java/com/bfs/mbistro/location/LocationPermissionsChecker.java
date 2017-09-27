package com.bfs.mbistro.location;

import android.annotation.SuppressLint;

public interface LocationPermissionsChecker {

  @SuppressLint("MissingPermission")
  boolean areLocationPermissionsGranted();
}
