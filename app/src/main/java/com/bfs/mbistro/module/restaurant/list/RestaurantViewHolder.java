package com.bfs.mbistro.module.restaurant.list;

import android.view.View;
import android.widget.TextView;
import com.bfs.mbistro.R;
import com.bfs.mbistro.base.adapter.BaseViewHolder;
import com.bfs.mbistro.base.listener.ItemClickListener;
import com.bfs.mbistro.model.Restaurant;
import com.bfs.mbistro.model.RestaurantContainer;
import com.bfs.mbistro.model.UserRating;
import com.bfs.mbistro.module.restaurant.rating.RestaurantRatingView;

import static com.bfs.mbistro.module.restaurant.mvp.RestaurantsListContract.RowView;

class RestaurantViewHolder extends BaseViewHolder implements RowView, View.OnClickListener {

  private final TextView titleView;
  private final TextView descriptionView;
  private final RestaurantRatingView ratingView;
  private final TextView priceForTwoView;
  private ItemClickListener<RestaurantContainer> itemClickListener;
  private RestaurantContainer item;

  RestaurantViewHolder(View itemView, ItemClickListener<RestaurantContainer> itemClickListener) {
    super(itemView);
    titleView = (TextView) itemView.findViewById(R.id.list_item_title);
    descriptionView = (TextView) itemView.findViewById(R.id.list_item_description);
    ratingView = (RestaurantRatingView) itemView.findViewById(R.id.list_item_rating_container);
    priceForTwoView = (TextView) itemView.findViewById(R.id.list_item_price_for_two);
    if (itemClickListener != null) {
      this.itemClickListener = itemClickListener;
      itemView.setOnClickListener(this);
    }
  }

  @Override public void showRestaurantRow(RestaurantContainer container) {
    this.item = container;
    titleView.setText(container.getName());
    Restaurant restaurant = container.getRestaurant();
    descriptionView.setText(restaurant.getCuisines());
    UserRating rating = container.restaurant.userRating;
    ratingView.setRatingValue(rating.getVotes(), rating.getAggregateRating(),
        rating.getRatingColor());
    int averageCostForTwo = restaurant.getAverageCostForTwo();
    if (averageCostForTwo > 0) {
      priceForTwoView.setVisibility(View.VISIBLE);
      priceForTwoView.setText(
          String.format(itemView.getResources().getString(R.string.avg_cost_for_two_item_pattern),
              averageCostForTwo, restaurant.getCurrency()));
    } else {
      priceForTwoView.setVisibility(View.INVISIBLE);
    }
  }

  @Override public void onClick(View v) {
    itemClickListener.onItemClicked(item);
  }
}
