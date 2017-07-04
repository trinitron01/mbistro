package com.bfs.mbistro.module.restaurant.list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.bfs.mbistro.base.model.NamedItem;

import android.os.Parcel;
import android.os.Parcelable;

public class RestaurantContainer implements NamedItem, Parcelable {

    @SerializedName("restaurant")
    @Expose
    public Restaurant restaurant;

    protected RestaurantContainer(Parcel in) {
        restaurant = in.readParcelable(Restaurant.class.getClassLoader());
    }

    @Override
    public String getName() {
        return restaurant.name;
    }

    public String getId() {
        return restaurant.id;
    }

    @Override
    public String toString() {
        return "RestaurantContainer{" + "restaurant=" + restaurant + '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(restaurant, flags);
    }

    public static final Creator<RestaurantContainer> CREATOR = new Creator<RestaurantContainer>() {
        @Override
        public RestaurantContainer createFromParcel(Parcel in) {
            return new RestaurantContainer(in);
        }

        @Override
        public RestaurantContainer[] newArray(int size) {
            return new RestaurantContainer[size];
        }
    };
}
