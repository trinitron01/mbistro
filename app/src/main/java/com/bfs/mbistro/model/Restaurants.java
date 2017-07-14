package com.bfs.mbistro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Restaurants {

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
