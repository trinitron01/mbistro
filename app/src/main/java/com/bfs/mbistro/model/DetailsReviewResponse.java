package com.bfs.mbistro.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DetailsReviewResponse implements Parcelable {

  public static final Parcelable.Creator<DetailsReviewResponse> CREATOR =
      new Parcelable.Creator<DetailsReviewResponse>() {
        @Override public DetailsReviewResponse createFromParcel(Parcel source) {
          return new DetailsReviewResponse(source);
        }

        @Override public DetailsReviewResponse[] newArray(int size) {
          return new DetailsReviewResponse[size];
        }
      };
  public final RestaurantDetails restaurant;
  public final ReviewsResponse reviewsResponse;

  public DetailsReviewResponse(RestaurantDetails restaurantDetails,
      ReviewsResponse reviewsResponse) {
    this.restaurant = restaurantDetails;
    this.reviewsResponse = reviewsResponse;
  }

  private DetailsReviewResponse(Parcel in) {
    this.restaurant = in.readParcelable(RestaurantDetails.class.getClassLoader());
    this.reviewsResponse = in.readParcelable(ReviewsResponse.class.getClassLoader());
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(this.restaurant, flags);
    dest.writeParcelable(this.reviewsResponse, flags);
  }
}
