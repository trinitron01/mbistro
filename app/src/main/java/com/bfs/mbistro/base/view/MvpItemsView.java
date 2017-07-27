package com.bfs.mbistro.base.view;

import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface MvpItemsView<T> extends MvpView {
  void showItems();

  void showProgress();

  void hideProgress();

  void showItemDetails(T t);
}
