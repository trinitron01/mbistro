package com.bfs.mbistro.module.restaurant.list;

import com.bfs.mbistro.model.RestaurantContainer;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface RestaurantRowView extends MvpView {
  void showRestaurantInfo(RestaurantContainer item);
}
