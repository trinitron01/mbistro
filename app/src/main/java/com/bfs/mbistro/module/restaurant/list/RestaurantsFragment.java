package com.bfs.mbistro.module.restaurant.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import com.bfs.mbistro.AndroidUtils;
import com.bfs.mbistro.LocationUtils;
import com.bfs.mbistro.R;
import com.bfs.mbistro.base.view.BaseMvpFragment;
import com.bfs.mbistro.di.BistroComponent;
import com.bfs.mbistro.model.Restaurant;
import com.bfs.mbistro.model.RestaurantContainer;
import com.bfs.mbistro.model.RestaurantLocation;
import com.bfs.mbistro.model.location.UserLocation;
import com.bfs.mbistro.module.restaurant.details.ui.RestaurantDetailsActivity;
import com.bfs.mbistro.module.restaurant.mvp.RestaurantsListContract;
import java.util.ArrayList;

public class RestaurantsFragment extends
    BaseMvpFragment<RecyclerView, RestaurantContainer, RestaurantsListContract.ItemsView, RestaurantsListContract.Presenter>
    implements RestaurantsListContract.ItemsView {

  private RestaurantLineAdapter restaurantAdapter;
  private View progressBar;
  private double currentLatitude;
  private double currentLongitude;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.frame_progress_with_recycler, container, false);
  }

  @Override public RestaurantsListContract.Presenter createPresenter() {
    return new PaginatedRestaurantsPresenter(service, new ArrayList<RestaurantContainer>());
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.contentView);
    int resId = R.anim.layout_animation_fall_down;
    LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), resId);
    recyclerView.setLayoutAnimation(animation);
    progressBar = view.findViewById(R.id.loadingView);
    recyclerView.setHasFixedSize(true);
    recyclerView.addItemDecoration(
        new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    restaurantAdapter = new RestaurantLineAdapter(R.layout.progress_indeterminate, getPresenter());
    restaurantAdapter.setOnLoadMoreListener(getPresenter());
    restaurantAdapter.setIsMoreDataAvailable(false);
    recyclerView.setAdapter(restaurantAdapter);
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

  @Override public void showItemsPageLoadError(Throwable error) {
    AndroidUtils.showSnackbar(getActivity(), networkMonitor.isOnline() ? R.string.download_error
        : R.string.internet_connection_unavailable_error, R.string.retry, new ReloadListener());
  }

  @Override public void setMoreItemsAvailable(boolean moreItemsAvailable) {
    restaurantAdapter.setIsMoreDataAvailable(moreItemsAvailable);
  }

  @Override protected void inject(BistroComponent component) {
    component.inject(this);
  }

  private class ReloadListener implements View.OnClickListener {

    @Override public void onClick(View v) {
      getPresenter().loadNextItems();
    }
  }
}
