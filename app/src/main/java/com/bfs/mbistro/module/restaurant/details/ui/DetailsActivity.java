package com.bfs.mbistro.module.restaurant.details.ui;

import com.bfs.mbistro.R;
import com.bfs.mbistro.base.BaseActivity;
import com.bfs.mbistro.module.restaurant.details.RestaurantDetails;
import com.bfs.mbistro.module.restaurant.list.Restaurant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DetailsActivity extends BaseActivity {

    private static final String RESTAURANT_DETAILS_KEY = "RESTAURANT_DETAILS_KEY";
    private Restaurant restaurant;
    private TextView textDetails;

    public static void start(Context context, Restaurant restaurant) {
        Intent starter = new Intent(context, DetailsActivity.class);
        starter.putExtra(RESTAURANT_DETAILS_KEY, restaurant);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null && getIntent().hasExtra(RESTAURANT_DETAILS_KEY)) {
            restaurant = getIntent().getParcelableExtra(RESTAURANT_DETAILS_KEY);
        } else {
            throw new IllegalArgumentException("Restaurant Intent Key has not been sent");
        }
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textDetails = (TextView) findViewById(R.id.textDetails);
        textDetails.setText(restaurant.toString());
        setTitle(restaurant.getName());
        downloadDetails();
    }

    private void downloadDetails() {
        service.getRestaurant(restaurant.getId(), "pl").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<RestaurantDetails>() {
                    @Override
                    public void onSuccess(RestaurantDetails value) {

                    }

                    @Override
                    public void onError(Throwable error) {

                    }
                });
    }
}
