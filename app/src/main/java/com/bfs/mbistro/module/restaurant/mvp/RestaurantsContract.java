package com.bfs.mbistro.module.restaurant.mvp;

import com.bfs.mbistro.base.presenter.BaseListItemPresenter;
import com.bfs.mbistro.base.view.MvpItemView;
import com.bfs.mbistro.base.view.MvpItemsView;
import com.bfs.mbistro.model.RestaurantContainer;
import java.util.List;

public class RestaurantsContract {

  public interface RestaurantRowView extends MvpItemView {

    void showRestaurantRow(RestaurantContainer item);
  }

  public interface RestaurantsView extends MvpItemsView {

  }

  public static class RestaurantsPresenter
      extends BaseListItemPresenter<RestaurantContainer, RestaurantRowView, RestaurantsView> {
    public RestaurantsPresenter(List<RestaurantContainer> items) {
      super(items);
    }

    @Override public void onBindRowViewAtPosition(int position, RestaurantRowView view) {
      view.showRestaurantRow(getItems().get(position));
    }
  }
}
