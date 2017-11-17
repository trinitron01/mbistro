package com.bfs.mbistro.restaurant;

import com.bfs.mbistro.RxSchedulersOverrideRule;
import com.bfs.mbistro.model.RestaurantContainer;
import com.bfs.mbistro.model.Restaurants;
import com.bfs.mbistro.model.location.UserLocation;
import com.bfs.mbistro.model.location.UserLocationResponse;
import com.bfs.mbistro.module.restaurant.list.PaginatedRestaurantsPresenter;
import com.bfs.mbistro.module.restaurant.mvp.RestaurantsListContract;
import com.bfs.mbistro.network.ApiService;
import io.reactivex.Observable;
import java.util.ArrayList;
import java.util.Collections;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class) public class RestaurantListTest {

  @Rule public final RxSchedulersOverrideRule overrideSchedulersRule =
      new RxSchedulersOverrideRule();
  @Mock private ApiService apiService;
  @Mock private RestaurantsListContract.ItemsView itemsView;

  @Test public void testLoadedListShowsLoadingAndContent() {
    Restaurants restaurantsResponse = new Restaurants();
    UserLocationResponse item = new UserLocationResponse();
    UserLocation userLocation = new UserLocation();
    userLocation.setCityId(1);
    item.setLocation(userLocation);
    restaurantsResponse.restaurants = new ArrayList<>();
    restaurantsResponse.restaurants.add(new RestaurantContainer());
    restaurantsResponse.restaurants.add(new RestaurantContainer());
    restaurantsResponse.restaurants.add(new RestaurantContainer());

    when(apiService.getRestaurants(anyInt(), anyString(), anyInt(), anyInt())).thenReturn(
        Observable.just(restaurantsResponse));
    when(apiService.geocode(anyDouble(), anyDouble())).thenReturn(Observable.just(item));

    PaginatedRestaurantsPresenter restaurantsPresenter =
        new PaginatedRestaurantsPresenter(apiService, new ArrayList<>());

    restaurantsPresenter.attachView(itemsView);
    restaurantsPresenter.loadLocationItems(1, 1);

    verify(itemsView, never()).showError(any(), anyBoolean());
    verify(itemsView, times(1)).showLoading(false);
    verify(itemsView, times(1)).showContent();
  }

  @Test public void testNonEmptyListIsSame() {
    Restaurants restaurantsResponse = new Restaurants();
    UserLocationResponse item = new UserLocationResponse();
    UserLocation userLocation = new UserLocation();
    userLocation.setCityId(1);
    item.setLocation(userLocation);
    restaurantsResponse.restaurants = new ArrayList<>();
    restaurantsResponse.restaurants.add(new RestaurantContainer());
    restaurantsResponse.restaurants.add(new RestaurantContainer());
    restaurantsResponse.restaurants.add(new RestaurantContainer());

    when(apiService.getRestaurants(anyInt(), anyString(), anyInt(), anyInt())).thenReturn(
        Observable.just(restaurantsResponse));
    when(apiService.geocode(anyDouble(), anyDouble())).thenReturn(Observable.just(item));

    PaginatedRestaurantsPresenter restaurantsPresenter =
        new PaginatedRestaurantsPresenter(apiService, new ArrayList<>());

    restaurantsPresenter.attachView(itemsView);
    restaurantsPresenter.loadLocationItems(1, 1);

    assertEquals(restaurantsPresenter.getItems(), restaurantsResponse.restaurants);
  }

  @Test public void testEmptyListShowsEmptyView() {
    Restaurants restaurantsResponse = new Restaurants();
    UserLocationResponse locationResponse = new UserLocationResponse();
    UserLocation userLocation = new UserLocation();
    userLocation.setCityId(1);
    locationResponse.setLocation(userLocation);
    restaurantsResponse.restaurants = Collections.emptyList();

    when(apiService.getRestaurants(anyInt(), anyString(), anyInt(), anyInt())).thenReturn(
        Observable.just(restaurantsResponse));
    when(apiService.geocode(anyDouble(), anyDouble())).thenReturn(
        Observable.just(locationResponse));

    PaginatedRestaurantsPresenter restaurantsPresenter =
        new PaginatedRestaurantsPresenter(apiService, new ArrayList<>());

    restaurantsPresenter.attachView(itemsView);
    restaurantsPresenter.loadLocationItems(1, 1);

    assertEquals(restaurantsPresenter.getItems(), restaurantsResponse.restaurants);

    verify(itemsView, never()).showError(any(), anyBoolean());
    verify(itemsView, times(1)).showLoading(false);
    verify(itemsView, times(1)).showEmptyView();
  }
}
