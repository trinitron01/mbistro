package com.bfs.mbistro.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.squareup.moshi.Json;

public class UserReviews implements Parcelable {

  public static final Parcelable.Creator<UserReviews> CREATOR =
      new Parcelable.Creator<UserReviews>() {
        @Override public UserReviews createFromParcel(Parcel source) {
          return new UserReviews(source);
        }

        @Override public UserReviews[] newArray(int size) {
          return new UserReviews[size];
        }
      };
  @Json(name = "review") private Review review;

  public UserReviews() {
  }

  protected UserReviews(Parcel in) {
    this.review = in.readParcelable(Review.class.getClassLoader());
  }

  public Review getReview() {
    return review;
  }

  public void setReview(Review review) {
    this.review = review;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(this.review, flags);
  }
}