package com.bfs.mbistro.module.restaurant.list;

import android.view.View;
import android.widget.TextView;
import com.bfs.mbistro.R;
import com.bfs.mbistro.base.adapter.BaseViewHolder;
import com.bfs.mbistro.base.listener.ItemClickListener;
import com.bfs.mbistro.model.RestaurantContainer;

import static com.bfs.mbistro.module.restaurant.mvp.RestaurantsContract.RestaurantRowView;

public class RestaurantViewHolder extends BaseViewHolder
    implements RestaurantRowView, View.OnClickListener {

  private final TextView tv;
  private ItemClickListener<RestaurantContainer> itemClickListener;
  private RestaurantContainer item;

  public RestaurantViewHolder(View itemView,
      ItemClickListener<RestaurantContainer> itemClickListener) {
    super(itemView);
    tv = (TextView) itemView.findViewById(R.id.list_item_first_line);
    if (itemClickListener != null) {
      this.itemClickListener = itemClickListener;
      itemView.setOnClickListener(this);
    }
  }

  @Override public void showRestaurantRow(RestaurantContainer item) {
    this.item = item;
    tv.setText(item.getName());
  }

  @Override public void onClick(View v) {
    itemClickListener.onItemClicked(item);
  }
}
