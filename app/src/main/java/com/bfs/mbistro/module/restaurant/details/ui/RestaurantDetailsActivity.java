package com.bfs.mbistro.module.restaurant.details.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.bfs.mbistro.R;
import com.bfs.mbistro.base.BaseActivity;
import com.bfs.mbistro.di.BistroComponent;
import com.bfs.mbistro.model.Restaurant;

public class RestaurantDetailsActivity extends BaseActivity {

  private static final String RESTAURANT_DETAILS_KEY = "RESTAURANT_DETAILS_KEY";
  private static final String DISTANCE_DETAILS_KEY = "DISTANCE_DETAILS_KEY";

  public static void start(Context context, Restaurant restaurant, float estimatedDistanceMeters) {
    Intent starter = new Intent(context, RestaurantDetailsActivity.class);
    starter.putExtra(RESTAURANT_DETAILS_KEY, restaurant);
    starter.putExtra(DISTANCE_DETAILS_KEY, estimatedDistanceMeters);
    context.startActivity(starter);
  }

  /**
   * cl
   */
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Restaurant restaurant;
    float distanceInMeters;
    if (getIntent() != null && getIntent().hasExtra(RESTAURANT_DETAILS_KEY)) {
      restaurant = getIntent().getParcelableExtra(RESTAURANT_DETAILS_KEY);
      distanceInMeters = getIntent().getFloatExtra(DISTANCE_DETAILS_KEY, 0);
    } else {
      throw new IllegalArgumentException("Restaurant Intent Key has not been sent");
    }
    setContentView(R.layout.restaurant_details);
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.fragment_details_container,
              RestaurantDetailsFragment.newInstance(restaurant.getId(), restaurant.getName(),
                  distanceInMeters))
          .commit();
    }
  }

  @Override protected void inject(BistroComponent component) {
    component.inject(this);
  }
}
