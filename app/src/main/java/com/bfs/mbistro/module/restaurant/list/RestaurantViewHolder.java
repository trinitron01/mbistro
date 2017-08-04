package com.bfs.mbistro.module.restaurant.list;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import com.bfs.mbistro.AndroidUtils;
import com.bfs.mbistro.R;
import com.bfs.mbistro.base.adapter.BaseViewHolder;
import com.bfs.mbistro.base.listener.ItemClickListener;
import com.bfs.mbistro.model.Restaurant;
import com.bfs.mbistro.model.RestaurantContainer;
import com.bfs.mbistro.model.UserRating;
import java.util.Locale;

import static com.bfs.mbistro.module.restaurant.mvp.RestaurantsContract.RowView;

class RestaurantViewHolder extends BaseViewHolder implements RowView, View.OnClickListener {

  private static final String RATING_FLOAT_VALUE_PATTERN = "%.1f";
  private static final char COLOR_VALUE_PREFIX = '#';
  private final TextView titleView;
  private final TextView descriptionView;
  private final TextView ratingLabel;
  private final TextView ratingCountLabel;
  private final RatingBar ratingBar;
  private final TextView priceForTwoView;
  private ItemClickListener<RestaurantContainer> itemClickListener;
  private RestaurantContainer item;

  RestaurantViewHolder(View itemView, ItemClickListener<RestaurantContainer> itemClickListener) {
    super(itemView);
    titleView = (TextView) itemView.findViewById(R.id.list_item_title);
    descriptionView = (TextView) itemView.findViewById(R.id.list_item_description);
    ratingLabel = (TextView) itemView.findViewById(R.id.list_item_rating);
    ratingCountLabel = (TextView) itemView.findViewById(R.id.list_item_rating_votes);
    ratingBar = (RatingBar) itemView.findViewById(R.id.list_item_rating_bar);
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
    UserRating userRating = restaurant.getUserRating();
    int ratingColor = Color.parseColor(COLOR_VALUE_PREFIX + userRating.getRatingColor());
    ratingLabel.setTextColor(ratingColor);
    ratingCountLabel.setTextColor(ratingColor);
    Resources resources = ratingLabel.getResources();
    ratingCountLabel.setText(userRating.getVotes());
    Drawable votesCountDrawable =
        AndroidUtils.getTintedVectorDrawable(R.drawable.ic_group_black_16dp, ratingColor,
            itemView.getContext());
    ratingCountLabel.setCompoundDrawablesWithIntrinsicBounds(null, null, votesCountDrawable, null);
    ratingLabel.setText(
        String.format(Locale.US, RATING_FLOAT_VALUE_PATTERN, userRating.getAggregateRating()));
    ratingBar.setRating(userRating.getAggregateRating());
    int averageCostForTwo = restaurant.getAverageCostForTwo();
    if (averageCostForTwo > 0) {
      priceForTwoView.setVisibility(View.VISIBLE);
      priceForTwoView.setText(
          String.format(resources.getString(R.string.avg_cost_for_two_item_pattern),
              averageCostForTwo, restaurant.getCurrency()));
    } else {
      priceForTwoView.setVisibility(View.INVISIBLE);
    }
  }

  @Override public void onClick(View v) {
    itemClickListener.onItemClicked(item);
  }
}
