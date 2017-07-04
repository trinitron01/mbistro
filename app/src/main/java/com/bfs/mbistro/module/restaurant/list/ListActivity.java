package com.bfs.mbistro.module.restaurant.list;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.bfs.mbistro.BuildConfig;
import com.bfs.mbistro.CollectionUtils;
import com.bfs.mbistro.R;
import com.bfs.mbistro.base.BaseActivity;
import com.bfs.mbistro.module.restaurant.details.ui.DetailsActivity;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.FooterAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter_extensions.items.ProgressItem;
import com.mikepenz.fastadapter_extensions.scroll.EndlessRecyclerOnScrollListener;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ListActivity extends BaseActivity implements FastAdapter.OnClickListener<RestaurantListItem> {

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 111;
    private static final String PL = "pl";
    private static final String LIST_KEY = "LIST_KEY";
    public static final int REQUEST_LOCATION = 10002;
    private FastItemAdapter<RestaurantListItem> restaurantAdapter;
    private int resultsShown;
    private int resultsStart;
    private FooterAdapter<ProgressItem> bottomProgressBar;
    private GoogleApiClient googleApiClient;
    private FusedLocationProviderClient fusedLocationClient;
    private Location lastLocation;
    private PaginatedList<RestaurantContainer> paginatedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        restaurantAdapter = new FastItemAdapter<>();
        bottomProgressBar = new FooterAdapter<>();
        recyclerView.setAdapter(bottomProgressBar.wrap(restaurantAdapter));
        restaurantAdapter.withOnClickListener(this);
        recyclerView.addOnScrollListener(new MyEndlessRecyclerOnScrollListener());
        initGoogleClient();
        ArrayList<RestaurantListItem> items = null;
        if (savedInstanceState != null) {
            paginatedList = savedInstanceState.getParcelable(LIST_KEY);
            updateOffset(paginatedList.resultsShown, paginatedList.resultsStart);
            items = createListItems(paginatedList.newItems);
        }
        load(items);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        List<RestaurantListItem> adapterItems = restaurantAdapter.getAdapterItems();
        if (CollectionUtils.isNotNullNorEmpty(adapterItems)) {
            List<RestaurantContainer> restaurants = new ArrayList<>(adapterItems.size());
            for (RestaurantListItem adapterItem : adapterItems) {
                restaurants.add(adapterItem.restaurant);
            }
            paginatedList = new RestaurantContainerPaginatedList(restaurants, resultsShown, resultsStart);
            outState.putParcelable(LIST_KEY, paginatedList);
        }
        super.onSaveInstanceState(outState);
    }

    private void initGoogleClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        googleApiClient = new GoogleApiClient.Builder(this).addApi(Places.GEO_DATA_API).addApi(Places.PLACE_DETECTION_API).addApi(LocationServices.API)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        showSnackbar("on connection failed");
                    }
                }).build();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        final PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(ListActivity.this, REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case CommonStatusCodes.SUCCESS:
                        //todo
                        break;
                }
            }
        });
    }

    private void updateOffset(int resultsShown, int resultsStart) {
        this.resultsShown += resultsShown;
        this.resultsStart = resultsStart;
    }

    private void load(ArrayList<RestaurantListItem> cachedItems) {
        Observable<ArrayList<RestaurantListItem>> network = service.getRestaurants(264, "city", PL, resultsShown, 20).subscribeOn(Schedulers.io())
                .map(new Func1<RestaurantsResponse, ArrayList<RestaurantListItem>>() {
                    @Override
                    public ArrayList<RestaurantListItem> call(RestaurantsResponse restaurantsResponse) {
                        updateOffset(restaurantsResponse.resultsShown, restaurantsResponse.resultsStart);
                        return createListItems(restaurantsResponse.restaurants);
                    }
                });

        Observable<ArrayList<RestaurantListItem>> callObservable;
        if (cachedItems != null) {
            callObservable = Observable.concat(Observable.just(cachedItems), network).first(new Func1<ArrayList<RestaurantListItem>, Boolean>() {
                @Override
                public Boolean call(ArrayList<RestaurantListItem> restaurantListItems) {
                    return restaurantListItems != null;
                }
            });

        } else {
            callObservable = network;
        }
        callObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(new PaginatedListSingleSubscriber());
    }

    @NonNull
    private static ArrayList<RestaurantListItem> createListItems(List<RestaurantContainer> restaurants) {
        ArrayList<RestaurantListItem> restaurantItems = new ArrayList<>(restaurants.size());
        for (int i = 0; i < restaurants.size(); i++) {
            restaurantItems.add(new RestaurantListItem(restaurants.get(i)));
        }
        return restaurantItems;
    }

    @Override
    public boolean onClick(View v, IAdapter<RestaurantListItem> adapter, RestaurantListItem item, int position) {
        DetailsActivity.start(this, item.restaurant.restaurant);
        return true;
    }

    private void requestPerms() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (checkPermissions()) {
            getAddress();
        } else {
            requestPerms();
        }
    }

    /**
     * Gets the address for the last known location.
     */
    @SuppressWarnings("MissingPermission")
    private void getAddress() {
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<android.location.Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location == null) {
                    String text = "null location";
                    onLocation(text);
                } else {
                    lastLocation = location;
                /*    service.getEstablishments(lastLocation.getLatitude(),lastLocation.getLongitude(),PL)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleSubscriber<EstablishmentsResponse>() {
                        @Override
                        public void onSuccess(EstablishmentsResponse value) {

                        }

                        @Override
                        public void onError(Throwable error) {

                        }
                    });*/
                    onLocation("location found");
                }
                // Determine whether a Geocoder is available.
                if (!Geocoder.isPresent()) {
                    onLocation(getString(R.string.no_geocoder_available));
                }
            }

            private void onLocation(String text) {
                // showSnackbar(text);
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showSnackbar("getLastLocation:onFailure");
            }
        });
    }

    /**
     * Shows a {@link Snackbar} using {@code text}.
     *
     * @param text The Snackbar text.
     */
    private void showSnackbar(final String text) {
        View container = findViewById(android.R.id.content);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getAddress();
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showSnackbar(R.string.permission_denied_explanation, R.string.settings, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Build intent that displays the App settings screen.
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
                        intent.setData(uri);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
            }
        }
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId, View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content), getString(mainTextStringId), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOCATION && resultCode == Activity.RESULT_OK) {
            getAddress();
        }
    }

    private class PaginatedListSingleSubscriber extends Subscriber<ArrayList<RestaurantListItem>> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable error) {
            showSnackbar(R.string.download_error, R.string.retry, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    load(null);
                }
            });
        }

        @Override
        public void onNext(ArrayList<RestaurantListItem> restaurantListItems) {
            if (bottomProgressBar.getAdapterItemCount() != 0) {
                bottomProgressBar.clear();
            }
            restaurantAdapter.add(restaurantListItems);
        }
    }

    private class MyEndlessRecyclerOnScrollListener extends EndlessRecyclerOnScrollListener {

        MyEndlessRecyclerOnScrollListener() {
            super(ListActivity.this.bottomProgressBar);
        }

        @Override
        public void onLoadMore(final int currentPage) {
            boolean hasMore = resultsShown < 100 && resultsStart <= 80;
            if (hasMore) {
                bottomProgressBar.clear();
                bottomProgressBar.add(new ProgressItem().withEnabled(false));
                load(null);
            }
        }
    }
}

