package com.bfs.mbistro.module.restaurant.list;

import android.view.View;
import android.widget.TextView;
import com.bfs.mbistro.base.adapter.BaseViewHolder;
import com.bfs.mbistro.model.RestaurantContainer;

public class RestaurantViewHolder extends BaseViewHolder<RestaurantContainer> {

    final TextView tv;

    public RestaurantViewHolder(View itemView) {
        super(itemView);
        tv = (TextView) itemView.findViewById(android.R.id.text1);
    }

    @Override public void bind(RestaurantContainer item) {
        super.bind(item);
        tv.setText(item.getName());
    }
}
