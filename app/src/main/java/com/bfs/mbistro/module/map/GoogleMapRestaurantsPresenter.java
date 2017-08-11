package com.bfs.mbistro.module.map;

import android.util.Pair;
import com.bfs.mbistro.location.LocationPermissionsChecker;
import com.bfs.mbistro.model.Restaurants;
import com.bfs.mbistro.network.ApiService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class GoogleMapRestaurantsPresenter extends RestaurantsMapContract.Presenter
    implements OnMapReadyCallback {

  private static final float INITIAL_ZOOM = 16;

  private GoogleMap googleMap;
  private CompositeSubscription compositeSubscription;
  private boolean scheduleLoad;
  private double latitude;
  private double longitude;

  GoogleMapRestaurantsPresenter(ApiService service, LocationPermissionsChecker conditionsChecker) {
    super(service, conditionsChecker);
  }

  @Override void loadRestaurantsInArea(double latitude, double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
    if (googleMap == null) {
      scheduleLoad = true;
    } else {
      googleMap.moveCamera(
          CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), INITIAL_ZOOM));
      scheduleLoad = false;
      loadItems();
    }
  }

  @Override public void attachView(RestaurantsMapContract.RestaurantsMapView view) {
    super.attachView(view);
    compositeSubscription = new CompositeSubscription();
  }

  @Override public void detachView(boolean retainInstance) {
    super.detachView(retainInstance);
    compositeSubscription.unsubscribe();
  }

  /**
   * Manipulates the map once available.
   * This callback is triggered when the map is ready to be used.
   * This is where we can add markers or lines, add listeners or move the camera. In this case,
   * we just add a marker near Sydney, Australia.
   * If Google Play services is not installed on the device, the user will be prompted to install
   * it inside the SupportMapFragment. This method will only be triggered once the user has
   * installed Google Play services and returned to the app.
   */
  @Override public void onMapReady(GoogleMap map) {
    googleMap = map;

    //noinspection MissingPermission
    googleMap.setMyLocationEnabled(locationPermissionsChecker.areLocationPermissionsGranted());

    if (scheduleLoad) {
      loadItems();
    }
  }

  private void loadItems() {
    getView().showLoading(false);
    compositeSubscription.add(service.getRestaurants(latitude, longitude)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new RestaurantsMapSubscriber()));
  }

  private class RestaurantsMapSubscriber extends Subscriber<Restaurants> {

    @Override public void onCompleted() {

    }

    @Override public void onError(Throwable e) {
      getView().showError(e, false);
      scheduleLoad = false;
    }

    @Override public void onNext(Restaurants restaurants) {
      getView().setData(new Pair<>(restaurants, googleMap));
      getView().showContent();
      scheduleLoad = false;
    }
  }
}
