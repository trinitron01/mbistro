package com.bfs.mbistro.model.location;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserLocation_ implements Parcelable {

  public static final Parcelable.Creator<UserLocation_> CREATOR =
      new Parcelable.Creator<UserLocation_>() {
        @Override public UserLocation_ createFromParcel(Parcel source) {
          return new UserLocation_(source);
        }

        @Override public UserLocation_[] newArray(int size) {
          return new UserLocation_[size];
        }
      };
  @SerializedName("entity_type") @Expose private String entityType;
  @SerializedName("entity_id") @Expose private Integer entityId;
  @SerializedName("title") @Expose private String title;
  @SerializedName("latitude") @Expose private double latitude;
  @SerializedName("longitude") @Expose private double longitude;
  @SerializedName("city_id") @Expose private Integer cityId;
  @SerializedName("city_name") @Expose private String cityName;
  @SerializedName("country_id") @Expose private Integer countryId;
  @SerializedName("country_name") @Expose private String countryName;

  public UserLocation_() {
  }

  protected UserLocation_(Parcel in) {
    this.entityType = in.readString();
    this.entityId = (Integer) in.readValue(Integer.class.getClassLoader());
    this.title = in.readString();
    this.latitude = in.readDouble();
    this.longitude = in.readDouble();
    this.cityId = (Integer) in.readValue(Integer.class.getClassLoader());
    this.cityName = in.readString();
    this.countryId = (Integer) in.readValue(Integer.class.getClassLoader());
    this.countryName = in.readString();
  }

  public String getEntityType() {
    return entityType;
  }

  public void setEntityType(String entityType) {
    this.entityType = entityType;
  }

  public Integer getEntityId() {
    return entityId;
  }

  public void setEntityId(Integer entityId) {
    this.entityId = entityId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Integer getCityId() {
    return cityId;
  }

  public void setCityId(Integer cityId) {
    this.cityId = cityId;
  }

  public String getCityName() {
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public Integer getCountryId() {
    return countryId;
  }

  public void setCountryId(Integer countryId) {
    this.countryId = countryId;
  }

  public String getCountryName() {
    return countryName;
  }

  public void setCountryName(String countryName) {
    this.countryName = countryName;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.entityType);
    dest.writeValue(this.entityId);
    dest.writeString(this.title);
    dest.writeDouble(this.latitude);
    dest.writeDouble(this.longitude);
    dest.writeValue(this.cityId);
    dest.writeString(this.cityName);
    dest.writeValue(this.countryId);
    dest.writeString(this.countryName);
  }

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }
}
