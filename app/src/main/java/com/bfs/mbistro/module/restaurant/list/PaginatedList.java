package com.bfs.mbistro.module.restaurant.list;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

abstract class PaginatedList<I extends Parcelable> implements Parcelable {

  final int resultsShown;
  final int resultsStart;
  final List<I> newItems;
  private final Creator<I> itemCreator = getItemCreator();

  public PaginatedList(int resultsShown, int resultsStart, List<I> newItems) {
    this.resultsShown = resultsShown;
    this.resultsStart = resultsStart;
    this.newItems = newItems;
  }

  protected PaginatedList(Parcel in) {
    resultsShown = in.readInt();
    resultsStart = in.readInt();
    newItems = in.createTypedArrayList(itemCreator);
  }

  protected abstract Creator<I> getItemCreator();

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(resultsShown);
    dest.writeInt(resultsStart);
    dest.writeTypedList(newItems);
  }
}
