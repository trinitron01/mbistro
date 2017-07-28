package com.bfs.mbistro.module.restaurant.list;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bfs.mbistro.AndroidUtils;
import com.bfs.mbistro.BistroApp;
import com.bfs.mbistro.CollectionUtils;
import com.bfs.mbistro.LocationUtils;
import com.bfs.mbistro.R;
import com.bfs.mbistro.base.adapter.OnLoadMoreListener;
import com.bfs.mbistro.model.RestaurantContainer;
import com.bfs.mbistro.model.Restaurants;
import com.bfs.mbistro.model.location.UserLocation;
import com.bfs.mbistro.module.restaurant.details.ui.RestaurantDetailsActivity;
import com.bfs.mbistro.module.restaurant.mvp.RestaurantsContract;
import com.bfs.mbistro.network.ApiService;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import retrofit2.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static com.bfs.mbistro.AndroidUtils.showSnackbar;

public class RestaurantsFragment extends Fragment
    implements OnLoadMoreListener, RestaurantsContract.ItemsView {

  private static final int DISTANCE_DIFF_THRESHOLD_METERS = 500;
  private static final String PL = "pl";
  private static final String LIST_KEY = "LIST_KEY";
  private static final String ENTITY_TYPE_CITY = "city";
  private static final int ITEMS_PAGE_LIMIT = 20;
  private static final String USER_LOCATION_KEY = "USER_LOCATION_KEY";
  @Inject protected ApiService service;
  private RestaurantLineAdapter restaurantAdapter;
  private int itemsShown;
  private int itemsStartIndex;
  private PaginatedList<RestaurantContainer> paginatedList;
  private RestaurantsContract.Presenter restaurantsPresenter;
  private RecyclerView recyclerView;
  private View progressBar;
  private UserLocation userLocation;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.restaurants_list, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
    progressBar = view.findViewById(R.id.loadingView);
    recyclerView.setLayoutManager(
        new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    recyclerView.setHasFixedSize(true);
    restaurantsPresenter = new RestaurantsContract.Presenter(new ArrayList<RestaurantContainer>());
    restaurantsPresenter.attachView(this);
    restaurantAdapter =
        new RestaurantLineAdapter(R.layout.progress_indeterminate, restaurantsPresenter);
    restaurantAdapter.setOnLoadMoreListener(this);
    restaurantAdapter.setIsMoreDataAvailable(false);
    recyclerView.setAdapter(restaurantAdapter);
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    List<RestaurantContainer> restaurants = restaurantAdapter.getDataSet();
    if (CollectionUtils.isNotNullNorEmpty(restaurants) && userLocation != null) {
      paginatedList =
          new RestaurantContainerPaginatedList(restaurants, itemsShown, itemsStartIndex);
      outState.putParcelable(LIST_KEY, paginatedList);
      outState.putParcelable(USER_LOCATION_KEY, userLocation);
    }
    super.onSaveInstanceState(outState);
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    ((BistroApp) getActivity().getApplication()).component.inject(this);
    if (savedInstanceState != null && savedInstanceState.containsKey(LIST_KEY) && savedInstanceState
        .containsKey(USER_LOCATION_KEY)) {
      paginatedList = savedInstanceState.getParcelable(LIST_KEY);
      updateOffset(paginatedList.resultsShown, paginatedList.resultsStart);
      List<RestaurantContainer> items = paginatedList.newItems;
      userLocation = savedInstanceState.getParcelable(USER_LOCATION_KEY);
      restaurantsPresenter.appendItems(items);
    }
  }

  @Override public void onLoadMore() {
    loadNext();
  }

  private void loadNext() {
    service.getRestaurants(userLocation.getLocation().getCityId(), ENTITY_TYPE_CITY, PL, itemsShown,
        ITEMS_PAGE_LIMIT)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new RestaurantsPageSubscriber());
  }

  private void loadForLocation(Pair<UserLocation, List<RestaurantContainer>> cachedItems,
      @NonNull Location location) {
    if (locationOutdated(location)) {
      itemsShown = 0;
      itemsStartIndex = 0;
      Observable<Pair<UserLocation, List<RestaurantContainer>>> network =
          service.geocode(location.getLatitude(), location.getLongitude(), PL)
              .flatMap(new Func1<UserLocation, Observable<Restaurants>>() {
                         @Override public Observable<Restaurants> call(UserLocation geocodedLocation) {
                           return service.getRestaurants(geocodedLocation.getLocation().getCityId(),
                               ENTITY_TYPE_CITY, PL, itemsShown, ITEMS_PAGE_LIMIT);
                         }
                       },
                  new Func2<UserLocation, Restaurants, Pair<UserLocation, List<RestaurantContainer>>>() {
                    @Override public Pair<UserLocation, List<RestaurantContainer>> call(
                        UserLocation geocodedUserLocation, Restaurants restaurantsResponse) {
                      updateOffset(restaurantsResponse.resultsShown,
                          restaurantsResponse.resultsStart);
                      return new Pair<>(geocodedUserLocation, restaurantsResponse.restaurants);
                    }
                  })
              .subscribeOn(Schedulers.io());

      Observable<Pair<UserLocation, List<RestaurantContainer>>> callObservable = network;


    /*  if (cachedItems == null) {
        callObservable = network;
      } else { todo cache
        callObservable = Observable.concat(Observable.just(cachedItems), network)
            .first(new Func1<Observable<Pair<UserLocation, List<RestaurantContainer>>>, Boolean>() {

            });
      }*/

      if (itemsShown == 0) {
        callObservable = callObservable.doOnSubscribe(new Action0() {
          @Override public void call() {
            showProgress();
          }
        });
      }
      callObservable.observeOn(AndroidSchedulers.mainThread())
          .subscribe(new PaginatedListWithLocationSubscriber(location));
    }
  }

  private boolean locationOutdated(Location newLocation) {
    return userLocation == null
        || LocationUtils.distanceBetweenMeters(userLocation.getLocation().getLatitude(),
        userLocation.getLocation().getLongitude(), newLocation.getLatitude(),
        newLocation.getLongitude()) > DISTANCE_DIFF_THRESHOLD_METERS;
  }

  private void updateOffset(int resultsShown, int resultsStart) {
    this.itemsShown += resultsShown;
    this.itemsStartIndex = resultsStart;
    restaurantAdapter.setIsMoreDataAvailable(itemsShown < 100 && itemsStartIndex <= 80);
  }

  @Override public void showItems() {
    AndroidUtils.setVisibilityIfDifferent(recyclerView, View.VISIBLE);
    restaurantAdapter.onDataChanged();
  }

  @Override public void showProgress() {
    recyclerView.setVisibility(View.GONE);
    progressBar.setVisibility(View.VISIBLE);
  }

  @Override public void hideProgress() {
    progressBar.setVisibility(View.GONE);
  }

  @Override public void showItemDetails(RestaurantContainer restaurantContainer) {
    RestaurantDetailsActivity.start(getContext(), restaurantContainer.restaurant);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    restaurantsPresenter.detachView(false);
  }

  public void onNewLocation(Location location) {
    loadForLocation(null, location);
  }

  private class PaginatedListWithLocationSubscriber
      extends Subscriber<Pair<UserLocation, List<RestaurantContainer>>> {

    private final Location location;

    public PaginatedListWithLocationSubscriber(Location location) {
      this.location = location;
    }

    @Override public void onCompleted() {

    }

    @Override public void onError(Throwable error) {
      if (error instanceof HttpException) {
        HttpException httpException = (HttpException) error;
        Timber.w("Paginated List loading error " + httpException.response().errorBody());
      }
      //todo do presentera
      showSnackbar(getActivity(), R.string.download_error, R.string.retry,
          new View.OnClickListener() {
            @Override public void onClick(View v) {
              loadForLocation(null, location);
            }
          });
    }

    @Override
    public void onNext(Pair<UserLocation, List<RestaurantContainer>> userLocationListPair) {
      userLocation = userLocationListPair.first;
      getActivity().setTitle(userLocation.getLocation().getTitle());
      restaurantsPresenter.setItems(userLocationListPair.second);
    }
  }

  private class RestaurantsPageSubscriber extends Subscriber<Restaurants> {

    @Override public void onCompleted() {

    }

    @Override public void onError(Throwable error) {
      if (error instanceof HttpException) {
        HttpException httpException = (HttpException) error;
        Timber.w("Paginated List loading error " + httpException.response().errorBody());
      }
      //todo do presentera
      showSnackbar(getActivity(), R.string.download_error, R.string.retry,
          new View.OnClickListener() {
            @Override public void onClick(View v) {
              loadNext();
            }
          });
    }

    @Override public void onNext(Restaurants restaurants) {
      updateOffset(restaurants.resultsShown, restaurants.resultsStart);
      restaurantsPresenter.appendItems(restaurants.restaurants);
    }
  }
}
