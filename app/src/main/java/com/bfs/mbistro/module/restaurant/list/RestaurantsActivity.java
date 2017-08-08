package com.bfs.mbistro.module.restaurant.list;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.bfs.mbistro.BuildConfig;
import com.bfs.mbistro.LocationUtils;
import com.bfs.mbistro.R;
import com.bfs.mbistro.base.BaseActivity;
import com.bfs.mbistro.di.BistroComponent;
import com.bfs.mbistro.location.AndroidLocationConditionsChecker;
import com.bfs.mbistro.location.AndroidLocationPresenter;
import com.bfs.mbistro.location.LocationConditionsView;
import com.google.android.gms.common.api.Status;
import timber.log.Timber;

import static com.bfs.mbistro.AndroidUtils.dismissSnackbar;
import static com.bfs.mbistro.AndroidUtils.isFragmentAlive;

public class RestaurantsActivity extends BaseActivity implements LocationConditionsView {

  public static final int REQUEST_CODE_LOCATION_SETTINGS = 101;
  private static final int REQUEST_CODE_LOCATION_PERMISSIONS = 102;
  private static final String USER_LOCATION_KEY = "USER_LOCATION_KEY";
  private static final int DISTANCE_DIFF_THRESHOLD_METERS = 500;

  private AndroidLocationPresenter locationPresenter;
  private Snackbar locationSnackbar;
  private Location lastLocation;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.restaurant_list);
    Toolbar toolbar = findView(R.id.toolbar);
    setSupportActionBar(toolbar);

    locationPresenter =
        new AndroidLocationPresenter(new AndroidLocationConditionsChecker(this), this);
    locationPresenter.attachView(this);
    if (savedInstanceState != null && savedInstanceState.containsKey(USER_LOCATION_KEY)) {
      lastLocation = savedInstanceState.getParcelable(USER_LOCATION_KEY);
    }
  }

  @Override protected void inject(BistroComponent component) {
    component.inject(this);
  }

  @Override public void onStart() {
    super.onStart();
    if (locationPresenter.isLocationOutdated()) {
      locationPresenter.checkSettingsAndRequestLocation();
    }
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    if (lastLocation != null) {
      outState.putParcelable(USER_LOCATION_KEY, lastLocation);
    }
    super.onSaveInstanceState(outState);
  }

  /**
   * Shows a {@link Snackbar} using {@code text}.
   *
   * @param text The Snackbar text.
   */
  private void showSnackbar(final CharSequence text) {
    View container = getSnackbarContainer();
    if (container != null) {
      Snackbar snackbar = Snackbar.make(container, text, Snackbar.LENGTH_LONG);
      snackbar.show();
    }
  }

  protected View getSnackbarContainer() {
    return findViewById(android.R.id.content);
  }

  private void showSnackbar(Activity activity, final int mainTextStringId, final int actionStringId,
      View.OnClickListener listener) {
    Snackbar.make(getSnackbarContainer(), activity.getString(mainTextStringId),
        Snackbar.LENGTH_INDEFINITE).setAction(activity.getString(actionStringId), listener).show();
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
      locationSettingsStatus.startResolutionForResult(RestaurantsActivity.this,
          REQUEST_CODE_LOCATION_SETTINGS);
    } catch (IntentSender.SendIntentException e) {
      Timber.e(e);
    }
  }

  private boolean locationChangedEnough(Location location) {
    return LocationUtils.distanceBetweenMeters(lastLocation.getLatitude(),
        lastLocation.getLongitude(), location.getLatitude(), location.getLongitude())
        > DISTANCE_DIFF_THRESHOLD_METERS;
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

  @Override public void showFoundLocation(Location location) {
    dismissSnackbar(locationSnackbar);
    RestaurantsFragment restaurantsFragment =
        (RestaurantsFragment) getSupportFragmentManager().findFragmentById(
            R.id.restaurants_fragment);

    if (isFragmentAlive(restaurantsFragment)) {
      if (lastLocation == null || locationChangedEnough(location)) {
        restaurantsFragment.onNewLocation(location.getLatitude(), location.getLongitude());
        lastLocation = location;
      }
    } else {
      Timber.w("UserLocation changed but RestaurantsFragment is no longer alive");
    }
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

  @Override public void showLocationApiError(String message) {
    showSnackbar(message);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    locationPresenter.detachView(false);
  }
}

