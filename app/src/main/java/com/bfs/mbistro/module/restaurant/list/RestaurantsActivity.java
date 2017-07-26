package com.bfs.mbistro.module.restaurant.list;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.bfs.mbistro.AndroidUtils;
import com.bfs.mbistro.BuildConfig;
import com.bfs.mbistro.R;
import com.bfs.mbistro.base.BaseActivity;
import com.bfs.mbistro.di.BistroComponent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.places.Places;

public class RestaurantsActivity extends BaseActivity {

  public static final int REQUEST_LOCATION = 10002;
  private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 111;

  private GoogleApiClient googleApiClient;
  private FusedLocationProviderClient fusedLocationClient;
  private Location lastLocation;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    initGoogleClient();
  }

  @Override protected void inject(BistroComponent component) {
    component.inject(this);
  }

  private void initGoogleClient() {
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    googleApiClient = new GoogleApiClient.Builder(this).addApi(Places.GEO_DATA_API)
        .addApi(Places.PLACE_DETECTION_API)
        .addApi(LocationServices.API)
        .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
          @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            showSnackbar("on connection failed");
          }
        })
        .build();

    LocationRequest locationRequest = LocationRequest.create();
    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    LocationSettingsRequest.Builder builder =
        new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

    builder.setAlwaysShow(true);

    final PendingResult<LocationSettingsResult> result =
        LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

      @Override public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
          case CommonStatusCodes.RESOLUTION_REQUIRED:
            try {
              // Show the dialog by calling startResolutionForResult(),
              // and check the result in onActivityResult().
              status.startResolutionForResult(RestaurantsActivity.this, REQUEST_LOCATION);
            } catch (IntentSender.SendIntentException e) {
              // Ignore the error.
            }
            break;
          case CommonStatusCodes.SUCCESS:

            break;
        }
      }
    });
  }

  private void requestPerms() {
  /*  ActivityCompat.requestPermissions(this,
        new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
        REQUEST_PERMISSIONS_REQUEST_CODE);*/
  }

  @Override public void onStart() {
    super.onStart();
    if (checkPermissions()) {
      getAddress();
    } else {
      requestPerms();
    }
  }

  /**
   * Gets the address for the last known location.
   */
  @SuppressWarnings("MissingPermission") private void getAddress() {
/*    fusedLocationClient.getLastLocation()
        .addOnSuccessListener(this, new OnSuccessListener<android.location.Location>() {
          @Override public void onSuccess(Location location) {
            if (location == null) {
              String text = "null location";
              onLocation(text);
            } else {
              lastLocation = location;
                *//*    service.getEstablishments(lastLocation.getLatitude(),lastLocation.getLongitude(),PL)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleSubscriber<Establishments>() {
                        @Override
                        public void onSuccess(Establishments value) {

                        }

                        @Override
                        public void onError(Throwable error) {

                        }
                    });*//*
              onLocation("location found");
            }
            // Determine whether a Geocoder is available.
            if (!Geocoder.isPresent()) {
              onLocation(getString(R.string.no_geocoder_available));
            }
          }

          private void onLocation(String text) {
            // showSnackbar(text);
          }
        })
        .addOnFailureListener(this, new OnFailureListener() {
          @Override public void onFailure(@NonNull Exception e) {
            //  showSnackbar("getLastLocation:onFailure");
          }
        });*/
  }

  /**
   * Shows a {@link Snackbar} using {@code text}.
   *
   * @param text The Snackbar text.
   */
  private void showSnackbar(final String text) {
    View container = findViewById(android.R.id.content);
    if (container != null) {
      Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
    }
  }

  /**
   * Return the current state of the permissions needed.
   */
  private boolean checkPermissions() {
    int permissionState =
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
    return permissionState == PackageManager.PERMISSION_GRANTED;
  }

  /**
   * Callback received when a permissions request has been completed.
   */
  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
      if (grantResults.length <= 0) {
        // If user interaction was interrupted, the permission request is cancelled and you
        // receive empty arrays.
      } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        // Permission granted.
        getAddress();
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
        AndroidUtils.showSnackbar(this, R.string.permission_denied_explanation, R.string.settings,
            new View.OnClickListener() {
              @Override public void onClick(View view) {
                // Build intent that displays the BistroApp settings screen.
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
                intent.setData(uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
              }
            });
      }
    }
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_LOCATION && resultCode == Activity.RESULT_OK) {
      getAddress();
    }
  }
}

