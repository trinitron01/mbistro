package com.bfs.mbistro.base.presenter;

import com.bfs.mbistro.base.view.MvpItemView;
import com.bfs.mbistro.base.view.MvpItemsView;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import java.util.List;

public abstract class BaseListItemPresenter<T, IV extends MvpItemView, V extends MvpItemsView>
    extends MvpBasePresenter<V> {
  private final List<T> items;

  protected BaseListItemPresenter(List<T> items) {
    this.items = items;
  }

  public List<T> getItems() {
    return items;
  }

  public abstract void onBindRowViewAtPosition(int position, IV view);

  public void appendItems(List<T> items) {
    this.items.addAll(items);
    getView().showItems();
  }
}
