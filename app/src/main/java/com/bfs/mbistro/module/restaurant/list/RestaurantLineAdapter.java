package com.bfs.mbistro.module.restaurant.list;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bfs.mbistro.R;
import com.bfs.mbistro.base.adapter.AbstractLoadMoreBaseAdapter;
import com.bfs.mbistro.base.adapter.BaseViewHolder;
import com.bfs.mbistro.model.RestaurantContainer;

import static com.bfs.mbistro.module.restaurant.mvp.RestaurantsContract.ItemsView;
import static com.bfs.mbistro.module.restaurant.mvp.RestaurantsContract.Presenter;
import static com.bfs.mbistro.module.restaurant.mvp.RestaurantsContract.RowView;

public class RestaurantLineAdapter extends
    AbstractLoadMoreBaseAdapter<RestaurantContainer, RowView, ItemsView, Presenter> {

  public RestaurantLineAdapter(int resLoading, Presenter restaurantsPresenter) {
    super(resLoading, restaurantsPresenter);
  }

  @NonNull @Override protected BaseViewHolder createItemHolder(ViewGroup parent) {
    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_item_single_line, parent, false);
    return new RestaurantViewHolder(itemView, getItemClickListener());
  }
}
