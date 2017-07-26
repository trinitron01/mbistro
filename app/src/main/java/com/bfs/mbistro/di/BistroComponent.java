package com.bfs.mbistro.di;

import com.bfs.mbistro.module.restaurant.details.ui.DetailsActivity;
import com.bfs.mbistro.module.restaurant.list.RestaurantsActivity;
import dagger.Component;
import javax.inject.Singleton;

@Singleton @Component(modules = { BistroServiceModule.class, AppModule.class })
public interface BistroComponent {

  void inject(RestaurantsActivity activity);

  void inject(DetailsActivity activity);
}
