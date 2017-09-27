package com.bfs.mbistro.module.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.bfs.mbistro.JavaUtils;
import com.bfs.mbistro.LocationUtils;
import com.bfs.mbistro.R;
import com.bfs.mbistro.base.view.BaseMvpFragment;
import com.bfs.mbistro.di.BistroComponent;
import com.bfs.mbistro.location.LocationPermissionsChecker;
import com.bfs.mbistro.model.Restaurant;
import com.bfs.mbistro.model.RestaurantContainer;
import com.bfs.mbistro.model.RestaurantLocation;
import com.bfs.mbistro.model.Restaurants;
import com.bfs.mbistro.module.restaurant.details.ui.RestaurantDetailsActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import timber.log.Timber;

public class RestaurantsMapFragment extends
    BaseMvpFragment<FrameLayout, Pair<Restaurants, GoogleMap>, RestaurantsMapContract.RestaurantsMapView, GoogleMapRestaurantsPresenter>
    implements RestaurantsMapContract.RestaurantsMapView, GoogleMap.OnInfoWindowClickListener {

  @Inject protected LocationPermissionsChecker locationPermissionsChecker;

  private double currentLatitude;
  private double currentLongitude;
  private Map<String, Restaurant> markerToRestaurantMap;

  @Override protected void inject(BistroComponent component) {
    component.inject(this);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_maps, container, false);
  }

  @Override public GoogleMapRestaurantsPresenter createPresenter() {
    return new GoogleMapRestaurantsPresenter(service, locationPermissionsChecker);
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    SupportMapFragment mapFragment =
        (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
    mapFragment.getMapAsync(getPresenter());
  }

  @Override public void setData(Pair<Restaurants, GoogleMap> data) {
    Restaurants restaurants = data.first;
    if (JavaUtils.isNotNulNorEmpty(restaurants.restaurants)) {
      markerToRestaurantMap = new HashMap<>(restaurants.restaurants.size());
      GoogleMap googleMap = data.second;
      googleMap.clear();
      googleMap.setOnInfoWindowClickListener(this);
      for (RestaurantContainer restaurantContainer : restaurants.restaurants) {
        MarkerOptions markerOptions = new MarkerOptions();
        Restaurant restaurant = restaurantContainer.getRestaurant();
        RestaurantLocation location = restaurant.location;
        markerOptions.position(new LatLng(location.getLatitude(), location.getLongitude()));
        markerOptions.title(restaurant.getName());
        markerToRestaurantMap.put(googleMap.addMarker(markerOptions).getId(), restaurant);
      }
    }
  }

  @Override public void loadData(boolean pullToRefresh) {
    getPresenter().loadRestaurantsInArea(currentLatitude, currentLongitude);
  }

  @Override public void onNewLocation(double latitude, double longitude) {
    currentLatitude = latitude;
    currentLongitude = longitude;
    loadData(false);
  }

  @Override public void onInfoWindowClick(Marker marker) {
    Restaurant restaurant = markerToRestaurantMap.get(marker.getId());
    if (restaurant == null) {
      Timber.e(
          "Selected marker with id " + marker.getId() + " does not match any available restaurant");
    } else {
      int distanceMeters = LocationUtils.distanceBetweenMeters(currentLatitude, currentLongitude,
          restaurant.location.getLatitude(), restaurant.location.getLongitude());
      RestaurantDetailsActivity.start(getContext(), restaurant, distanceMeters);
    }
  }

  @Override public void showEmptyView() {
    animateEmptyViewIn();
  }
}
