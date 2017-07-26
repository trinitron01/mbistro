package com.bfs.mbistro.module.restaurant.list;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.bfs.mbistro.base.adapter.AbstractLoadMoreBaseAdapter;
import com.bfs.mbistro.base.adapter.BaseViewHolder;
import com.bfs.mbistro.model.RestaurantContainer;

public class RestaurantLineAdapter extends
    AbstractLoadMoreBaseAdapter<RestaurantContainer, RestaurantRowView, RestaurantsView, RestaurantsPresenter> {

  public RestaurantLineAdapter(int resLoading, RestaurantsPresenter restaurantsPresenter) {
    super(resLoading, restaurantsPresenter);
  }

  @NonNull @Override protected BaseViewHolder createItemHolder(ViewGroup parent) {
    return new RestaurantViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(android.R.layout.simple_list_item_1, parent, false));
  }

}
