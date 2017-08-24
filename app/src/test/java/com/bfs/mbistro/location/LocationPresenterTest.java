package com.bfs.mbistro.location;

import android.location.Location;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class) public class LocationPresenterTest {

  @Mock private LocationPermissionsChecker locationPermissionsChecker;
  @Mock private LocationContract.View locationView;
  @Mock private LocationApi locationApi;

  private LocationContract.Presenter locationPresenter;

  @Before public void setUp() {
    locationPresenter = new LocationPresenter(locationPermissionsChecker, locationApi);
    locationPresenter.attachView(locationView);
  }

  @Test public void shouldShowGetLastLocationFirst() {
    when(locationPermissionsChecker.areLocationPermissionsGranted()).thenReturn(true);
    locationPresenter.checkSettingsAndRequestLocation();
    verify(locationApi, times(1)).requestLatestLocation(locationPresenter);
    verify(locationView, never()).askForLocationPermissions();
  }

  @Test public void shouldAskForPermissionsIfDenied() {
    when(locationPermissionsChecker.areLocationPermissionsGranted()).thenReturn(false);
    locationPresenter.checkSettingsAndRequestLocation();
    verify(locationApi, never()).requestLatestLocation(locationPresenter);
    verify(locationView, times(1)).askForLocationPermissions();
  }

  @Test public void shouldShowLocation() {
    Location location = mock(Location.class);
    locationPresenter.onLocationObtained(location);
    verify(locationView, times(1)).hideLocationSearchingProgress();
    verify(locationView, times(1)).onNewLocation(anyDouble(), anyDouble());
    assertFalse(locationPresenter.isLocationOutdated());
  }

  @Test public void shouldLocationOutdatedBeFalseForNewUpdate() {
    Location location = mock(Location.class);
    locationPresenter.onLocationObtained(location);
    assertFalse(locationPresenter.isLocationOutdated());
  }

  @After public void tearDown() {
    locationPresenter.detachView(false);
  }
}