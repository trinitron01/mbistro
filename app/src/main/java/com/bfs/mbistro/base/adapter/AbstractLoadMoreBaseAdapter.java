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

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bfs.mbistro.R;
import com.bfs.mbistro.base.presenter.BaseListItemPresenter;
import com.bfs.mbistro.base.view.MvpItemView;
import com.bfs.mbistro.base.view.MvpItemsView;
import java.util.Collection;

/**
 * Base class for an Adapter
 * <p>
 * <p>Adapters provide a binding from an app-specific data set to views that are displayed
 * within a {@link RecyclerView}.</p>
 *
 * @param <T> The type of the elements from the adapter.
 */
public abstract class AbstractLoadMoreBaseAdapter<T, IV extends MvpItemView, V extends MvpItemsView<T>, P extends BaseListItemPresenter<T, IV, V>>
    extends AbstractBaseAdapter<T, IV, V, P> {

  public static final int VIEW_TYPE_ITEM = 0;
  public static final int VIEW_TYPE_LOAD = 1;
  public static final int VIEW_TYPE_EMPTY = -1;

  private static final int INVALID_RESOURCE_ID = -1;
  protected OnChildClickListener<T> mOnChildCLickListener;
  private OnLoadMoreListener mOnLoadMoreListener;
  private int mResLoading;
  private boolean mIsLoading, moreDataAvailable = true;
  /**
   * The constructor of the adapter.
   *
   */
  public AbstractLoadMoreBaseAdapter(P baseListItemPresenter) {
    super(baseListItemPresenter);
    init(INVALID_RESOURCE_ID);
  }

  /**
   * The constructor of the adapter.
   *
   * @param resLoading The loading resource id.
   */
  public AbstractLoadMoreBaseAdapter(int resLoading, P baseListItemPresenter) {
    super(baseListItemPresenter);
    init(resLoading);
  }

  /**
   * Initialization fo the adapter.
   *
   * @param resLoading The loading resource id.
   */
  private void init(int resLoading) {
    mResLoading = resLoading;
  }

  @Override public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    if (viewType == VIEW_TYPE_LOAD && hasLoadingLayout()) {
      return new BaseViewHolder(
          LayoutInflater.from(parent.getContext()).inflate(mResLoading, parent, false));
    } else if (viewType == VIEW_TYPE_EMPTY) {
      return new BaseViewHolder(LayoutInflater.from(parent.getContext())
          .inflate(R.layout.empty_list_view, parent, false));
    } else {
      final BaseViewHolder baseViewHolder = createItemHolder(parent);
      if (mOnChildCLickListener != null) {
        baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            int position = baseViewHolder.getAdapterPosition();
            mOnChildCLickListener.onChildClick(v, getDataSet().get(position), position);
          }
        });
      }
      return baseViewHolder;
    }
  }

  @NonNull protected abstract BaseViewHolder createItemHolder(ViewGroup parent);

  @Override public final void onBindViewHolder(BaseViewHolder holder, int position) {
    if (position >= getItemCount() - 1
        && moreDataAvailable
        && !mIsLoading
        && mOnLoadMoreListener != null) {
      mIsLoading = true;
      mOnLoadMoreListener.onLoadMore();
    }

    if (getItemViewType(position) == VIEW_TYPE_ITEM) {
      //noinspection unchecked
      bindRowDataItem(position, (IV) holder);
    }
  }


  @Override @ViewType public int getItemViewType(int position) {
    if (isDataEmpty()) {
      return VIEW_TYPE_EMPTY;
    } else if (position > getDataSet().size() - 1) {
      return VIEW_TYPE_LOAD;
    } else {
      return VIEW_TYPE_ITEM;
    }
  }

  @Override public final int getItemCount() {
    if (moreDataAvailable && hasLoadingLayout()) {
      return getDataSet().size() + 1;
    } else if (isDataEmpty()) {
      return 1;
    } else {
      return getDataSet().size();
    }
  }

  private boolean isDataEmpty() {
    return getDataSet() == null || getDataSet().isEmpty();
  }

  /**
   * Set on load more listener.
   *
   * @param onLoadMoreListener The listener called when there is more data to load.
   */
  public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
    mOnLoadMoreListener = onLoadMoreListener;
  }

  /**
   * Set the on click listener on list elements.
   *
   * @param listener the listener called when an element is clicked.
   */
  public void setOnChildClickListener(OnChildClickListener<T> listener) {
    this.mOnChildCLickListener = listener;
  }

  /**
   * Set if is more data available.
   *
   * @param moreDataAvailable true if is more available, false otherwise
   */
  public void setIsMoreDataAvailable(boolean moreDataAvailable) {
    this.moreDataAvailable = moreDataAvailable;
  }


  /**
   * <p>Adds an entire data set</p>
   *
   * @param data Collection of data that will be added
   */
  public void addAll(Collection<T> data) {
    if (data.isEmpty()) {
      moreDataAvailable = false;
    }

    getDataSet().addAll(data);
    notifyDataSetChanged();
    mIsLoading = false;
  }

  /**
   * <p>Clears data set and reset loading variables</p>
   */
  @Override public void clear() {
    super.clear();
    moreDataAvailable = true;
  }

  /**
   * Validate if loading layout is present.
   *
   * @return true if is present, false otherwise.
   */
  private boolean hasLoadingLayout() {
    return mResLoading != INVALID_RESOURCE_ID;
  }

  public void onDataChanged() {
    notifyDataSetChanged();
    mIsLoading = false;
  }

  @IntDef({ VIEW_TYPE_ITEM, VIEW_TYPE_LOAD, VIEW_TYPE_EMPTY }) @interface ViewType {

  }
}
