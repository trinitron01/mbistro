package com.bfs.mbistro.model.location;

import android.os.Parcel;
import android.os.Parcelable;
import com.squareup.moshi.Json;

public class UserLocation implements Parcelable {

  public static final Parcelable.Creator<UserLocation> CREATOR =
      new Parcelable.Creator<UserLocation>() {
        @Override public UserLocation createFromParcel(Parcel source) {
          return new UserLocation(source);
        }

        @Override public UserLocation[] newArray(int size) {
          return new UserLocation[size];
        }
      };
  @Json(name = "entity_type") private String entityType;
  @Json(name = "entity_id") private Integer entityId;
  @Json(name = "title") private String title;
  @Json(name = "latitude") private double latitude;
  @Json(name = "longitude") private double longitude;
  @Json(name = "city_id") private Integer cityId;
  @Json(name = "city_name") private String cityName;
  @Json(name = "country_id") private Integer countryId;
  @Json(name = "country_name") private String countryName;

  public UserLocation() {
  }

  protected UserLocation(Parcel in) {
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
