package com.bfs.mbistro.module.restaurant.list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RestaurantsResponse {

    @SerializedName("results_found")
    @Expose
    public Integer resultsFound;
    @SerializedName("results_start")
    @Expose
    public Integer resultsStart;
    @SerializedName("results_shown")
    @Expose
    public Integer resultsShown;
    @SerializedName("restaurants")
    @Expose
    public List<RestaurantContainer> restaurants = null;

}
