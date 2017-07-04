package com.bfs.mbistro.module.restaurant.list;

import android.os.Parcel;

import java.util.List;

class RestaurantContainerPaginatedList extends PaginatedList<RestaurantContainer> {

    RestaurantContainerPaginatedList(List<RestaurantContainer> restaurants, Integer resultsShown, Integer resultsStart) {
        super(resultsShown, resultsStart, restaurants);
    }

    private RestaurantContainerPaginatedList(Parcel in) {
        super(in);
    }

    @Override
    protected List<RestaurantContainer> readFromParcel(Parcel in) {
        return in.createTypedArrayList(RestaurantContainer.CREATOR);
    }

    public static final Creator<RestaurantContainerPaginatedList> CREATOR = new Creator<RestaurantContainerPaginatedList>() {
        @Override
        public RestaurantContainerPaginatedList createFromParcel(Parcel source) {
            return new RestaurantContainerPaginatedList(source);
        }

        @Override
        public RestaurantContainerPaginatedList[] newArray(int size) {
            return new RestaurantContainerPaginatedList[size];
        }
    };
}
