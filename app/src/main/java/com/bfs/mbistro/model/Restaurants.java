package com.bfs.mbistro.model;

import com.squareup.moshi.Json;
import java.util.List;

public class Restaurants {

  @Json(name = "results_found") public int resultsFound;
  @Json(name = "results_start") public int resultsStart;
  @Json(name = "results_shown") public int resultsShown;
  @Json(name = "restaurants") public List<RestaurantContainer> restaurants = null;
}
