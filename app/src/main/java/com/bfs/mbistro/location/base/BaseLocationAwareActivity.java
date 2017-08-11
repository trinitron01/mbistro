package com.bfs.mbistro.location.base;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import com.bfs.mbistro.BuildConfig;
import com.bfs.mbistro.R;
import com.bfs.mbistro.base.BaseActivity;
import com.bfs.mbistro.location.AndroidLocationPresenter;
import com.bfs.mbistro.location.LocationConditionsView;
import com.bfs.mbistro.location.LocationPermissionsChecker;
import com.google.android.gms.common.api.Status;
import javax.inject.Inject;
import timber.log.Timber;

import static com.bfs.mbistro.AndroidUtils.dismissSnackbar;

public abstract class BaseLocationAwareActivity extends BaseActivity
    implements LocationConditionsView {

  public static final int REQUEST_CODE_LOCATION_SETTINGS = 101;
  private static final String USER_LOCATION_KEY = "USER_LOCATION_KEY";
  private static final int REQUEST_CODE_LOCATION_PERMISSIONS = 102;

  @Inject protected LocationPermissionsChecker locationPermissionsChecker;
  private AndroidLocationPresenter locationPresenter;
  private Snackbar locationSnackbar;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Location savedInstanceLocation = null;
    if (savedInstanceState != null && savedInstanceState.containsKey(USER_LOCATION_KEY)) {
      savedInstanceLocation = savedInstanceState.getParcelable(USER_LOCATION_KEY);
    }
    locationPresenter =
        new AndroidLocationPresenter(locationPermissionsChecker, this, savedInstanceLocation);
    locationPresenter.attachView(this);
  }

  @Override public void onStart() {
    super.onStart();
    if (locationPresenter.isLocationOutdated()) {
      locationPresenter.checkSettingsAndRequestLocation();
    }
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    if (locationPresenter.getLastLocation() != null) {
      outState.putParcelable(USER_LOCATION_KEY, locationPresenter.getLastLocation());
    }
    super.onSaveInstanceState(outState);
  }

  /**
   * Callback received when a permissions request has been completed.
   */
  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    if (requestCode == REQUEST_CODE_LOCATION_PERMISSIONS) {
      locationPresenter.onPermissionRequestCompleted(grantResults);
    }
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE_LOCATION_SETTINGS) {
      locationPresenter.onLocationSettingsChanged(resultCode == Activity.RESULT_OK);
    }
  }

  @Override public void showLocationSettingsView(Status locationSettingsStatus) {
    try {
      // Show the dialog by calling startResolutionForResult(),
      // and check the result in onActivityResult().
      locationSettingsStatus.startResolutionForResult(this, REQUEST_CODE_LOCATION_SETTINGS);
    } catch (IntentSender.SendIntentException e) {
      Timber.e(e);
    }
  }

  @Override public void askForLocationPermissions() {
    ActivityCompat.requestPermissions(this,
        new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
        REQUEST_CODE_LOCATION_PERMISSIONS);
  }

  @Override public void showLocationSearchingProgress() {
    if (locationSnackbar == null || !locationSnackbar.isShownOrQueued()) {
      locationSnackbar =
          Snackbar.make(getSnackbarContainer(), getString(R.string.location_search_in_progress),
              Snackbar.LENGTH_INDEFINITE);
      locationSnackbar.show();
    }
  }

  @Override public void hideLocationSearchingProgress() {
    dismissSnackbar(locationSnackbar);
  }

  @CallSuper @Override public void onNewLocation(double latitude, double longitude) {
    dismissSnackbar(locationSnackbar);
  }

  @Override public void showLocationError(String error) {
    showSnackbar(error);
  }

  @Override public void showDeniedPermissionsView() {
    showSnackbar(this, R.string.location_permission_denied_explanation, R.string.settings,
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

  @Override public void showLocationDisabled() {
    showSnackbar(this, R.string.location_disabled_explanation, R.string.turn_on_location,
        new View.OnClickListener() {
          @Override public void onClick(View v) {
            locationPresenter.requestLocationSettingsChange();
          }
        });
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    locationPresenter.detachView(false);
  }

  @Override public void showLocationApiError(String message) {
    showSnackbar(message);
  }
}