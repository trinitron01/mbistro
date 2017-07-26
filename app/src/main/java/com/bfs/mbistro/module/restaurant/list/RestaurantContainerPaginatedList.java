package com.bfs.mbistro.module.restaurant.list;

import android.os.Parcel;
import com.bfs.mbistro.model.RestaurantContainer;
import java.util.List;

class RestaurantContainerPaginatedList extends PaginatedList<RestaurantContainer> {

  public static final Creator<RestaurantContainerPaginatedList> CREATOR =
      new Creator<RestaurantContainerPaginatedList>() {
        @Override public RestaurantContainerPaginatedList createFromParcel(Parcel source) {
          return new RestaurantContainerPaginatedList(source);
        }

        @Override public RestaurantContainerPaginatedList[] newArray(int size) {
          return new RestaurantContainerPaginatedList[size];
        }
      };

    RestaurantContainerPaginatedList(List<RestaurantContainer> restaurants, Integer resultsShown, Integer resultsStart) {
        super(resultsShown, resultsStart, restaurants);
    }

  private RestaurantContainerPaginatedList(Parcel in) {
        super(in);
    }

  @Override protected Creator<RestaurantContainer> getItemCreator() {
    return RestaurantContainer.CREATOR;
  }

}
