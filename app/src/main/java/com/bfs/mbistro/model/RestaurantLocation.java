package com.bfs.mbistro.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.squareup.moshi.Json;

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
  @Json(name = "city_id") private Integer cityId;
  @Json(name = "locality_verbose") private String localityVerbose;
  @Json(name = "address") private String address;
  @Json(name = "locality") private String locality;
  @Json(name = "city") private String city;
  @Json(name = "latitude") private double latitude;
  @Json(name = "longitude") private double longitude;
  @Json(name = "zipcode") private String zipcode;
  @Json(name = "country_id") private String countryId;

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

  public String getCity() {
    return city;
  }
}
