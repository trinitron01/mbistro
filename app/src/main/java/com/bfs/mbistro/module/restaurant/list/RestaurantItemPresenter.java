package com.bfs.mbistro.module.restaurant.list;

import com.bfs.mbistro.base.presenter.BaseListItemPresenter;
import com.bfs.mbistro.model.RestaurantContainer;
import java.util.List;

class RestaurantItemPresenter extends BaseListItemPresenter<RestaurantContainer> {
  RestaurantItemPresenter(List<RestaurantContainer> items) {
    super(items);
  }

  void onBindRowViewAtPosition(int position, RestaurantRowView view) {
    view.showRestaurantInfo(getItems().get(position));
  }
}
