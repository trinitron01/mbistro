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

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * This class is an Base ViewHolder for recycler views.
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

  /**
   * Constructor of view holder.
   *
   * @param itemView The layout view for adapter element.
   */
  public BaseViewHolder(View itemView) {
    super(itemView);
  }

  /**
   * Look for a child view with the given id.  If this view has the given
   * id, return this view.
   *
   * @param viewId The id to search for.
   * @param <T> The type of view.
   * @return The view that has the given id in the hierarchy or null
   */
  @SuppressWarnings("unchecked") public <T extends View> T getView(@IdRes int viewId) {
    return (T) itemView.findViewById(viewId);
  }
}
