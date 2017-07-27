package com.bfs.mbistro.module.restaurant.mvp;

import com.bfs.mbistro.base.presenter.BaseListItemPresenter;
import com.bfs.mbistro.base.view.MvpItemView;
import com.bfs.mbistro.base.view.MvpItemsView;
import com.bfs.mbistro.model.RestaurantContainer;
import java.util.List;

public interface RestaurantsContract {

  interface RowView extends MvpItemView {

    void showRestaurantRow(RestaurantContainer item);
  }

  interface ItemsView extends MvpItemsView<RestaurantContainer> {

  }

  class Presenter extends BaseListItemPresenter<RestaurantContainer, RowView, ItemsView> {

    public Presenter(List<RestaurantContainer> items) {
      super(items);
    }

    @Override public void onBindRowViewAtPosition(int position, RowView view) {
      view.showRestaurantRow(getItems().get(position));
    }
  }
}
