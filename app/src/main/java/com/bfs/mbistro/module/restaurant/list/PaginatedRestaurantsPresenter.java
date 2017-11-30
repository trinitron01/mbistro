package com.bfs.mbistro.module.restaurant.list;

import android.support.v4.util.Pair;
import com.bfs.mbistro.model.RestaurantContainer;
import com.bfs.mbistro.model.Restaurants;
import com.bfs.mbistro.model.location.UserLocationResponse;
import com.bfs.mbistro.module.restaurant.mvp.RestaurantsListContract;
import com.bfs.mbistro.network.ApiService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import retrofit2.HttpException;
import timber.log.Timber;

public class PaginatedRestaurantsPresenter extends RestaurantsListContract.Presenter {

  private static final String ENTITY_TYPE_CITY = "city";

  //API restrictions - max 100 results per search query
  private static final int ITEMS_PAGE_LIMIT = 20;
  private static final int MAX_ITEMS = 100;
  private static final int MAX_ITEMS_LAST_PAGE_INDEX = 80;
  private final ApiService service;
  private int itemsShown;
  private int itemsStartIndex;
  private Integer cityId;
  private CompositeDisposable compositeDisposable;

  public PaginatedRestaurantsPresenter(ApiService service, List<RestaurantContainer> items) {
    super(items);
    this.service = service;
  }

  private static void logException(Throwable error, final String errorTag) {
    if (error instanceof HttpException) {
      HttpException httpException = (HttpException) error;
      try {
        Timber.w(errorTag + " loading error " + httpException.response().errorBody().string());
      } catch (IOException e) {
        Timber.w(errorTag + " loading error ");
        e.printStackTrace();
      }
    }
  }

  @Override public void attachView(RestaurantsListContract.ItemsView view) {
    super.attachView(view);
    compositeDisposable = new CompositeDisposable();
  }

  private void updateOffset(int newResults, int resultsStart) {
    this.itemsShown += newResults;
    this.itemsStartIndex = resultsStart;
    getView().setMoreItemsAvailable(
        newResults > 0 && itemsShown < MAX_ITEMS && itemsStartIndex <= MAX_ITEMS_LAST_PAGE_INDEX);
  }

  @Override public void loadNextItems() {
    if (cityId != null) {
      compositeDisposable.add(
          service.getRestaurants(cityId, ENTITY_TYPE_CITY, itemsShown, ITEMS_PAGE_LIMIT)
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribeWith(new RestaurantsPageSubscriber()));
    } else {
      throw new IllegalStateException("Requested more items but city Id has not been set yet");
    }
  }

  @Override public void loadLocationItems(double latitude, double longitude) {

    itemsShown = 0;
    itemsStartIndex = 0;
    compositeDisposable.add(service.geocode(latitude, longitude)
        .flatMap(
            geocodedLocation -> service.getRestaurants(geocodedLocation.getLocation().getCityId(),
                ENTITY_TYPE_CITY, itemsShown, ITEMS_PAGE_LIMIT),
            (geocodedUserLocation, restaurantsResponse) -> {
              updateOffset(restaurantsResponse.resultsShown, restaurantsResponse.resultsStart);
              return new Pair<>(geocodedUserLocation, restaurantsResponse.restaurants);
            })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(new Consumer<Disposable>() {
          @Override public void accept(Disposable disposable) throws Exception {
            getItems().clear();
            getView().showLoading(false);
          }
        })
        .subscribeWith(new LocationItemsObserver()));
  }



  @Override public void detachView(boolean retainInstance) {
    super.detachView(retainInstance);
    compositeDisposable.clear();
  }

  private class RestaurantsPageSubscriber extends DisposableObserver<Restaurants> {

    @Override public void onError(Throwable error) {
      if (error instanceof HttpException
          && ((HttpException) error).code() == HttpURLConnection.HTTP_NOT_FOUND) {
        getView().showEmptyView();
      } else {
        logException(error, "Paginated List");
        getView().showItemsPageLoadError(error);
      }
    }

    @Override public void onComplete() {

    }

    @Override public void onNext(Restaurants restaurants) {
      updateOffset(restaurants.resultsShown, restaurants.resultsStart);
      onNewItems(restaurants.restaurants, false);
    }
  }

  private class LocationItemsObserver
      extends DisposableObserver<Pair<UserLocationResponse, List<RestaurantContainer>>> {


    @Override public void onNext(
        Pair<UserLocationResponse, List<RestaurantContainer>> locationWithRestaurants) {
      RestaurantsListContract.ItemsView restaurantsView = getView();
      cityId = locationWithRestaurants.first.getLocation().getCityId();
      restaurantsView.showRestaurantsLocation(locationWithRestaurants.first);
      onNewItems(locationWithRestaurants.second, true);
    }

    @Override public void onError(Throwable error) {
      if (error instanceof HttpException
          && ((HttpException) error).code() == HttpURLConnection.HTTP_NOT_FOUND) {
        getView().showEmptyView();
      } else {
        logException(error, "Restaurant List For Location");
        getView().showError(error, false);
      }
    }

    @Override public void onComplete() {

    }
  }
}
