package com.bfs.mbistro.model.location;

import android.os.Parcel;
import android.os.Parcelable;
import com.squareup.moshi.Json;
import java.util.List;

public class Popularity implements Parcelable {

  public static final Parcelable.Creator<Popularity> CREATOR =
      new Parcelable.Creator<Popularity>() {
        @Override public Popularity createFromParcel(Parcel source) {
          return new Popularity(source);
        }

        @Override public Popularity[] newArray(int size) {
          return new Popularity[size];
        }
      };
  @Json(name = "popularity") private String popularity;
  @Json(name = "nightlife_index") private String nightlifeIndex;
  @Json(name = "nearby_res") private List<String> nearbyRes = null;
  @Json(name = "top_cuisines") private List<String> topCuisines = null;
  @Json(name = "popularity_res") private String popularityRes;
  @Json(name = "nightlife_res") private String nightlifeRes;
  @Json(name = "subzone") private String subzone;
  @Json(name = "subzone_id") private Integer subzoneId;
  @Json(name = "city") private String city;

  public Popularity() {
  }

  protected Popularity(Parcel in) {
    this.popularity = in.readString();
    this.nightlifeIndex = in.readString();
    this.nearbyRes = in.createStringArrayList();
    this.topCuisines = in.createStringArrayList();
    this.popularityRes = in.readString();
    this.nightlifeRes = in.readString();
    this.subzone = in.readString();
    this.subzoneId = (Integer) in.readValue(Integer.class.getClassLoader());
    this.city = in.readString();
  }

  public String getPopularity() {
    return popularity;
  }

  public void setPopularity(String popularity) {
    this.popularity = popularity;
  }

  public String getNightlifeIndex() {
    return nightlifeIndex;
  }

  public void setNightlifeIndex(String nightlifeIndex) {
    this.nightlifeIndex = nightlifeIndex;
  }

  public List<String> getNearbyRes() {
    return nearbyRes;
  }

  public void setNearbyRes(List<String> nearbyRes) {
    this.nearbyRes = nearbyRes;
  }

  public List<String> getTopCuisines() {
    return topCuisines;
  }

  public void setTopCuisines(List<String> topCuisines) {
    this.topCuisines = topCuisines;
  }

  public String getPopularityRes() {
    return popularityRes;
  }

  public void setPopularityRes(String popularityRes) {
    this.popularityRes = popularityRes;
  }

  public String getNightlifeRes() {
    return nightlifeRes;
  }

  public void setNightlifeRes(String nightlifeRes) {
    this.nightlifeRes = nightlifeRes;
  }

  public String getSubzone() {
    return subzone;
  }

  public void setSubzone(String subzone) {
    this.subzone = subzone;
  }

  public Integer getSubzoneId() {
    return subzoneId;
  }

  public void setSubzoneId(Integer subzoneId) {
    this.subzoneId = subzoneId;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.popularity);
    dest.writeString(this.nightlifeIndex);
    dest.writeStringList(this.nearbyRes);
    dest.writeStringList(this.topCuisines);
    dest.writeString(this.popularityRes);
    dest.writeString(this.nightlifeRes);
    dest.writeString(this.subzone);
    dest.writeValue(this.subzoneId);
    dest.writeString(this.city);
  }
}
