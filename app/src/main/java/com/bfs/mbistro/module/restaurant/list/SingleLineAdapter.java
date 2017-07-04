package com.bfs.mbistro.module.restaurant.list;

import com.bfs.mbistro.base.listener.ItemClickListener;
import com.bfs.mbistro.base.model.NamedItem;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

class SingleLineAdapter<I extends NamedItem> extends RecyclerView.Adapter<SingleLineViewHolder> {

    private final List<I> namedItems = new ArrayList<>();
    private final ItemClickListener<I> listener;

    SingleLineAdapter(ItemClickListener<I> listener) {
        this.listener = listener;
    }

    @Override
    public SingleLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater vi = LayoutInflater.from(parent.getContext());
        View v = vi.inflate(android.R.layout.simple_list_item_1, parent, false);

        return new SingleLineViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SingleLineViewHolder holder, final int position) {
        final I selectedItem = getAtPosition(position);
        holder.tv.setText(selectedItem.getName());
        holder.itemView.setOnClickListener(new ParentListener<>(selectedItem, listener));
    }

    private I getAtPosition(int position) {
        return namedItems.get(position);
    }

    @Override
    public int getItemCount() {
        return namedItems != null ? namedItems.size() : 0;
    }

    public List<I> getNamedItems() {
        return namedItems;
    }

    private static class ParentListener<I> implements View.OnClickListener {

        private final I selectedItem;
        private final ItemClickListener<I> listener;

        public ParentListener(I selectedItem, ItemClickListener<I> listener) {
            this.selectedItem = selectedItem;
            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
            listener.onItemClicked(selectedItem);
        }
    }
}
