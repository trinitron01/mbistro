package com.bfs.mbistro.module.map;

import android.content.Context;
import android.content.Intent;

public class AndroidMapScreenPresenter implements MapScreenPresenter {

  private final Context context;

  public AndroidMapScreenPresenter(Context context) {
    this.context = context;
  }

  @Override public void switchToMapView() {
    context.startActivity(new Intent(context, RestaurantsMapActivity.class));
  }
}
