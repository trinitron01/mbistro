package com.bfs.mbistro.module.restaurant.list;

import android.view.View;
import android.widget.TextView;
import com.bfs.mbistro.base.adapter.BaseViewHolder;
import com.bfs.mbistro.model.RestaurantContainer;

import static com.bfs.mbistro.module.restaurant.mvp.RestaurantsContract.RestaurantRowView;

public class RestaurantViewHolder extends BaseViewHolder implements RestaurantRowView {

  private final TextView tv;

  public RestaurantViewHolder(View itemView) {
    super(itemView);
    tv = (TextView) itemView.findViewById(android.R.id.text1);
  }

  @Override public void showRestaurantRow(RestaurantContainer item) {
    tv.setText(item.getName());
  }
}
