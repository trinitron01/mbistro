package com.bfs.mbistro.module.restaurant.mvp;

import com.bfs.mbistro.base.adapter.OnLoadMoreListener;
import com.bfs.mbistro.base.presenter.BaseListItemPresenter;
import com.bfs.mbistro.base.view.MvpItemView;
import com.bfs.mbistro.base.view.MvpItemsView;
import com.bfs.mbistro.model.RestaurantContainer;
import com.bfs.mbistro.model.location.UserLocationResponse;
import java.util.List;

public interface RestaurantsListContract {

  interface RowView extends MvpItemView {

    void showRestaurantRow(RestaurantContainer item);
  }

  interface ItemsView extends MvpItemsView<RestaurantContainer> {

    void showRestaurantsLocation(UserLocationResponse userLocationResponse);

    void showItemsPageLoadError(Throwable error);

    void setMoreItemsAvailable(boolean moreItemsAvailable);
  }

  abstract class Presenter extends BaseListItemPresenter<RestaurantContainer, RowView, ItemsView>
      implements OnLoadMoreListener {

    protected Presenter(List<RestaurantContainer> items) {
      super(items);
    }

    @Override public void onBindRowViewAtPosition(int position, RowView view) {
      view.showRestaurantRow(getItems().get(position));
    }

    public abstract void loadLocationItems(double latitude, double longitude);
  }
}
