package com.bfs.mbistro.module.restaurant.list;

import com.bfs.mbistro.base.presenter.BaseListItemPresenter;
import com.bfs.mbistro.model.RestaurantContainer;
import java.util.List;

class RestaurantsPresenter
    extends BaseListItemPresenter<RestaurantContainer, RestaurantRowView, RestaurantsView> {
  RestaurantsPresenter(List<RestaurantContainer> items) {
    super(items);
  }

  @Override public void onBindRowViewAtPosition(int position, RestaurantRowView view) {
    view.showRestaurantRow(getItems().get(position));
  }


}
