package com.bfs.mbistro.module.restaurant.rating;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import com.bfs.mbistro.AndroidUtils;
import com.bfs.mbistro.R;
import com.bfs.mbistro.model.UserRating;
import java.util.Locale;

public class RestaurantRatingView extends LinearLayout {

  private static final String RATING_FLOAT_VALUE_PATTERN = "%.1f";
  private static final char COLOR_VALUE_PREFIX = '#';
  private TextView ratingLabel;
  private TextView ratingCountLabel;
  private RatingBar ratingBar;

  public RestaurantRatingView(Context context) {
    this(context, null);
  }

  public RestaurantRatingView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public RestaurantRatingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public RestaurantRatingView(Context context, AttributeSet attrs, int defStyleAttr,
      int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init();
  }

  private void init() {
    LayoutInflater inflater =
        (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    inflater.inflate(R.layout.rating_layout, this, true);
    ratingLabel = (TextView) findViewById(R.id.list_item_rating);
    ratingCountLabel = (TextView) findViewById(R.id.list_item_rating_votes);
    ratingBar = (RatingBar) findViewById(R.id.list_item_rating_bar);
  }

  public void setRating(UserRating userRating) {
    int ratingColor = Color.parseColor(COLOR_VALUE_PREFIX + userRating.getRatingColor());
    ratingLabel.setTextColor(ratingColor);
    ratingCountLabel.setTextColor(ratingColor);
    ratingCountLabel.setText(userRating.getVotes());
    Drawable votesCountDrawable =
        AndroidUtils.getTintedVectorDrawable(R.drawable.ic_group_black_16dp, ratingColor,
            getContext());
    ratingCountLabel.setCompoundDrawablesWithIntrinsicBounds(null, null, votesCountDrawable, null);
    ratingLabel.setText(
        String.format(Locale.US, RATING_FLOAT_VALUE_PATTERN, userRating.getAggregateRating()));
    ratingBar.setRating(userRating.getAggregateRating());
  }
}
