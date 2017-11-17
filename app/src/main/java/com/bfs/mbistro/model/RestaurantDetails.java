package com.bfs.mbistro.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.squareup.moshi.Json;

public class RestaurantDetails implements Parcelable {

  public static final Creator<RestaurantDetails> CREATOR = new Creator<RestaurantDetails>() {
    @Override public RestaurantDetails createFromParcel(Parcel in) {
      return new RestaurantDetails(in);
    }

    @Override public RestaurantDetails[] newArray(int size) {
      return new RestaurantDetails[size];
    }
  };

  @Json(name = "id") private String id;
  @Json(name = "name") private String name;
  @Json(name = "url") private String url;
  @Json(name = "location") private RestaurantLocation location;
  @Json(name = "average_cost_for_two") private String averageCostForTwo;
  @Json(name = "price_range") private String priceRange;
  @Json(name = "currency") private String currency;
  @Json(name = "thumb") private String thumb;
  @Json(name = "featured_image") private String featuredImage;
  @Json(name = "photos_url") private String photosUrl;
  @Json(name = "menu_url") private String menuUrl;
  @Json(name = "events_url") private String eventsUrl;
  @Json(name = "user_rating") private UserRating userRating;
  @Json(name = "has_online_delivery") private String hasOnlineDelivery;
  @Json(name = "is_delivering_now") private String isDeliveringNow;
  @Json(name = "has_table_booking") private String hasTableBooking;
  @Json(name = "deeplink") private String deeplink;
  @Json(name = "cuisines") private String cuisines;
  @Json(name = "photo_count") private String photoCount;
  @Json(name = "phone_numbers") private String phoneNumbers;

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

  public RestaurantDetails() {
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
