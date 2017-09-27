package com.bfs.mbistro.module.restaurant.list;

import android.support.v4.util.Pair;
import com.bfs.mbistro.model.RestaurantContainer;
import com.bfs.mbistro.model.Restaurants;
import com.bfs.mbistro.model.location.UserLocation;
import com.bfs.mbistro.module.restaurant.mvp.RestaurantsListContract;
import com.bfs.mbistro.network.ApiService;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import retrofit2.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

class PaginatedRestaurantsPresenter extends RestaurantsListContract.Presenter {

  private static final String ENTITY_TYPE_CITY = "city";

  //API restrictions - max 100 results per search query
  private static final int ITEMS_PAGE_LIMIT = 20;
  private static final int MAX_ITEMS = 100;
  private static final int MAX_ITEMS_LAST_PAGE_INDEX = 80;
  private final ApiService service;
  private int itemsShown;
  private int itemsStartIndex;
  private Integer cityId;
  private CompositeSubscription compositeSubscription;

  PaginatedRestaurantsPresenter(ApiService service, List<RestaurantContainer> items) {
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
    compositeSubscription = new CompositeSubscription();
  }

  private void updateOffset(int resultsShown, int resultsStart) {
    this.itemsShown += resultsShown;
    this.itemsStartIndex = resultsStart;
    getView().setMoreItemsAvailable(
        itemsShown > 0 && itemsShown < MAX_ITEMS && itemsStartIndex <= MAX_ITEMS_LAST_PAGE_INDEX);
  }

  @Override public void loadNextItems() {
    if (cityId != null) {
      compositeSubscription.add(
          service.getRestaurants(cityId, ENTITY_TYPE_CITY, itemsShown, ITEMS_PAGE_LIMIT)
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(new RestaurantsPageSubscriber()));
    } else {
      throw new IllegalStateException("Requested more items but city Id has not been set yet");
    }
  }

  @Override public void loadLocationItems(double latitude, double longitude) {

    itemsShown = 0;
    itemsStartIndex = 0;
    compositeSubscription.add(service.geocode(latitude, longitude)
        .flatMap(new Func1<UserLocation, Observable<Restaurants>>() {
          @Override public Observable<Restaurants> call(UserLocation geocodedLocation) {
            return service.getRestaurants(geocodedLocation.getLocation().getCityId(),
                ENTITY_TYPE_CITY, itemsShown, ITEMS_PAGE_LIMIT);
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
            getItems().clear();
            getView().showLoading(false);
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new PaginatedListWithLocationSubscriber()));
  }

  @Override public void detachView(boolean retainInstance) {
    super.detachView(retainInstance);
    compositeSubscription.unsubscribe();
  }

  private class PaginatedListWithLocationSubscriber
      extends Subscriber<Pair<UserLocation, List<RestaurantContainer>>> {

    PaginatedListWithLocationSubscriber() {
    }

    @Override public void onCompleted() {

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

    @Override
    public void onNext(Pair<UserLocation, List<RestaurantContainer>> locationWithRestaurants) {
      RestaurantsListContract.ItemsView restaurantsView = getView();
      cityId = locationWithRestaurants.first.getLocation().getCityId();
      restaurantsView.showRestaurantsLocation(locationWithRestaurants.first);
      onNewItems(locationWithRestaurants.second, true);
    }
  }

  private class RestaurantsPageSubscriber extends Subscriber<Restaurants> {

    @Override public void onCompleted() {

    }

    @Override public void onError(Throwable error) {
      if (error instanceof HttpException
          && ((HttpException) error).code() == HttpURLConnection.HTTP_NOT_FOUND) {
        getView().showEmptyView();
      } else {
        logException(error, "Paginated List");
        getView().showItemsPageLoadError(error);
      }
    }

    @Override public void onNext(Restaurants restaurants) {
      updateOffset(restaurants.resultsShown, restaurants.resultsStart);
      onNewItems(restaurants.restaurants, false);
    }
  }
}
