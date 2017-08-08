package com.bfs.mbistro.module.restaurant.details.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bfs.mbistro.AndroidUtils;
import com.bfs.mbistro.BistroApp;
import com.bfs.mbistro.Intents;
import com.bfs.mbistro.R;
import com.bfs.mbistro.model.DetailsReviewResponse;
import com.bfs.mbistro.module.restaurant.mvp.RestaurantDetailsContract;
import com.bfs.mbistro.module.restaurant.rating.RestaurantRatingView;
import com.bfs.mbistro.network.ApiService;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.MvpLceViewStateFragment;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.data.ParcelableDataLceViewState;
import javax.inject.Inject;

public class RestaurantDetailsFragment extends
    MvpLceViewStateFragment<TextView, DetailsReviewResponse, RestaurantDetailsContract.RestaurantDetailsView, RestaurantDetailsContract.Presenter>
    implements RestaurantDetailsContract.RestaurantDetailsView, View.OnClickListener,
    Intents.NoAvailableComponentListener {

  private static final String ID_KEY = "ID_KEY";
  private static final String NAME_KEY = "NAME_KEY";
  private static final String DISTANCE_KEY = "DISTANCE_KEY";
  @Inject protected ApiService service;
  private RestaurantDetailsContract.Presenter detailsPresenter;
  private TextView distance;
  private TextView cuisinesLabel;
  private FloatingActionButton navigationFab;
  private RestaurantRatingView ratingView;
  private String restaurantName;
  private DetailsReviewResponse data;

  public static RestaurantDetailsFragment newInstance(@NonNull String restaurantId,
      @NonNull String name, float distance) {
    Bundle args = new Bundle();
    args.putString(ID_KEY, restaurantId);
    args.putString(NAME_KEY, name);
    args.putFloat(DISTANCE_KEY, distance);
    RestaurantDetailsFragment fragment = new RestaurantDetailsFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ((BistroApp) getActivity().getApplication()).component.inject(this);
  }

  @Override public RestaurantDetailsContract.Presenter createPresenter() {
    detailsPresenter = new RestaurantDetailsContract.Presenter(service);
    return detailsPresenter;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_restaurant_details, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    presenter.attachView(this);
    distance = (TextView) view.findViewById(R.id.restaurant_distance);
    cuisinesLabel = (TextView) view.findViewById(R.id.restaurant_cuisines);
    ratingView = (RestaurantRatingView) view.findViewById(R.id.details_rating_view);

    final Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    getAppCompatActivity().setSupportActionBar(toolbar);
    restaurantName = getArguments().getString(NAME_KEY);
    getAppCompatActivity().setTitle(restaurantName);
    navigationFab = (FloatingActionButton) view.findViewById(R.id.navigation_fab);
    navigationFab.setOnClickListener(this);
  }

  public AppCompatActivity getAppCompatActivity() {
    return (AppCompatActivity) getActivity();
  }

  @Override public void onDestroyView() {
    presenter.detachView(false);
    distance = null;
    cuisinesLabel = null;
    navigationFab = null;
    ratingView = null;
    super.onDestroyView();
  }

  @Override public void loadData(boolean pullToRefresh) {
    presenter.loadDetails(getArguments().getString(ID_KEY));
  }

  @Override protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
    return getString(R.string.download_error);
  }

  @Override public DetailsReviewResponse getData() {
    return data;
  }

  @Override public void setData(DetailsReviewResponse data) {
    this.data = data;
    final float distanceInMeters = getArguments().getFloat(DISTANCE_KEY);
    if (distanceInMeters > 0) {
      distance.setText(getString(R.string.distance_subtitle_pattern, distanceInMeters / 1000));
    }
    cuisinesLabel.setText(data.restaurant.getCuisines());
    if (data.restaurant.getLocation() != null) {
      navigationFab.setVisibility(View.VISIBLE);
    }
    ratingView.setRating(data.restaurant.getUserRating());
  }

  @Override public void onClick(View v) {
    Intents.showOnMap(getContext(), data.restaurant.getLocation().getLatitude(),
        data.restaurant.getLocation().getLongitude(), restaurantName, this);
  }

  @Override public void onComponentUnavailable(Intent unsupportedIntent) {
    AndroidUtils.showShortSnackbar(getActivity(), R.string.no_navigation_intent_map_available);
  }

  @NonNull @Override
  public LceViewState<DetailsReviewResponse, RestaurantDetailsContract.RestaurantDetailsView> createViewState() {
    return new ParcelableDataLceViewState<>();
  }
}
