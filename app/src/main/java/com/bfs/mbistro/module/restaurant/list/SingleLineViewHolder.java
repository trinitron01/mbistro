package com.bfs.mbistro.module.restaurant.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

class SingleLineViewHolder extends RecyclerView.ViewHolder {

    final TextView tv;

    public SingleLineViewHolder(View itemView) {
        super(itemView);
        tv = (TextView) itemView.findViewById(android.R.id.text1);
    }

}
