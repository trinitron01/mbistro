package com.bfs.mbistro.base.presenter;

import java.util.List;

public class BaseListItemPresenter<T> {
  private final List<T> items;

  public BaseListItemPresenter(List<T> items) {
    this.items = items;
  }

  public List<T> getItems() {
    return items;
  }
}
