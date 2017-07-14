package com.bfs.mbistro.module.restaurant.list;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.bfs.mbistro.base.adapter.AbstractLoadMoreBaseAdapter;
import com.bfs.mbistro.base.adapter.BaseViewHolder;
import com.bfs.mbistro.model.RestaurantContainer;
import java.util.List;

public class RestaurantLineAdapter extends AbstractLoadMoreBaseAdapter<RestaurantContainer> {

  public RestaurantLineAdapter(int resLoading, List<RestaurantContainer> lstItems) {
    super(resLoading, lstItems);
  }

  @Override
  protected void bindItem(BaseViewHolder<RestaurantContainer> holder, RestaurantContainer item) {
    holder.bind(item);
  }

  @Override
  protected void bindError(BaseViewHolder<RestaurantContainer> holder, boolean loadingError) {

  }

  @NonNull @Override protected BaseViewHolder<RestaurantContainer> createHolder(ViewGroup parent) {
    return new RestaurantViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(android.R.layout.simple_list_item_1, parent, false));
  }


/*    private final List<I> namedItems = new ArrayList<>();
    private final ItemClickListener<I> listener;*/



    /*
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater vi = LayoutInflater.from(parent.getContext());
        View v = vi.inflate(android.R.layout.simple_list_item_1, parent, false);

        return new BaseViewHolder(v);
    }

   @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
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
    }*/
}
