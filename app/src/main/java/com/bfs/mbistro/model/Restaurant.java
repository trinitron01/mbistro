package com.bfs.mbistro.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.bfs.mbistro.model.base.NamedItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class Restaurant implements NamedItem, Parcelable {

  public static final Parcelable.Creator<Restaurant> CREATOR =
      new Parcelable.Creator<Restaurant>() {
        @Override public Restaurant createFromParcel(Parcel source) {
          return new Restaurant(source);
        }

        @Override public Restaurant[] newArray(int size) {
          return new Restaurant[size];
        }
      };
  @SerializedName("id") @Expose public String id;
  @SerializedName("name") @Expose public String name;
  @SerializedName("url") @Expose public String url;
  @SerializedName("location") @Expose public RestaurantLocation location;
  @SerializedName("switch_to_order_menu") @Expose public Integer switchToOrderMenu;
  @SerializedName("cuisines") @Expose public String cuisines;
  @SerializedName("average_cost_for_two") @Expose public int averageCostForTwo;
  @SerializedName("price_range") @Expose public Integer priceRange;
  @SerializedName("currency") @Expose public String currency;
  @SerializedName("offers") @Expose public List<Object> offers = null;
  @SerializedName("thumb") @Expose public String thumb;
  @SerializedName("user_rating") @Expose public UserRating userRating;
  @SerializedName("photos_url") @Expose public String photosUrl;
  @SerializedName("menu_url") @Expose public String menuUrl;
  @SerializedName("featured_image") @Expose public String featuredImage;
  @SerializedName("has_online_delivery") @Expose public Integer hasOnlineDelivery;
  @SerializedName("is_delivering_now") @Expose public Integer isDeliveringNow;
  @SerializedName("deeplink") @Expose public String deeplink;
  @SerializedName("has_table_booking") @Expose public Integer hasTableBooking;
  @SerializedName("events_url") @Expose public String eventsUrl;
  @SerializedName("establishment_types") @Expose public List<Object> establishmentTypes = null;

  public Restaurant() {
  }

  protected Restaurant(Parcel in) {
    this.id = in.readString();
    this.name = in.readString();
    this.url = in.readString();
    this.location = in.readParcelable(RestaurantLocation.class.getClassLoader());
    this.switchToOrderMenu = (Integer) in.readValue(Integer.class.getClassLoader());
    this.cuisines = in.readString();
    this.averageCostForTwo = in.readInt();
    this.priceRange = (Integer) in.readValue(Integer.class.getClassLoader());
    this.currency = in.readString();
    this.offers = new ArrayList<Object>();
    in.readList(this.offers, Object.class.getClassLoader());
    this.thumb = in.readString();
    this.userRating = in.readParcelable(UserRating.class.getClassLoader());
    this.photosUrl = in.readString();
    this.menuUrl = in.readString();
    this.featuredImage = in.readString();
    this.hasOnlineDelivery = (Integer) in.readValue(Integer.class.getClassLoader());
    this.isDeliveringNow = (Integer) in.readValue(Integer.class.getClassLoader());
    this.deeplink = in.readString();
    this.hasTableBooking = (Integer) in.readValue(Integer.class.getClassLoader());
    this.eventsUrl = in.readString();
    this.establishmentTypes = new ArrayList<Object>();
    in.readList(this.establishmentTypes, Object.class.getClassLoader());
  }

  @Override public String getName() {
    return name;
  }

  public String getId() {
    return id;
  }

  public String getCuisines() {
    return cuisines;
  }

  public int getAverageCostForTwo() {
    return averageCostForTwo;
  }

  public String getCurrency() {
    return currency;
  }

  public UserRating getUserRating() {
    return userRating;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeString(this.name);
    dest.writeString(this.url);
    dest.writeParcelable(this.location, flags);
    dest.writeValue(this.switchToOrderMenu);
    dest.writeString(this.cuisines);
    dest.writeInt(this.averageCostForTwo);
    dest.writeValue(this.priceRange);
    dest.writeString(this.currency);
    dest.writeList(this.offers);
    dest.writeString(this.thumb);
    dest.writeParcelable(this.userRating, flags);
    dest.writeString(this.photosUrl);
    dest.writeString(this.menuUrl);
    dest.writeString(this.featuredImage);
    dest.writeValue(this.hasOnlineDelivery);
    dest.writeValue(this.isDeliveringNow);
    dest.writeString(this.deeplink);
    dest.writeValue(this.hasTableBooking);
    dest.writeString(this.eventsUrl);
    dest.writeList(this.establishmentTypes);
  }
}
