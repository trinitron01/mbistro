package com.bfs.mbistro.module.restaurant.details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

public class AllReview implements Parcelable {

    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("review_text")
    @Expose
    private String reviewText;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("rating_color")
    @Expose
    private String ratingColor;
    @SerializedName("review_time_friendly")
    @Expose
    private String reviewTimeFriendly;
    @SerializedName("rating_text")
    @Expose
    private String ratingText;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("likes")
    @Expose
    private String likes;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("comments_count")
    @Expose
    private String commentsCount;

    protected AllReview(Parcel in) {
        rating = in.readString();
        reviewText = in.readString();
        id = in.readString();
        ratingColor = in.readString();
        reviewTimeFriendly = in.readString();
        ratingText = in.readString();
        timestamp = in.readString();
        likes = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        commentsCount = in.readString();
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRatingColor() {
        return ratingColor;
    }

    public void setRatingColor(String ratingColor) {
        this.ratingColor = ratingColor;
    }

    public String getReviewTimeFriendly() {
        return reviewTimeFriendly;
    }

    public void setReviewTimeFriendly(String reviewTimeFriendly) {
        this.reviewTimeFriendly = reviewTimeFriendly;
    }

    public String getRatingText() {
        return ratingText;
    }

    public void setRatingText(String ratingText) {
        this.ratingText = ratingText;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(String commentsCount) {
        this.commentsCount = commentsCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(rating);
        dest.writeString(reviewText);
        dest.writeString(id);
        dest.writeString(ratingColor);
        dest.writeString(reviewTimeFriendly);
        dest.writeString(ratingText);
        dest.writeString(timestamp);
        dest.writeString(likes);
        dest.writeParcelable(user, flags);
        dest.writeString(commentsCount);
    }

    public static final Creator<AllReview> CREATOR = new Creator<AllReview>() {
        @Override
        public AllReview createFromParcel(Parcel in) {
            return new AllReview(in);
        }

        @Override
        public AllReview[] newArray(int size) {
            return new AllReview[size];
        }
    };
}
