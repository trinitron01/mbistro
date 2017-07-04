package com.bfs.mbistro.module.restaurant.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class RestaurantViewHolder extends RecyclerView.ViewHolder {

    final TextView tv;

    public RestaurantViewHolder(View itemView) {
        super(itemView);
        tv = (TextView) itemView.findViewById(android.R.id.text1);
    }

}
