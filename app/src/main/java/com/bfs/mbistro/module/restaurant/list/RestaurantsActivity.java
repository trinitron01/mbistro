package com.bfs.mbistro.module.restaurant.list;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.bfs.mbistro.R;
import com.bfs.mbistro.di.BistroComponent;
import com.bfs.mbistro.location.base.BaseLocationAwareActivity;
import com.bfs.mbistro.module.map.AndroidMapScreenNavigator;
import com.bfs.mbistro.module.map.MapScreenNavigator;
import timber.log.Timber;

import static com.bfs.mbistro.AndroidUtils.isFragmentAlive;

public class RestaurantsActivity extends BaseLocationAwareActivity {

  private MapScreenNavigator mapScreenNavigator;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.restaurant_list);
    Toolbar toolbar = findView(R.id.toolbar);
    setSupportActionBar(toolbar);
    mapScreenNavigator = new AndroidMapScreenNavigator(this);
  }

  @Override protected void inject(BistroComponent component) {
    component.inject(this);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.restaurants_list, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.switch_to_map) {
      mapScreenNavigator.navigateToMapView();
    }
    return super.onOptionsItemSelected(item);
  }

  @Override public void onNewLocation(double latitude, double longitude) {
    super.onNewLocation(latitude, longitude);
    RestaurantsFragment restaurantsFragment =
        (RestaurantsFragment) getSupportFragmentManager().findFragmentById(
            R.id.restaurants_fragment);
    if (isFragmentAlive(restaurantsFragment)) {
      restaurantsFragment.onNewLocation(latitude, longitude);
    } else {
      Timber.w("UserLocationResponse changed but RestaurantsFragment is no longer alive");
    }
  }
}

