package com.bfs.mbistro;

import android.content.Context;
import android.location.LocationManager;

public class LocationUtils {

  public static final double EARTH_RADIUS_KM = 6371.0;

  public static final double ONE_EIGHTY = 180.0;

  public static final double ONE_KM_IN_METERS = 1000.0;

  public static final double TWO_DIGIT = 2.0;

  private LocationUtils() {
  }

  public static boolean isAnyLocationProviderOn(Context context) {
    return isGpsOn(context) || isNetworkLocationProviderOn(context);
  }

  public static boolean isGpsOn(Context context) {
    return isProviderEnabled(context, LocationManager.GPS_PROVIDER);
  }

  public static boolean isNetworkLocationProviderOn(Context context) {
    return isProviderEnabled(context, LocationManager.NETWORK_PROVIDER);
  }

  private static boolean isProviderEnabled(Context context, String provider) {
    return context != null && ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE))
        .isProviderEnabled(provider);
  }

  @SuppressWarnings("NumericCastThatLosesPrecision")
  public static int distanceBetweenMeters(double latitude, double longitude, double endLatitude,
      double endLongitude) {
    double fromPx = longitude * Math.PI / ONE_EIGHTY;
    double fromPy = latitude * Math.PI / ONE_EIGHTY;
    double toPx = endLongitude * Math.PI / ONE_EIGHTY;
    double toPy = endLatitude * Math.PI / ONE_EIGHTY;
    double x = (toPx - fromPx) * StrictMath.cos((fromPy + toPy) / TWO_DIGIT);
    double y = toPy - fromPy;
    double dist = Math.sqrt(x * x + y * y) * EARTH_RADIUS_KM;
    return (int) (dist * ONE_KM_IN_METERS);
  }
}
