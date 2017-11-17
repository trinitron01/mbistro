package com.bfs.mbistro.model.location;

import android.os.Parcel;
import android.os.Parcelable;
import com.squareup.moshi.Json;
import java.util.List;

public class UserLocationResponse implements Parcelable {

  public static final Parcelable.Creator<UserLocationResponse> CREATOR =
      new Parcelable.Creator<UserLocationResponse>() {
        @Override public UserLocationResponse createFromParcel(Parcel source) {
          return new UserLocationResponse(source);
        }

        @Override public UserLocationResponse[] newArray(int size) {
          return new UserLocationResponse[size];
        }
      };
  @Json(name = "location") private UserLocation location;
  @Json(name = "popularity") private Popularity popularity;
  @Json(name = "link") private String link;
  @Json(name = "nearby_restaurants") private List<NearbyRestaurant> nearbyRestaurants =
      null;

  public UserLocationResponse() {
  }

  protected UserLocationResponse(Parcel in) {
    this.location = in.readParcelable(UserLocation.class.getClassLoader());
    this.popularity = in.readParcelable(Popularity.class.getClassLoader());
    this.link = in.readString();
    this.nearbyRestaurants = in.createTypedArrayList(NearbyRestaurant.CREATOR);
  }

  public UserLocation getLocation() {
    return location;
  }

  public void setLocation(UserLocation location) {
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
