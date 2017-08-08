package com.bfs.mbistro.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class ReviewsResponse implements Parcelable {

  public static final Parcelable.Creator<ReviewsResponse> CREATOR =
      new Parcelable.Creator<ReviewsResponse>() {
        @Override public ReviewsResponse createFromParcel(Parcel source) {
          return new ReviewsResponse(source);
        }

        @Override public ReviewsResponse[] newArray(int size) {
          return new ReviewsResponse[size];
        }
      };
  @SerializedName("reviews_start") @Expose private Integer reviews_start;
  @SerializedName("user_reviews") @Expose private List<UserReviews> user_reviews;
  @SerializedName("reviews_shown") @Expose private Integer reviews_shown;
  @SerializedName("reviews_count") @Expose private Integer reviews_count;

  public ReviewsResponse() {
  }

  protected ReviewsResponse(Parcel in) {
    this.reviews_start = (Integer) in.readValue(Integer.class.getClassLoader());
    this.user_reviews = new ArrayList<UserReviews>();
    in.readList(this.user_reviews, UserReviews.class.getClassLoader());
    this.reviews_shown = (Integer) in.readValue(Integer.class.getClassLoader());
    this.reviews_count = (Integer) in.readValue(Integer.class.getClassLoader());
  }

  public Integer getReviews_start() {
    return reviews_start;
  }

  public void setReviews_start(Integer reviews_start) {
    this.reviews_start = reviews_start;
  }

  public List<UserReviews> getUser_reviews() {
    return user_reviews;
  }

  public void setUser_reviews(List<UserReviews> user_reviews) {
    this.user_reviews = user_reviews;
  }

  public Integer getReviews_shown() {
    return reviews_shown;
  }

  public void setReviews_shown(Integer reviews_shown) {
    this.reviews_shown = reviews_shown;
  }

  public Integer getReviews_count() {
    return reviews_count;
  }

  public void setReviews_count(Integer reviews_count) {
    this.reviews_count = reviews_count;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(this.reviews_start);
    dest.writeList(this.user_reviews);
    dest.writeValue(this.reviews_shown);
    dest.writeValue(this.reviews_count);
  }
}