package com.bfs.mbistro.network;

import com.bfs.mbistro.module.establishment.EstablishmentsResponse;
import com.bfs.mbistro.module.restaurant.details.RestaurantDetails;
import com.bfs.mbistro.module.restaurant.list.RestaurantsResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import rx.Single;

public interface ApiService {

    @GET("search")
    Observable<RestaurantsResponse> getRestaurants(@Query("entity_id") int cityId, @Query("entity_type") String type, @Query("lang") String langId,
            @Query("start") int startOffset, @Query("count") int count);

    @GET("restaurant")
    Single<RestaurantDetails> getRestaurant(@Query("res_id") String restaurantId, @Query("lang") String langId);

    @GET("establishments")
    Single<EstablishmentsResponse> getEstablishments(@Query("lat") double lat, @Query("lon") double lon, @Query("lang") String langId);

}
