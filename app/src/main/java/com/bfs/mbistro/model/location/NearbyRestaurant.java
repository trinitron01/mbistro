package com.bfs.mbistro.model.location;

import android.os.Parcel;
import android.os.Parcelable;
import com.bfs.mbistro.model.Restaurant;
import com.squareup.moshi.Json;

public class NearbyRestaurant implements Parcelable {

  public static final Parcelable.Creator<NearbyRestaurant> CREATOR =
      new Parcelable.Creator<NearbyRestaurant>() {
        @Override public NearbyRestaurant createFromParcel(Parcel source) {
          return new NearbyRestaurant(source);
        }

        @Override public NearbyRestaurant[] newArray(int size) {
          return new NearbyRestaurant[size];
        }
      };
  @Json(name = "restaurant") private Restaurant restaurant;

  public NearbyRestaurant() {
  }

  protected NearbyRestaurant(Parcel in) {
    this.restaurant = in.readParcelable(Restaurant.class.getClassLoader());
  }

  public Restaurant getRestaurant() {
    return restaurant;
  }

  public void setRestaurant(Restaurant restaurant) {
    this.restaurant = restaurant;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(this.restaurant, flags);
  }
}
