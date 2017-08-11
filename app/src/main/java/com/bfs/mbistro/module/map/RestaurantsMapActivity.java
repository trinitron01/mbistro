package com.bfs.mbistro.module.map;

import android.os.Bundle;
import com.bfs.mbistro.AndroidUtils;
import com.bfs.mbistro.R;
import com.bfs.mbistro.di.BistroComponent;
import com.bfs.mbistro.location.base.BaseLocationAwareActivity;

public class RestaurantsMapActivity extends BaseLocationAwareActivity {

  private RestaurantsMapFragment restaurantsMapFragment;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);
    restaurantsMapFragment = (RestaurantsMapFragment) getSupportFragmentManager().findFragmentById(
        R.id.restaurants_map_fragment);
  }

  @Override protected void inject(BistroComponent component) {
    component.inject(this);
  }

  @Override public void onNewLocation(double latitude, double longitude) {
    super.onNewLocation(latitude, longitude);
    if (AndroidUtils.isFragmentAlive(restaurantsMapFragment)) {
      restaurantsMapFragment.onNewLocation(latitude, longitude);
    }
  }
}
