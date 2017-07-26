package com.bfs.mbistro.module.restaurant.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bfs.mbistro.BistroApp;
import com.bfs.mbistro.CollectionUtils;
import com.bfs.mbistro.R;
import com.bfs.mbistro.base.adapter.OnLoadMoreListener;
import com.bfs.mbistro.model.RestaurantContainer;
import com.bfs.mbistro.model.Restaurants;
import com.bfs.mbistro.module.restaurant.details.ui.DetailsActivity;
import com.bfs.mbistro.module.restaurant.mvp.RestaurantsContract;
import com.bfs.mbistro.network.ApiService;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.bfs.mbistro.AndroidUtils.showSnackbar;

public class RestaurantsFragment extends Fragment
    implements OnLoadMoreListener, RestaurantsContract.RestaurantsView {

  private static final String PL = "pl";
  private static final String LIST_KEY = "LIST_KEY";
  @Inject protected ApiService service;
  private RestaurantLineAdapter restaurantAdapter;
  private int itemsShown;
  private int itemsStartIndex;
  private PaginatedList<RestaurantContainer> paginatedList;
  private RestaurantsContract.RestaurantsPresenter restaurantsPresenter;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.restaurants_list, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
    recyclerView.setLayoutManager(
        new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    recyclerView.setHasFixedSize(true);
    restaurantsPresenter =
        new RestaurantsContract.RestaurantsPresenter(new ArrayList<RestaurantContainer>());
    restaurantsPresenter.attachView(this);
    restaurantAdapter =
        new RestaurantLineAdapter(R.layout.progress_indeterminate, restaurantsPresenter);
    restaurantAdapter.setOnLoadMoreListener(this);
    restaurantAdapter.setIsMoreDataAvailable(false);
    recyclerView.setAdapter(restaurantAdapter);
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    List<RestaurantContainer> restaurants = restaurantAdapter.getDataSet();
    if (CollectionUtils.isNotNullNorEmpty(restaurants)) {
      paginatedList =
          new RestaurantContainerPaginatedList(restaurants, itemsShown, itemsStartIndex);
      outState.putParcelable(LIST_KEY, paginatedList);
    }
    super.onSaveInstanceState(outState);
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    ((BistroApp) getActivity().getApplication()).component.inject(this);
    List<RestaurantContainer> items = null;
    if (savedInstanceState != null) {
      paginatedList = savedInstanceState.getParcelable(LIST_KEY);
      updateOffset(paginatedList.resultsShown, paginatedList.resultsStart);
      items = paginatedList.newItems;
    }
    load(items);
  }

  @Override public void onLoadMore() {
    load(null);
  }

  private void load(List<RestaurantContainer> cachedItems) {
    Observable<List<RestaurantContainer>> network =
        service.getRestaurants(264, "city", PL, itemsShown, 20)
            .subscribeOn(Schedulers.io())
            .map(new Func1<Restaurants, List<RestaurantContainer>>() {
              @Override public List<RestaurantContainer> call(Restaurants restaurantsResponse) {
                updateOffset(restaurantsResponse.resultsShown, restaurantsResponse.resultsStart);
                return restaurantsResponse.restaurants;
              }
            });

    Observable<List<RestaurantContainer>> callObservable;
    if (cachedItems != null) {
      callObservable = Observable.concat(Observable.just(cachedItems), network)
          .first(new Func1<List<RestaurantContainer>, Boolean>() {
            @Override public Boolean call(List<RestaurantContainer> restaurantListItems) {
              return restaurantListItems != null;
            }
          });
    } else {
      callObservable = network;
    }
    callObservable.observeOn(AndroidSchedulers.mainThread())
        .subscribe(new PaginatedListSingleSubscriber());
  }

  private void updateOffset(int resultsShown, int resultsStart) {
    this.itemsShown += resultsShown;
    this.itemsStartIndex = resultsStart;
    restaurantAdapter.setIsMoreDataAvailable(itemsShown < 100 && itemsStartIndex <= 80);
  }

  @Override public void showItems() {
    restaurantAdapter.onDataChanged();
  }

  @Override public void showItemDetails(RestaurantContainer restaurantContainer) {
    DetailsActivity.start(getContext(), restaurantContainer.restaurant);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    restaurantsPresenter.detachView(false);
  }

  private class PaginatedListSingleSubscriber extends Subscriber<List<RestaurantContainer>> {

    @Override public void onCompleted() {

    }

    @Override public void onError(Throwable error) {
      showSnackbar(getActivity(), R.string.download_error, R.string.retry,
          new View.OnClickListener() {
            @Override public void onClick(View v) {
              load(null);
            }
          });
    }

    @Override public void onNext(List<RestaurantContainer> restaurantListItems) {
      restaurantsPresenter.appendItems(restaurantListItems);
    }
  }
}
