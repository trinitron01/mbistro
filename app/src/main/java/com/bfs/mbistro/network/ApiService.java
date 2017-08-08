package com.bfs.mbistro.network;

import com.bfs.mbistro.model.Establishments;
import com.bfs.mbistro.model.RestaurantDetails;
import com.bfs.mbistro.model.Restaurants;
import com.bfs.mbistro.model.ReviewsResponse;
import com.bfs.mbistro.model.location.UserLocation;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import rx.Single;

public interface ApiService {

  String LATITUDE = "lat";
  String LONGITUDE = "lon";
  
  @GET("search") Observable<Restaurants> getRestaurants(@Query("entity_id") int cityId,
      @Query("entity_type") String type, @Query("start") int startOffset,
      @Query("count") int count);

  @GET("restaurant") Observable<RestaurantDetails> getRestaurant(
      @Query("res_id") String restaurantId);

  @GET("reviews") Observable<ReviewsResponse> getReviews(@Query("res_id") String restaurantId);

  @GET("establishments") Single<Establishments> getEstablishments(@Query(LATITUDE) double lat,
      @Query(LONGITUDE) double lon);

  @GET("geocode") Observable<UserLocation> geocode(@Query(LATITUDE) double lat,
      @Query(LONGITUDE) double lon);
}
