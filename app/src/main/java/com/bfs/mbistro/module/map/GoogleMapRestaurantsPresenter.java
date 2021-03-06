package com.bfs.mbistro.module.map;

import android.annotation.SuppressLint;
import android.util.Pair;
import com.bfs.mbistro.location.LocationPermissionsChecker;
import com.bfs.mbistro.model.Restaurants;
import com.bfs.mbistro.network.ApiService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import java.net.HttpURLConnection;
import retrofit2.HttpException;

import static com.bfs.mbistro.network.ApiService.SORT_CRITERIA_DISTANCE;

public class GoogleMapRestaurantsPresenter extends RestaurantsMapContract.Presenter
    implements OnMapReadyCallback {

  private static final float INITIAL_ZOOM = 16;
  private static final int RADIUS_IN_METERS = 50;


  private GoogleMap googleMap;
  private CompositeDisposable compositeDisposable;
  private boolean scheduleLoad;
  private double latitude;
  private double longitude;

  GoogleMapRestaurantsPresenter(ApiService service, LocationPermissionsChecker conditionsChecker) {
    super(service, conditionsChecker);
  }

  @Override void loadRestaurantsInArea(double lat, double lon) {
    this.latitude = lat;
    this.longitude = lon;
    if (googleMap == null) {
      scheduleLoad = true;
    } else {
      googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), INITIAL_ZOOM));
      scheduleLoad = false;
      loadItems();
    }
  }

  @Override public void attachView(RestaurantsMapContract.RestaurantsMapView view) {
    super.attachView(view);
    compositeDisposable = new CompositeDisposable();
  }

  @Override public void detachView(boolean retainInstance) {
    super.detachView(retainInstance);
    compositeDisposable.clear();
  }

  @SuppressLint("MissingPermission") @Override public void onMapReady(GoogleMap map) {
    googleMap = map;
    if (locationPermissionsChecker.areLocationPermissionsGranted()) {
      googleMap.setMyLocationEnabled(true);
    }
    googleMap.getUiSettings().setMapToolbarEnabled(false);
    googleMap.getUiSettings().setZoomControlsEnabled(true);
    if (scheduleLoad) {
      loadItems();
    }
  }

  private void loadItems() {
    getView().showLoading(false);
    compositeDisposable.add(
        service.getRestaurants(latitude, longitude, RADIUS_IN_METERS, SORT_CRITERIA_DISTANCE)
        .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new RestaurantsMapSubscriber()));
  }

  private class RestaurantsMapSubscriber extends DisposableObserver<Restaurants> {

    @Override public void onComplete() {

    }

    @Override public void onError(Throwable error) {
      if (error instanceof HttpException
          && ((HttpException) error).code() == HttpURLConnection.HTTP_NOT_FOUND) {
        getView().showEmptyView();
      } else {
        getView().showError(error, false);
      }
      scheduleLoad = false;
    }

    @Override public void onNext(Restaurants restaurants) {
      getView().setData(new Pair<>(restaurants, googleMap));
      getView().showContent();
      scheduleLoad = false;
    }
  }
}
