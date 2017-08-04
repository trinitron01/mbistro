package com.bfs.mbistro.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RestaurantDetails implements Parcelable {

  public static final Creator<RestaurantDetails> CREATOR = new Creator<RestaurantDetails>() {
    @Override public RestaurantDetails createFromParcel(Parcel in) {
      return new RestaurantDetails(in);
    }

    @Override public RestaurantDetails[] newArray(int size) {
      return new RestaurantDetails[size];
    }
  };
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("location") @Expose private RestaurantLocation location;
    @SerializedName("average_cost_for_two")
    @Expose
    private String averageCostForTwo;
    @SerializedName("price_range")
    @Expose
    private String priceRange;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("thumb")
    @Expose
    private String thumb;
    @SerializedName("featured_image")
    @Expose
    private String featuredImage;
    @SerializedName("photos_url")
    @Expose
    private String photosUrl;
    @SerializedName("menu_url")
    @Expose
    private String menuUrl;
    @SerializedName("events_url")
    @Expose
    private String eventsUrl;
    @SerializedName("user_rating")
    @Expose
    private UserRating userRating;
    @SerializedName("has_online_delivery")
    @Expose
    private String hasOnlineDelivery;
    @SerializedName("is_delivering_now")
    @Expose
    private String isDeliveringNow;
    @SerializedName("has_table_booking")
    @Expose
    private String hasTableBooking;
    @SerializedName("deeplink")
    @Expose
    private String deeplink;
    @SerializedName("cuisines")
    @Expose
    private String cuisines;
    @SerializedName("all_reviews_count")
    @Expose
    private String allReviewsCount;
    @SerializedName("photo_count")
    @Expose
    private String photoCount;
    @SerializedName("phone_numbers")
    @Expose
    private String phoneNumbers;
    @SerializedName("all_reviews")
    @Expose
    private List<AllReview> allReviews = null;

    protected RestaurantDetails(Parcel in) {
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
        hasOnlineDelivery = in.readString();
        isDeliveringNow = in.readString();
        hasTableBooking = in.readString();
        deeplink = in.readString();
        cuisines = in.readString();
        allReviewsCount = in.readString();
        photoCount = in.readString();
        phoneNumbers = in.readString();
        allReviews = in.createTypedArrayList(AllReview.CREATOR);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
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
        dest.writeString(hasOnlineDelivery);
        dest.writeString(isDeliveringNow);
        dest.writeString(hasTableBooking);
        dest.writeString(deeplink);
        dest.writeString(cuisines);
        dest.writeString(allReviewsCount);
        dest.writeString(photoCount);
        dest.writeString(phoneNumbers);
        dest.writeTypedList(allReviews);
    }

  @Override public String toString() {
    return id + "\n" + name + "\n"

        + url + "\n"

        + location

        + averageCostForTwo + "\n"

        + priceRange + "\n"

        + currency + "\n"

        + thumb + "\n"

        + featuredImage + "\n"

        + photosUrl + "\n"

        + menuUrl + "\n"

        + eventsUrl + "\n"

        + userRating

        + hasOnlineDelivery + "\n"

        + isDeliveringNow + "\n"

        + hasTableBooking + "\n"

        + deeplink + "\n"

        + cuisines + "\n"

        + allReviewsCount + "\n"

        + photoCount + "\n"

        + phoneNumbers + "\n"

        + allReviews
        + '}';
  }
}
