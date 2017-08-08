package com.bfs.mbistro.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RestaurantLocation implements Parcelable {

  public static final Parcelable.Creator<RestaurantLocation> CREATOR =
      new Parcelable.Creator<RestaurantLocation>() {
        @Override public RestaurantLocation createFromParcel(Parcel source) {
          return new RestaurantLocation(source);
        }

        @Override public RestaurantLocation[] newArray(int size) {
          return new RestaurantLocation[size];
        }
      };
  @SerializedName("city_id") @Expose private Integer cityId;
  @SerializedName("locality_verbose") @Expose private String localityVerbose;
  @SerializedName("address") @Expose private String address;
  @SerializedName("locality") @Expose private String locality;
  @SerializedName("city") @Expose private String city;
  @SerializedName("latitude") @Expose private double latitude;
  @SerializedName("longitude") @Expose private double longitude;
  @SerializedName("zipcode") @Expose private String zipcode;
  @SerializedName("country_id") @Expose private String countryId;

  public RestaurantLocation() {
  }

  protected RestaurantLocation(Parcel in) {
    this.cityId = (Integer) in.readValue(Integer.class.getClassLoader());
    this.localityVerbose = in.readString();
    this.address = in.readString();
    this.locality = in.readString();
    this.city = in.readString();
    this.latitude = in.readDouble();
    this.longitude = in.readDouble();
    this.zipcode = in.readString();
    this.countryId = in.readString();
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(this.cityId);
    dest.writeString(this.localityVerbose);
    dest.writeString(this.address);
    dest.writeString(this.locality);
    dest.writeString(this.city);
    dest.writeDouble(this.latitude);
    dest.writeDouble(this.longitude);
    dest.writeString(this.zipcode);
    dest.writeString(this.countryId);
  }

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }
}
