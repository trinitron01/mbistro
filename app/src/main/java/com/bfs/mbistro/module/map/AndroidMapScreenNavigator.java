package com.bfs.mbistro.module.map;

import android.content.Context;
import android.content.Intent;

public class AndroidMapScreenNavigator implements MapScreenNavigator {

  private final Context context;

  public AndroidMapScreenNavigator(Context context) {
    this.context = context;
  }

  @Override public void navigateToMapView() {
    context.startActivity(new Intent(context, RestaurantsMapActivity.class));
  }
}
