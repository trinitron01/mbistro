package com.bfs.mbistro.module.restaurant.details.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bfs.mbistro.BistroApp;
import com.bfs.mbistro.R;
import com.bfs.mbistro.model.RestaurantDetails;
import com.bfs.mbistro.module.restaurant.mvp.RestaurantDetailsContract;
import com.bfs.mbistro.network.ApiService;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.MvpLceViewStateFragment;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.data.ParcelableDataLceViewState;
import javax.inject.Inject;

public class RestaurantDetailsFragment extends
    MvpLceViewStateFragment<TextView, RestaurantDetails, RestaurantDetailsContract.RestaurantDetailsView, RestaurantDetailsContract.RestaurantDetailsPresenter>
    implements RestaurantDetailsContract.RestaurantDetailsView {

  private static final String ID_KEY = "ID_KEY";
  @Inject protected ApiService service;
  private RestaurantDetailsContract.RestaurantDetailsPresenter detailsPresenter;
  private TextView content;
  private RestaurantDetails restaurant;

  public static RestaurantDetailsFragment newInstance(String restaurantId) {
    Bundle args = new Bundle();
    args.putString(ID_KEY, restaurantId);
    RestaurantDetailsFragment fragment = new RestaurantDetailsFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ((BistroApp) getActivity().getApplication()).component.inject(this);
  }

  @Override public RestaurantDetailsContract.RestaurantDetailsPresenter createPresenter() {
    detailsPresenter = new RestaurantDetailsContract.RestaurantDetailsPresenter(service);
    return detailsPresenter;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.content_details, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    presenter.attachView(this);
    content = (TextView) view.findViewById(R.id.contentView);
  }

  @Override public void onDestroyView() {
    presenter.detachView(false);
    content = null;
    super.onDestroyView();
  }

  @Override public void loadData(boolean pullToRefresh) {
    presenter.loadDetails(getArguments().getString(ID_KEY));
  }

  @Override protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
    return getString(R.string.download_error);
  }

  @Override public RestaurantDetails getData() {
    return restaurant;
  }

  @Override public void setData(RestaurantDetails restaurant) {
    this.restaurant = restaurant;
    content.setText(restaurant.toString());
    getActivity().setTitle(restaurant.getName());
  }

  @NonNull @Override
  public LceViewState<RestaurantDetails, RestaurantDetailsContract.RestaurantDetailsView> createViewState() {
    return new ParcelableDataLceViewState<>();
  }
}
