package com.bfs.mbistro.base.presenter;

import com.bfs.mbistro.CollectionUtils;
import com.bfs.mbistro.base.listener.ItemClickListener;
import com.bfs.mbistro.base.view.MvpItemView;
import com.bfs.mbistro.base.view.MvpItemsView;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import java.util.Collection;
import java.util.List;

public abstract class BaseListItemPresenter<T, IV extends MvpItemView, V extends MvpItemsView<T>>
    extends MvpBasePresenter<V> implements ItemClickListener<T> {

  private final List<T> itemsList;

  protected BaseListItemPresenter(List<T> items) {
    this.itemsList = items;
  }

  public List<T> getItems() {
    return itemsList;
  }

  protected final void onNewItems(Collection<T> items, boolean clearExisting) {
    if (clearExisting) {
      this.itemsList.clear();
    }
    this.itemsList.addAll(items);
    getView().hideProgress();
    if (CollectionUtils.isNotNullNorEmpty(this.itemsList)) {
      getView().showContent();
    } else {
      getView().showEmptyView();
    }
  }

  public abstract void onBindRowViewAtPosition(int position, IV view);

  @Override public void onItemClicked(T item) {
    getView().showItemDetails(item);
  }
}
