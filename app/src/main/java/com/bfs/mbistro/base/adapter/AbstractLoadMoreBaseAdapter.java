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
import java.util.Collection;
import java.util.List;

/**
 * Base class for an Adapter
 * <p>
 * <p>Adapters provide a binding from an app-specific data set to views that are displayed
 * within a {@link RecyclerView}.</p>
 *
 * @param <T> The type of the elements from the adapter.
 */
public abstract class AbstractLoadMoreBaseAdapter<T> extends AbstractBaseAdapter<T> {

  public static final int VIEW_TYPE_ITEM = 0;
  public static final int VIEW_TYPE_LOAD = 1;
  public static final int VIEW_TYPE_EMPTY = -1;

  private static final int INVALID_RESOURCE_ID = -1;
  protected OnChildClickListener<T> mOnChildCLickListener;
  private OnLoadMoreListener mOnLoadMoreListener;
  private int mResLoading;
  private boolean mIsLoading, moreDataAvailable = true, mIsLoadingError;

  /**
   * The constructor of the adapter.
   *
   * @param lstItems The list of items.
   */
  public AbstractLoadMoreBaseAdapter(List<T> lstItems) {
    super();
    init(INVALID_RESOURCE_ID, lstItems);
  }

  /**
   * The constructor of the adapter.
   *
   * @param resLoading The loading resource id.
   * @param lstItems The list of items.
   */
  public AbstractLoadMoreBaseAdapter(int resLoading, List<T> lstItems) {
    super();
    init(resLoading, lstItems);
  }

  /**
   * Initialization fo the adapter.
   *
   * @param resLoading The loading resource id.
   * @param data The list of items.
   */
  private void init(int resLoading, List<T> data) {
    mResLoading = resLoading;
    mData = data;
  }

  @Override public BaseViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {

    if (viewType == VIEW_TYPE_LOAD && hasLoadingLayout()) {
      return new BaseViewHolder<>(
          LayoutInflater.from(parent.getContext()).inflate(mResLoading, parent, false));
    } else if (viewType == VIEW_TYPE_EMPTY) {
      return new BaseViewHolder<>(LayoutInflater.from(parent.getContext())
          .inflate(R.layout.empty_list_view, parent, false));
    } else {
      final BaseViewHolder<T> baseViewHolder = createHolder(parent);
      if (mOnChildCLickListener != null) {
        baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            int position = baseViewHolder.getAdapterPosition();
            mOnChildCLickListener.onChildClick(v, mData.get(position), position);
          }
        });
      }
      return baseViewHolder;
    }
  }

  @NonNull protected abstract BaseViewHolder<T> createHolder(ViewGroup parent);

  @Override public void onBindViewHolder(BaseViewHolder<T> holder, int position) {
    if (position >= getItemCount() - 1
        && moreDataAvailable
        && !mIsLoading
        && mOnLoadMoreListener != null) {
      mIsLoading = true;
      mOnLoadMoreListener.onLoadMore();
    }

    if (getItemViewType(position) == VIEW_TYPE_ITEM) {
      bindItem(holder, getItem(position));
    } else if (getItemViewType(position) == VIEW_TYPE_LOAD && hasLoadingLayout()) {
      bindError(holder, mIsLoadingError);
    }
  }

  @Override @ViewType public int getItemViewType(int position) {
    if (isDataEmpty()) {
      return VIEW_TYPE_EMPTY;
    } else if (position > mData.size() - 1) {
      return VIEW_TYPE_LOAD;
    } else {
      return VIEW_TYPE_ITEM;
    }
  }

  @Override public final int getItemCount() {
    if (moreDataAvailable && hasLoadingLayout()) {
      return mData.size() + 1;
    } else if (isDataEmpty()) {
      return 1;
    } else {
      return mData.size();
    }
  }

  private boolean isDataEmpty() {
    return mData == null || mData.isEmpty();
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
   * Set if appended an error during loading.
   *
   * @param isError true if there was an error, false otherwise.
   */
  public void setLoadingError(boolean isError) {
    mIsLoadingError = isError;
    notifyDataSetChanged();
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

    mData.addAll(data);
    notifyDataSetChanged();
    mIsLoading = false;
  }

  /**
   * <p>Clears data set and reset loading variables</p>
   */
  @Override public void clear() {
    super.clear();
    moreDataAvailable = true;
    mIsLoadingError = false;
  }

  /**
   * Validate if loading layout is present.
   *
   * @return true if is present, false otherwise.
   */
  private boolean hasLoadingLayout() {
    return mResLoading != INVALID_RESOURCE_ID;
  }

  @IntDef({ VIEW_TYPE_ITEM, VIEW_TYPE_LOAD, VIEW_TYPE_EMPTY }) @interface ViewType {

  }
}
