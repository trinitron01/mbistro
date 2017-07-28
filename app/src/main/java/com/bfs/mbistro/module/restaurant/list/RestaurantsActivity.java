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
import com.bfs.mbistro.AndroidUtils;
import com.bfs.mbistro.BuildConfig;
import com.bfs.mbistro.R;
import com.bfs.mbistro.base.BaseActivity;
import com.bfs.mbistro.di.BistroComponent;
import com.bfs.mbistro.location.AndroidLocationConditionsChecker;
import com.bfs.mbistro.location.AndroidLocationPresenter;
import com.bfs.mbistro.location.LocationConditionsView;
import com.google.android.gms.common.api.Status;
import timber.log.Timber;

public class RestaurantsActivity extends BaseActivity implements LocationConditionsView {

  public static final int REQUEST_LOCATION = 10002;
  private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 111;

  private AndroidLocationPresenter locationPresenter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list);
    Toolbar toolbar = findView(R.id.toolbar);
    setSupportActionBar(toolbar);

    locationPresenter =
        new AndroidLocationPresenter(new AndroidLocationConditionsChecker(this), this);
    locationPresenter.attachView(this);
  }

  @Override protected void inject(BistroComponent component) {
    component.inject(this);
  }

  @Override public void onStart() {
    super.onStart();
    locationPresenter.checkSettingsAndRequestLocation();
  }

  /**
   * Shows a {@link Snackbar} using {@code text}.
   *
   * @param text The Snackbar text.
   */
  private void showSnackbar(final CharSequence text) {
    View container = findViewById(android.R.id.content);
    if (container != null) {
      Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
    }
  }

  private void showSnackbar(Activity activity, final int mainTextStringId, final int actionStringId,
      View.OnClickListener listener) {
    Snackbar.make(findViewById(android.R.id.content), activity.getString(mainTextStringId),
        Snackbar.LENGTH_INDEFINITE).setAction(activity.getString(actionStringId), listener).show();
  }

  /**
   * Callback received when a permissions request has been completed.
   */
  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
      locationPresenter.onPermissionRequestCompleted(grantResults);
    }
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_LOCATION) {
      locationPresenter.onLocationSettingsChanged(resultCode == Activity.RESULT_OK);
    }
  }

  @Override public void showLocationSettingsView(Status locationSettingsStatus) {
    try {
      // Show the dialog by calling startResolutionForResult(),
      // and check the result in onActivityResult().
      locationSettingsStatus.startResolutionForResult(RestaurantsActivity.this, REQUEST_LOCATION);
    } catch (IntentSender.SendIntentException e) {
      Timber.e(e);
    }
  }

  @Override public void askForLocationPermissions() {
    ActivityCompat.requestPermissions(this,
        new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
        REQUEST_PERMISSIONS_REQUEST_CODE);
  }

  @Override public void showLocation(Location location) {
    RestaurantsFragment restaurantsFragment =
        (RestaurantsFragment) getSupportFragmentManager().findFragmentById(
            R.id.restaurants_fragment);
    if (AndroidUtils.isFragmentAlive(restaurantsFragment)) {
      restaurantsFragment.onNewLocation(location);
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

