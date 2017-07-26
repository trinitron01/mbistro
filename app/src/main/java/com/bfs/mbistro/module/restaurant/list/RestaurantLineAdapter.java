package com.bfs.mbistro.module.restaurant.list;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.bfs.mbistro.base.adapter.AbstractLoadMoreBaseAdapter;
import com.bfs.mbistro.model.RestaurantContainer;

public class RestaurantLineAdapter extends
    AbstractLoadMoreBaseAdapter<RestaurantContainer, RestaurantViewHolder, RestaurantItemPresenter> {

  private final RestaurantItemPresenter restaurantItemPresenter;

  public RestaurantLineAdapter(int resLoading, RestaurantItemPresenter restaurantItemPresenter) {
    super(resLoading, restaurantItemPresenter);
    this.restaurantItemPresenter = restaurantItemPresenter;
  }

  @NonNull @Override protected RestaurantViewHolder createItemHolder(ViewGroup parent) {
    return new RestaurantViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(android.R.layout.simple_list_item_1, parent, false));
  }

  @Override protected void bindItem(RestaurantViewHolder holder, int position) {
    restaurantItemPresenter.onBindRowViewAtPosition(position, holder);
  }
}
