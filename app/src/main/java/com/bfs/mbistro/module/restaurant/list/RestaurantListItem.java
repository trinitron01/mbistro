package com.bfs.mbistro.module.restaurant.list;

import com.bfs.mbistro.R;
import com.mikepenz.fastadapter.items.AbstractItem;

import android.view.View;

import java.util.List;

public class RestaurantListItem extends AbstractItem<RestaurantListItem, RestaurantViewHolder> {

    final RestaurantContainer restaurant;

    public RestaurantListItem(RestaurantContainer restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public RestaurantViewHolder getViewHolder(View v) {
        return new RestaurantViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.restaurant_item;
    }

    @Override
    public int getLayoutRes() {
        return android.R.layout.simple_list_item_1;
    }

    @Override
    public void bindView(RestaurantViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);
        holder.tv.setText(restaurant.getName());
    }
}
