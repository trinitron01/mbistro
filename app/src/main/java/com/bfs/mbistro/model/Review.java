package com.bfs.mbistro.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review implements Parcelable {

  public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
    @Override public Review createFromParcel(Parcel source) {
      return new Review(source);
    }

    @Override public Review[] newArray(int size) {
      return new Review[size];
    }
  };
  @SerializedName("rating_color") @Expose private String rating_color;
  @SerializedName("review_time_friendly") @Expose private String review_time_friendly;
  @SerializedName("rating_text") @Expose private String rating_text;
  @SerializedName("comments_count") @Expose private Integer comments_count;
  @SerializedName("rating") @Expose private float rating;
  @SerializedName("review_text") @Expose private String review_text;
  @SerializedName("id") @Expose private Integer id;
  @SerializedName("user") @Expose private User user;
  @SerializedName("timestamp") @Expose private Integer timestamp;
  @SerializedName("likes") @Expose private Integer likes;

  public Review() {
  }

  protected Review(Parcel in) {
    this.rating_color = in.readString();
    this.review_time_friendly = in.readString();
    this.rating_text = in.readString();
    this.comments_count = (Integer) in.readValue(Integer.class.getClassLoader());
    this.rating = in.readFloat();
    this.review_text = in.readString();
    this.id = (Integer) in.readValue(Integer.class.getClassLoader());
    this.user = in.readParcelable(User.class.getClassLoader());
    this.timestamp = (Integer) in.readValue(Integer.class.getClassLoader());
    this.likes = (Integer) in.readValue(Integer.class.getClassLoader());
  }

  public String getRating_color() {
    return rating_color;
  }

  public void setRating_color(String rating_color) {
    this.rating_color = rating_color;
  }

  public String getReview_time_friendly() {
    return review_time_friendly;
  }

  public void setReview_time_friendly(String review_time_friendly) {
    this.review_time_friendly = review_time_friendly;
  }

  public String getRating_text() {
    return rating_text;
  }

  public void setRating_text(String rating_text) {
    this.rating_text = rating_text;
  }

  public Integer getComments_count() {
    return comments_count;
  }

  public void setComments_count(Integer comments_count) {
    this.comments_count = comments_count;
  }

  public void setRating(Integer rating) {
    this.rating = rating;
  }

  public Object getReview_text() {
    return review_text;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Integer getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Integer timestamp) {
    this.timestamp = timestamp;
  }

  public Integer getLikes() {
    return likes;
  }

  public void setLikes(Integer likes) {
    this.likes = likes;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.rating_color);
    dest.writeString(this.review_time_friendly);
    dest.writeString(this.rating_text);
    dest.writeValue(this.comments_count);
    dest.writeFloat(this.rating);
    dest.writeString(this.review_text);
    dest.writeValue(this.id);
    dest.writeParcelable(this.user, flags);
    dest.writeValue(this.timestamp);
    dest.writeValue(this.likes);
  }
}