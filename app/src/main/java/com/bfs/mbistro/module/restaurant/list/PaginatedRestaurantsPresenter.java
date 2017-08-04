package com.bfs.mbistro.module.restaurant.list;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import com.bfs.mbistro.model.RestaurantContainer;
import com.bfs.mbistro.model.Restaurants;
import com.bfs.mbistro.model.location.UserLocation;
import com.bfs.mbistro.module.restaurant.mvp.RestaurantsContract;
import com.bfs.mbistro.network.ApiService;
import java.io.IOException;
import java.util.List;
import retrofit2.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import timber.log.Timber;

class PaginatedRestaurantsPresenter extends RestaurantsContract.Presenter {

  private static final String ENTITY_TYPE_CITY = "city";
  //todo chyba mozna pl wywalic
  private static final String LANGUAGE_PL = "pl";
  //API restrictions - max 100 results per search query
  private static final int ITEMS_PAGE_LIMIT = 20;
  private static final int MAX_ITEMS = 100;
  private static final int MAX_ITEMS_LAST_PAGE_INDEX = 80;
  private final ApiService service;
  private int itemsShown;
  private int itemsStartIndex;
  private Integer cityId;

  PaginatedRestaurantsPresenter(ApiService service, List<RestaurantContainer> items) {
    super(items);
    this.service = service;
  }

  private static void logException(Throwable error) {
    if (error instanceof HttpException) {
      HttpException httpException = (HttpException) error;
      try {
        Timber.w("Paginated List loading error " + httpException.response().errorBody().string());
      } catch (IOException e) {
        Timber.w("Paginated List loading error ");
        e.printStackTrace();
      }
    }
  }

  private void updateOffset(int resultsShown, int resultsStart) {
    this.itemsShown += resultsShown;
    this.itemsStartIndex = resultsStart;
    getView().setMoreItemsAvailable(
        itemsShown < MAX_ITEMS && itemsStartIndex <= MAX_ITEMS_LAST_PAGE_INDEX);
  }

  @Override public void loadNextItems() {
    if (cityId != null) {
      service.getRestaurants(cityId, ENTITY_TYPE_CITY, LANGUAGE_PL, itemsShown, ITEMS_PAGE_LIMIT)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new RestaurantsPageSubscriber());
    } else {
      throw new IllegalStateException("Requested more items but city Id has not been set yet");
    }
  }

  @Override public void loadLocationItems(@NonNull Location location) {
    itemsShown = 0;
    itemsStartIndex = 0;
    service.geocode(location.getLatitude(), location.getLongitude(), LANGUAGE_PL)
        .flatMap(new Func1<UserLocation, Observable<Restaurants>>() {
          @Override public Observable<Restaurants> call(UserLocation geocodedLocation) {
            return service.getRestaurants(geocodedLocation.getLocation().getCityId(),
                ENTITY_TYPE_CITY, LANGUAGE_PL, itemsShown, ITEMS_PAGE_LIMIT);
          }
        }, new Func2<UserLocation, Restaurants, Pair<UserLocation, List<RestaurantContainer>>>() {
          @Override public Pair<UserLocation, List<RestaurantContainer>> call(
              UserLocation geocodedUserLocation, Restaurants restaurantsResponse) {
            updateOffset(restaurantsResponse.resultsShown, restaurantsResponse.resultsStart);
            return new Pair<>(geocodedUserLocation, restaurantsResponse.restaurants);
          }
        })
        .subscribeOn(Schedulers.io())
        .doOnSubscribe(new Action0() {
          @Override public void call() {
            getView().showProgress();
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new PaginatedListWithLocationSubscriber());
  }

  private class PaginatedListWithLocationSubscriber
      extends Subscriber<Pair<UserLocation, List<RestaurantContainer>>> {

    PaginatedListWithLocationSubscriber() {
    }

    @Override public void onCompleted() {

    }

    @Override public void onError(Throwable error) {
      logException(error);
      getView().showItemsLoadError(error);
    }

    @Override
    public void onNext(Pair<UserLocation, List<RestaurantContainer>> userLocationListPair) {
      RestaurantsContract.ItemsView restaurantsView = getView();
      cityId = userLocationListPair.first.getLocation().getCityId();
      restaurantsView.showRestaurantsLocation(userLocationListPair.first);
      onNewItems(userLocationListPair.second, true);
    }
  }

  private class RestaurantsPageSubscriber extends Subscriber<Restaurants> {

    @Override public void onCompleted() {

    }

    @Override public void onError(Throwable error) {
      logException(error);
      getView().showItemsPageLoadError(error);
    }

    @Override public void onNext(Restaurants restaurants) {
      updateOffset(restaurants.resultsShown, restaurants.resultsStart);
      onNewItems(restaurants.restaurants, false);
    }
  }
}
