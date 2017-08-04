package com.bfs.mbistro.module.restaurant.list;

import android.app.Activity;
import android.location.Location;
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
import com.bfs.mbistro.model.RestaurantContainer;
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

  private static final String USER_LOCATION_KEY = "USER_LOCATION_KEY";
  private static final int DISTANCE_DIFF_THRESHOLD_METERS = 500;
  @Inject protected ApiService service;
  private RestaurantLineAdapter restaurantAdapter;
  private RecyclerView recyclerView;
  private View progressBar;
  private UserLocation userLocation;
  private Location lastLocation;

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
    DividerItemDecoration decor =
        new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
    recyclerView.addItemDecoration(decor);
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

  @Override public void onSaveInstanceState(Bundle outState) {
    if (userLocation != null) {
      outState.putParcelable(USER_LOCATION_KEY, userLocation);
    }
    super.onSaveInstanceState(outState);
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    if (savedInstanceState != null && savedInstanceState.containsKey(USER_LOCATION_KEY)) {
      userLocation = savedInstanceState.getParcelable(USER_LOCATION_KEY);
    }
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

  public void onNewLocation(Location location) {
    this.lastLocation = location;
    loadData(false);
  }

  private boolean shouldGetNewLocation(Location newLocation) {
    return userLocation == null
        || LocationUtils.distanceBetweenMeters(userLocation.getLocation().getLatitude(),
        userLocation.getLocation().getLongitude(), newLocation.getLatitude(),
        newLocation.getLongitude()) > DISTANCE_DIFF_THRESHOLD_METERS;
  }

  @Override public void setData(RestaurantContainer data) {

  }

  @Override public void showContent() {
    super.showContent();
    restaurantAdapter.onDataChanged();
  }

  @Override public void loadData(boolean pullToRefresh) {
    if (shouldGetNewLocation(lastLocation)) {
      getPresenter().loadLocationItems(lastLocation);
    }
  }

  @Override public void showRestaurantsLocation(UserLocation userLocation) {
    this.userLocation = userLocation;
    getActivity().setTitle(userLocation.getLocation().getTitle());
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
