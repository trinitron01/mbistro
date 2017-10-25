package com.bfs.mbistro.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.bfs.mbistro.model.base.NamedItem;
import com.squareup.moshi.Json;
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
  @Json(name = "id") public String id;
  @Json(name = "name") public String name;
  @Json(name = "url") public String url;
  @Json(name = "location") public RestaurantLocation location;
  @Json(name = "switch_to_order_menu") public Integer switchToOrderMenu;
  @Json(name = "cuisines") public String cuisines;
  @Json(name = "average_cost_for_two") public int averageCostForTwo;
  @Json(name = "price_range") public Integer priceRange;
  @Json(name = "currency") public String currency;
  @Json(name = "offers") public List<Object> offers = null;
  @Json(name = "thumb") public String thumb;
  @Json(name = "user_rating") public UserRating userRating;
  @Json(name = "photos_url") public String photosUrl;
  @Json(name = "menu_url") public String menuUrl;
  @Json(name = "featured_image") public String featuredImage;
  @Json(name = "has_online_delivery") public Integer hasOnlineDelivery;
  @Json(name = "is_delivering_now") public Integer isDeliveringNow;
  @Json(name = "deeplink") public String deeplink;
  @Json(name = "has_table_booking") public Integer hasTableBooking;
  @Json(name = "events_url") public String eventsUrl;
  @Json(name = "establishment_types") public List<Object> establishmentTypes = null;

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
