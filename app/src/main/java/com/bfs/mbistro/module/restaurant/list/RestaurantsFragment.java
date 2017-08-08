package com.bfs.mbistro.module.restaurant.list;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import com.bfs.mbistro.AndroidUtils;
import com.bfs.mbistro.BistroApp;
import com.bfs.mbistro.LocationUtils;
import com.bfs.mbistro.R;
import com.bfs.mbistro.model.Restaurant;
import com.bfs.mbistro.model.RestaurantContainer;
import com.bfs.mbistro.model.RestaurantLocation;
import com.bfs.mbistro.model.location.UserLocation;
import com.bfs.mbistro.module.restaurant.details.ui.RestaurantDetailsActivity;
import com.bfs.mbistro.module.restaurant.mvp.RestaurantsContract;
import com.bfs.mbistro.network.ApiService;
import com.hannesdorfmann.mosby3.mvp.lce.MvpLceFragment;
import java.util.ArrayList;
import javax.inject.Inject;

public class RestaurantsFragment extends
    MvpLceFragment<RecyclerView, RestaurantContainer, RestaurantsContract.ItemsView, RestaurantsContract.Presenter>
    implements RestaurantsContract.ItemsView {

  @Inject protected ApiService service;
  private RestaurantLineAdapter restaurantAdapter;
  private RecyclerView recyclerView;
  private View progressBar;
  private double currentLatitude;
  private double currentLongitude;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.frame_progress_with_recycler, container, false);
  }

  @Override public RestaurantsContract.Presenter createPresenter() {
    return new PaginatedRestaurantsPresenter(service, new ArrayList<RestaurantContainer>());
  }

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    ((BistroApp) activity.getApplication()).component.inject(this);//todo inject w presenterze
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    recyclerView = (RecyclerView) view.findViewById(R.id.contentView);
    int resId = R.anim.layout_animation_fall_down;
    LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), resId);
    recyclerView.setLayoutAnimation(animation);
    progressBar = view.findViewById(R.id.loadingView);
    recyclerView.setLayoutManager(
        new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    recyclerView.setHasFixedSize(true);
    recyclerView.addItemDecoration(
        new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    restaurantAdapter = new RestaurantLineAdapter(R.layout.progress_indeterminate, getPresenter());
    restaurantAdapter.setOnLoadMoreListener(getPresenter());
    restaurantAdapter.setIsMoreDataAvailable(false);
    recyclerView.setAdapter(restaurantAdapter);
  }

  @NonNull @Override protected TextView createErrorView(View view) {
    return super.createErrorView(view);
  }

  @Override protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
    return null;//todo test
  }

  @Override public void showProgress() {
    recyclerView.setVisibility(View.GONE);
    progressBar.setVisibility(View.VISIBLE);
  }

  @Override public void hideProgress() {
    progressBar.setVisibility(View.GONE);
  }

  @Override public void showItemDetails(RestaurantContainer restaurantContainer) {
    Restaurant restaurant = restaurantContainer.restaurant;
    RestaurantLocation location = restaurant.location;
    int distanceMeters = LocationUtils.distanceBetweenMeters(currentLatitude, currentLongitude,
        location.getLatitude(), location.getLongitude());
    RestaurantDetailsActivity.start(getContext(), restaurant, distanceMeters);
  }

  public void onNewLocation(double latitude, double longitude) {
    currentLatitude = latitude;
    currentLongitude = longitude;
    loadData(false);
  }

  @Override public void setData(RestaurantContainer data) {

  }

  @Override public void showContent() {
    super.showContent();
    restaurantAdapter.onDataChanged();
  }

  @Override public void loadData(boolean pullToRefresh) {
    getPresenter().loadLocationItems(currentLatitude, currentLongitude);
  }

  @Override public void showRestaurantsLocation(UserLocation location) {
    getActivity().setTitle(location.getLocation().getTitle());
  }

  @Override public void showItemsLoadError(Throwable error) {
    AndroidUtils.showSnackbar(getActivity(), R.string.download_error, R.string.retry,
        new View.OnClickListener() {
          @Override public void onClick(View v) {
            loadData(false);
          }
        });
  }

  @Override public void showItemsPageLoadError(Throwable error) {
    AndroidUtils.showSnackbar(getActivity(), R.string.download_error, R.string.retry,
        new View.OnClickListener() {
          @Override public void onClick(View v) {
            getPresenter().loadNextItems();
          }
        });
  }

  @Override public void setMoreItemsAvailable(boolean moreItemsAvailable) {
    restaurantAdapter.setIsMoreDataAvailable(moreItemsAvailable);
  }
}
