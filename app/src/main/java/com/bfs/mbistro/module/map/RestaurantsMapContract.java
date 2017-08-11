package com.bfs.mbistro.module.map;

import android.util.Pair;
import com.bfs.mbistro.location.LocationChangedListener;
import com.bfs.mbistro.location.LocationPermissionsChecker;
import com.bfs.mbistro.model.Restaurants;
import com.bfs.mbistro.network.ApiService;
import com.google.android.gms.maps.GoogleMap;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView;

class RestaurantsMapContract {

  interface RestaurantsMapView
      extends MvpLceView<Pair<Restaurants, GoogleMap>>, LocationChangedListener {

  }

  abstract static class Presenter extends MvpBasePresenter<RestaurantsMapView> {

    protected final ApiService service;
    final LocationPermissionsChecker locationPermissionsChecker;

    Presenter(ApiService service, LocationPermissionsChecker conditionsChecker) {
      this.service = service;
      locationPermissionsChecker = conditionsChecker;
    }

    abstract void loadRestaurantsInArea(double latitude, double longitude);
  }
}
