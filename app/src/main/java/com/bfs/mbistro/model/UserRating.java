package com.bfs.mbistro.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserRating implements Parcelable {

  public static final Creator<UserRating> CREATOR = new Creator<UserRating>() {
    @Override public UserRating createFromParcel(Parcel in) {
      return new UserRating(in);
    }

    @Override public UserRating[] newArray(int size) {
      return new UserRating[size];
    }
  };
    @SerializedName("aggregate_rating") @Expose private float aggregateRating;
    @SerializedName("rating_text")
    @Expose
    private String ratingText;
    @SerializedName("rating_color")
    @Expose
    private String ratingColor;
    @SerializedName("votes")
    @Expose
    private String votes;

    protected UserRating(Parcel in) {
      aggregateRating = in.readFloat();
        ratingText = in.readString();
        ratingColor = in.readString();
        votes = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      dest.writeFloat(aggregateRating);
        dest.writeString(ratingText);
        dest.writeString(ratingColor);
        dest.writeString(votes);
    }

  public float getAggregateRating() {
    return aggregateRating;
  }

  public String getVotes() {
    return votes;
  }

  public String getRatingColor() {
    return ratingColor;
  }
}
