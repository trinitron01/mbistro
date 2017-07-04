package com.bfs.mbistro.module.restaurant.list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.bfs.mbistro.base.model.NamedItem;
import com.bfs.mbistro.module.restaurant.details.UserRating;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Restaurant implements Parcelable, NamedItem {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("location")
    @Expose
    public Location location;
    @SerializedName("switch_to_order_menu")
    @Expose
    public Integer switchToOrderMenu;
    @SerializedName("cuisines")
    @Expose
    public String cuisines;
    @SerializedName("average_cost_for_two")
    @Expose
    public Integer averageCostForTwo;
    @SerializedName("price_range")
    @Expose
    public Integer priceRange;
    @SerializedName("currency")
    @Expose
    public String currency;
    @SerializedName("offers")
    @Expose
    public List<Object> offers = null;
    @SerializedName("thumb")
    @Expose
    public String thumb;
    @SerializedName("user_rating")
    @Expose
    public UserRating userRating;
    @SerializedName("photos_url")
    @Expose
    public String photosUrl;
    @SerializedName("menu_url")
    @Expose
    public String menuUrl;
    @SerializedName("featured_image")
    @Expose
    public String featuredImage;
    @SerializedName("has_online_delivery")
    @Expose
    public Integer hasOnlineDelivery;
    @SerializedName("is_delivering_now")
    @Expose
    public Integer isDeliveringNow;
    @SerializedName("deeplink")
    @Expose
    public String deeplink;
    @SerializedName("has_table_booking")
    @Expose
    public Integer hasTableBooking;
    @SerializedName("events_url")
    @Expose
    public String eventsUrl;
    @SerializedName("establishment_types")
    @Expose
    public List<Object> establishmentTypes = null;

    protected Restaurant(Parcel in) {
        id = in.readString();
        name = in.readString();
        url = in.readString();
        cuisines = in.readString();
        currency = in.readString();
        thumb = in.readString();
        userRating = in.readParcelable(UserRating.class.getClassLoader());
        photosUrl = in.readString();
        menuUrl = in.readString();
        featuredImage = in.readString();
        deeplink = in.readString();
        eventsUrl = in.readString();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Restaurant{" + " id='" + id + '\'' + ", name='" + name + '\'' + ", url='" + url + '\'' + ", location=" + location + ", switchToOrderMenu="
                + switchToOrderMenu + ", cuisines='" + cuisines + '\'' + ", averageCostForTwo=" + averageCostForTwo + ", priceRange=" + priceRange
                + ", currency='" + currency + '\'' + ", offers=" + offers + ", thumb='" + thumb + '\'' + ", userRating=" + userRating + ", photosUrl='"
                + photosUrl + '\'' + ", menuUrl='" + menuUrl + '\'' + ", featuredImage='" + featuredImage + '\'' + ", hasOnlineDelivery=" + hasOnlineDelivery
                + ", isDeliveringNow=" + isDeliveringNow + ", deeplink='" + deeplink + '\'' + ", hasTableBooking=" + hasTableBooking + ", eventsUrl='"
                + eventsUrl + '\'' + ", establishmentTypes=" + establishmentTypes + '}';
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
        dest.writeString(cuisines);
        dest.writeString(currency);
        dest.writeString(thumb);
        dest.writeParcelable(userRating, flags);
        dest.writeString(photosUrl);
        dest.writeString(menuUrl);
        dest.writeString(featuredImage);
        dest.writeString(deeplink);
        dest.writeString(eventsUrl);
    }

    public String getId() {
        return id;
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

}
