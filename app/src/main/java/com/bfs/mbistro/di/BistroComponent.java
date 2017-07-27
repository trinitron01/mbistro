package com.bfs.mbistro.di;

import com.bfs.mbistro.module.restaurant.details.ui.RestaurantDetailsActivity;
import com.bfs.mbistro.module.restaurant.details.ui.RestaurantDetailsFragment;
import com.bfs.mbistro.module.restaurant.list.RestaurantsActivity;
import com.bfs.mbistro.module.restaurant.list.RestaurantsFragment;
import dagger.Component;
import javax.inject.Singleton;

@Singleton @Component(modules = { BistroServiceModule.class, AppModule.class })
public interface BistroComponent {

  void inject(RestaurantsActivity activity);

  void inject(RestaurantDetailsActivity activity);

  void inject(RestaurantsFragment fragment);

  void inject(RestaurantDetailsFragment fragment);
}
