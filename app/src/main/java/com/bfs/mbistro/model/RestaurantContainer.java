package com.bfs.mbistro.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.bfs.mbistro.model.base.NamedItem;
import com.squareup.moshi.Json;

public class RestaurantContainer implements NamedItem, Parcelable {

  public static final Creator<RestaurantContainer> CREATOR = new Creator<RestaurantContainer>() {
    @Override public RestaurantContainer createFromParcel(Parcel in) {
      return new RestaurantContainer(in);
    }

    @Override public RestaurantContainer[] newArray(int size) {
      return new RestaurantContainer[size];
    }
  };
  @Json(name = "restaurant")

  public Restaurant restaurant;

  public RestaurantContainer() {
  }

  private RestaurantContainer(Parcel in) {
    restaurant = in.readParcelable(Restaurant.class.getClassLoader());
  }

  @Override public String getName() {
    return restaurant.name;
  }

  public Restaurant getRestaurant() {
    return restaurant;
  }

  @Override public String toString() {
    return "RestaurantContainer{" + "restaurant=" + restaurant + '}';
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(restaurant, flags);
  }
}
