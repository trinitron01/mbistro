package com.bfs.mbistro.model;

import com.squareup.moshi.Json;
import java.util.List;

public class Restaurants {

    @Json(name = "results_found")

    public Integer resultsFound;
    @Json(name = "results_start")

    public Integer resultsStart;
    @Json(name = "results_shown")

    public Integer resultsShown;
    @Json(name = "restaurants")

    public List<RestaurantContainer> restaurants = null;

}
