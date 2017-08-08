package com.bfs.mbistro.model;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Awesome Pojo Generator
 */
public class User implements Parcelable {

  public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
    @Override public User createFromParcel(Parcel source) {
      return new User(source);
    }

    @Override public User[] newArray(int size) {
      return new User[size];
    }
  };
  @SerializedName("zomato_handle") @Expose private String zomato_handle;
  @SerializedName("profile_deeplink") @Expose private String profile_deeplink;
  @SerializedName("profile_image") @Expose private String profile_image;
  @SerializedName("profile_url") @Expose private String profile_url;
  @SerializedName("foodie_color") @Expose private String foodie_color;
  @SerializedName("name") @Expose private String name;
  @SerializedName("foodie_level_num") @Expose private Integer foodie_level_num;
  @SerializedName("foodie_level") @Expose private String foodie_level;

  public User() {
  }

  protected User(Parcel in) {
    this.zomato_handle = in.readString();
    this.profile_deeplink = in.readString();
    this.profile_image = in.readString();
    this.profile_url = in.readString();
    this.foodie_color = in.readString();
    this.name = in.readString();
    this.foodie_level_num = (Integer) in.readValue(Integer.class.getClassLoader());
    this.foodie_level = in.readString();
  }

  public String getZomato_handle() {
    return zomato_handle;
  }

  public void setZomato_handle(String zomato_handle) {
    this.zomato_handle = zomato_handle;
  }

  public String getProfile_deeplink() {
    return profile_deeplink;
  }

  public void setProfile_deeplink(String profile_deeplink) {
    this.profile_deeplink = profile_deeplink;
  }

  public String getProfile_image() {
    return profile_image;
  }

  public void setProfile_image(String profile_image) {
    this.profile_image = profile_image;
  }

  public String getProfile_url() {
    return profile_url;
  }

  public void setProfile_url(String profile_url) {
    this.profile_url = profile_url;
  }

  public String getFoodie_color() {
    return foodie_color;
  }

  public void setFoodie_color(String foodie_color) {
    this.foodie_color = foodie_color;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getFoodie_level_num() {
    return foodie_level_num;
  }

  public void setFoodie_level_num(Integer foodie_level_num) {
    this.foodie_level_num = foodie_level_num;
  }

  public String getFoodie_level() {
    return foodie_level;
  }

  public void setFoodie_level(String foodie_level) {
    this.foodie_level = foodie_level;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.zomato_handle);
    dest.writeString(this.profile_deeplink);
    dest.writeString(this.profile_image);
    dest.writeString(this.profile_url);
    dest.writeString(this.foodie_color);
    dest.writeString(this.name);
    dest.writeValue(this.foodie_level_num);
    dest.writeString(this.foodie_level);
  }
}