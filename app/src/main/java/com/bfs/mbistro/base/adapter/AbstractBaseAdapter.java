/*
 * ADAL - A set of Android libraries to help speed up Android development.
 *
 * Copyright (c) 2017 ADAL
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.bfs.mbistro.base.adapter;

import android.support.v7.widget.RecyclerView;
import com.bfs.mbistro.base.listener.ItemClickListener;
import com.bfs.mbistro.base.presenter.BaseListItemPresenter;
import com.bfs.mbistro.base.view.MvpItemView;
import com.bfs.mbistro.base.view.MvpItemsView;
import java.util.Collection;
import java.util.List;

/**
 * Base class for an Adapter
 *
 * @param <T> The type of the elements from the adapter.
 */
public abstract class AbstractBaseAdapter<T, IV extends MvpItemView, V extends MvpItemsView<T>, P extends BaseListItemPresenter<T, IV, V>>
    extends RecyclerView.Adapter<BaseViewHolder> {

  private final P itemPresenter;

  protected AbstractBaseAdapter(P itemPresenter) {
    this.itemPresenter = itemPresenter;
  }

  /**
   * <p>Adds an item on a specific position of the data set</p>
   *
   * @param position specific position to add the item
   * @param item Item to be added
   */
  public void add(int position, T item) {
    validatePosition(position);
    itemPresenter.getItems().add(position, item);
    notifyItemInserted(position);
  }

  final void bindRowDataItem(int position, IV itemView) {
    itemPresenter.onBindRowViewAtPosition(position, itemView);
  }

  protected final ItemClickListener<T> getItemClickListener() {
    return itemPresenter;
  }

  /**
   * <p>Adds an item into data set</p>
   *
   * @param item Item to be added
   */
  public void add(T item) {
    itemPresenter.getItems().add(item);
    notifyItemInserted(getItemPosition(item));
  }

  /**
   * <p>Returns the item on the given position</p>
   *
   * @param position Position of the retrieved item
   * @return item: The item at the specific position
   */
  public T getItem(int position) {
    validatePosition(position);
    return itemPresenter.getItems().get(position);
  }

  /**
   * <p>Removes the item at the given position</p>
   *
   * @param position Position of the item that will be removed
   */
  public void remove(int position) {
    validatePosition(position);
    itemPresenter.getItems().remove(position);
    notifyItemRemoved(position);
  }

  /**
   * <p>Removes the given item</p>
   *
   * @param item The item that will be removed
   */
  public void remove(T item) {
    itemPresenter.getItems().remove(item);
    notifyItemRemoved(getItemPosition(item));
  }

  /**
   * <p>Remove a collection of items</p>
   *
   * @param ts Collection of items that will be removed
   */
  public void removeAll(Collection<T> ts) {
    itemPresenter.getItems().removeAll(ts);
    notifyDataSetChanged();
  }

  /**
   * <p>Returns the given item position</p>
   *
   * @param item Item to retrieve the position
   * @return position: Position of the specified item
   */
  public int getItemPosition(T item) {
    return itemPresenter.getItems().indexOf(item);
  }

  /**
   * <p>Check if data set contains the specified item</p>
   *
   * @param item Item to verify if contains on data set
   * @return true if contains otherwise false
   */
  public boolean containsItem(T item) {
    return itemPresenter.getItems().contains(item);
  }

  /**
   * <p>Returns adapter data set (All items)</p>
   *
   * @return data: All items stored on this list
   */
  public List<T> getDataSet() {
    return itemPresenter.getItems();
  }

  /**
   * <p>Clears data set and reset loading variables</p>
   */
  public void clear() {
    itemPresenter.getItems().clear();
    notifyDataSetChanged();
  }

  /**
   * <p>Validates an specific position according to the data set size</p>
   *
   * @param position Specific position to be validated
   */
  private void validatePosition(int position) {
    if (itemPresenter.getItems().isEmpty()) {
      throw new IndexOutOfBoundsException("The adapter is empty!");
    }

    if (position < 0 || position >= itemPresenter.getItems().size()) {
      throw new IndexOutOfBoundsException(
          "Please, specify a valid position that is equals or greater than 0 and less than "
              + itemPresenter.getItems().size());
    }
  }
}
