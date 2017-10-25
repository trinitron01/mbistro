package com.bfs.mbistro.model.location;

import com.squareup.moshi.Json;

public class UserLocation__ {

  @Json(name = "address") private String address;
  @Json(name = "locality") private String locality;
  @Json(name = "city") private String city;
  @Json(name = "city_id") private Integer cityId;
  @Json(name = "latitude") private String latitude;
  @Json(name = "longitude") private String longitude;
  @Json(name = "zipcode") private String zipcode;
  @Json(name = "country_id") private Integer countryId;
  @Json(name = "locality_verbose") private String localityVerbose;

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getLocality() {
    return locality;
  }

  public void setLocality(String locality) {
    this.locality = locality;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public Integer getCityId() {
    return cityId;
  }

  public void setCityId(Integer cityId) {
    this.cityId = cityId;
  }

  public String getLatitude() {
    return latitude;
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  public String getLongitude() {
    return longitude;
  }

  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  public String getZipcode() {
    return zipcode;
  }

  public void setZipcode(String zipcode) {
    this.zipcode = zipcode;
  }

  public Integer getCountryId() {
    return countryId;
  }

  public void setCountryId(Integer countryId) {
    this.countryId = countryId;
  }

  public String getLocalityVerbose() {
    return localityVerbose;
  }

  public void setLocalityVerbose(String localityVerbose) {
    this.localityVerbose = localityVerbose;
  }
}
