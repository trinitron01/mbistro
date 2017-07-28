package com.bfs.mbistro.model.location;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

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
  @SerializedName("location") @Expose private UserLocation_ location;
  @SerializedName("popularity") @Expose private Popularity popularity;
  @SerializedName("link") @Expose private String link;
  @SerializedName("nearby_restaurants") @Expose private List<NearbyRestaurant> nearbyRestaurants =
      null;

  public UserLocation() {
  }

  protected UserLocation(Parcel in) {
    this.location = in.readParcelable(UserLocation_.class.getClassLoader());
    this.popularity = in.readParcelable(Popularity.class.getClassLoader());
    this.link = in.readString();
    this.nearbyRestaurants = in.createTypedArrayList(NearbyRestaurant.CREATOR);
  }

  public UserLocation_ getLocation() {
    return location;
  }

  public void setLocation(UserLocation_ location) {
    this.location = location;
  }

  public Popularity getPopularity() {
    return popularity;
  }

  public void setPopularity(Popularity popularity) {
    this.popularity = popularity;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public List<NearbyRestaurant> getNearbyRestaurants() {
    return nearbyRestaurants;
  }

  public void setNearbyRestaurants(List<NearbyRestaurant> nearbyRestaurants) {
    this.nearbyRestaurants = nearbyRestaurants;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(this.location, flags);
    dest.writeParcelable(this.popularity, flags);
    dest.writeString(this.link);
    dest.writeTypedList(this.nearbyRestaurants);
  }
}
