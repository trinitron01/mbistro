package com.bfs.mbistro.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RestaurantDetails implements Parcelable {

  public static final Creator<RestaurantDetails> CREATOR = new Creator<RestaurantDetails>() {
    @Override public RestaurantDetails createFromParcel(Parcel in) {
      return new RestaurantDetails(in);
    }

    @Override public RestaurantDetails[] newArray(int size) {
      return new RestaurantDetails[size];
    }
  };

  @SerializedName("id") @Expose private String id;
  @SerializedName("name") @Expose private String name;
  @SerializedName("url") @Expose private String url;
  @SerializedName("location") @Expose private RestaurantLocation location;
  @SerializedName("average_cost_for_two") @Expose private String averageCostForTwo;
  @SerializedName("price_range") @Expose private String priceRange;
  @SerializedName("currency") @Expose private String currency;
  @SerializedName("thumb") @Expose private String thumb;
  @SerializedName("featured_image") @Expose private String featuredImage;
  @SerializedName("photos_url") @Expose private String photosUrl;
  @SerializedName("menu_url") @Expose private String menuUrl;
  @SerializedName("events_url") @Expose private String eventsUrl;
  @SerializedName("user_rating") @Expose private UserRating userRating;
  @SerializedName("has_online_delivery") @Expose private String hasOnlineDelivery;
  @SerializedName("is_delivering_now") @Expose private String isDeliveringNow;
  @SerializedName("has_table_booking") @Expose private String hasTableBooking;
  @SerializedName("deeplink") @Expose private String deeplink;
  @SerializedName("cuisines") @Expose private String cuisines;
  @SerializedName("photo_count") @Expose private String photoCount;
  @SerializedName("phone_numbers") @Expose private String phoneNumbers;

  private RestaurantDetails(Parcel in) {
    id = in.readString();
    name = in.readString();
    url = in.readString();
    location = in.readParcelable(RestaurantLocation.class.getClassLoader());
    averageCostForTwo = in.readString();
    priceRange = in.readString();
    currency = in.readString();
    thumb = in.readString();
    featuredImage = in.readString();
    photosUrl = in.readString();
    menuUrl = in.readString();
    eventsUrl = in.readString();
    userRating = in.readParcelable(UserRating.class.getClassLoader());
    hasOnlineDelivery = in.readString();
    isDeliveringNow = in.readString();
    hasTableBooking = in.readString();
    deeplink = in.readString();
    cuisines = in.readString();
    photoCount = in.readString();
    phoneNumbers = in.readString();
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(id);
    dest.writeString(name);
    dest.writeString(url);
    dest.writeParcelable(location, flags);
    dest.writeString(averageCostForTwo);
    dest.writeString(priceRange);
    dest.writeString(currency);
    dest.writeString(thumb);
    dest.writeString(featuredImage);
    dest.writeString(photosUrl);
    dest.writeString(menuUrl);
    dest.writeString(eventsUrl);
    dest.writeParcelable(userRating, flags);
    dest.writeString(hasOnlineDelivery);
    dest.writeString(isDeliveringNow);
    dest.writeString(hasTableBooking);
    dest.writeString(deeplink);
    dest.writeString(cuisines);
    dest.writeString(photoCount);
    dest.writeString(phoneNumbers);
  }

  @Override public int describeContents() {
    return 0;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public RestaurantLocation getLocation() {
    return location;
  }

  public String getCuisines() {
    return cuisines;
  }

  public UserRating getUserRating() {
    return userRating;
  }
}
