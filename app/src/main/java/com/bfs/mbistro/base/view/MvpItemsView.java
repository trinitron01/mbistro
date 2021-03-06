package com.bfs.mbistro.base.view;

import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView;

public interface MvpItemsView<T> extends MvpLceView<T> {

  void hideProgress();

  void showItemDetails(T t);

  void showEmptyView();
}
