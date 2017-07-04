package com.bfs.mbistro.module.restaurant.details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("zomato_handle")
    @Expose
    private String zomatoHandle;
    @SerializedName("foodie_level")
    @Expose
    private String foodieLevel;
    @SerializedName("foodie_level_num")
    @Expose
    private String foodieLevelNum;
    @SerializedName("foodie_color")
    @Expose
    private String foodieColor;
    @SerializedName("profile_url")
    @Expose
    private String profileUrl;
    @SerializedName("profile_deeplink")
    @Expose
    private String profileDeeplink;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;

    protected User(Parcel in) {
        name = in.readString();
        zomatoHandle = in.readString();
        foodieLevel = in.readString();
        foodieLevelNum = in.readString();
        foodieColor = in.readString();
        profileUrl = in.readString();
        profileDeeplink = in.readString();
        profileImage = in.readString();
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
        dest.writeString(name);
        dest.writeString(zomatoHandle);
        dest.writeString(foodieLevel);
        dest.writeString(foodieLevelNum);
        dest.writeString(foodieColor);
        dest.writeString(profileUrl);
        dest.writeString(profileDeeplink);
        dest.writeString(profileImage);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
