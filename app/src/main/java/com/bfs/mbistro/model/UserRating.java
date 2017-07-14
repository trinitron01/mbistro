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
    @SerializedName("aggregate_rating")
    @Expose
    private String aggregateRating;
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
        aggregateRating = in.readString();
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
        dest.writeString(aggregateRating);
        dest.writeString(ratingText);
        dest.writeString(ratingColor);
        dest.writeString(votes);
    }

    @Override
    public String toString() {
        return "UserRating{" + "aggregateRating='" + aggregateRating + '\'' + ", ratingText='" + ratingText + '\'' + ", ratingColor='" + ratingColor + '\''
                + ", votes='" + votes + '\'' + '}';
    }
}
