package com.bfs.mbistro.module.restaurant.details.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bfs.mbistro.AndroidUtils;
import com.bfs.mbistro.BistroApp;
import com.bfs.mbistro.Intents;
import com.bfs.mbistro.R;
import com.bfs.mbistro.model.DetailsReviewResponse;
import com.bfs.mbistro.model.RestaurantLocation;
import com.bfs.mbistro.model.UserRating;
import com.bfs.mbistro.module.restaurant.mvp.RestaurantDetailsContract;
import com.bfs.mbistro.module.restaurant.rating.RestaurantRatingView;
import com.bfs.mbistro.network.ApiService;
import com.bfs.mbistro.network.NetworkMonitor;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.MvpLceViewStateFragment;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.data.ParcelableDataLceViewState;
import javax.inject.Inject;

public class RestaurantDetailsFragment extends
    MvpLceViewStateFragment<TextView, DetailsReviewResponse, RestaurantDetailsContract.RestaurantDetailsView, RestaurantDetailsContract.Presenter>
    implements RestaurantDetailsContract.RestaurantDetailsView, View.OnClickListener,
    Intents.ComponentUnavailableListener {

  private static final String ID_KEY = "ID_KEY";
  private static final String NAME_KEY = "NAME_KEY";
  private static final String DISTANCE_KEY = "DISTANCE_KEY";
  @Inject protected ApiService service;
  @Inject protected NetworkMonitor networkMonitor;
  private RestaurantDetailsContract.Presenter detailsPresenter;
  private TextView distance;
  private TextView cuisinesLabel;
  private TextView googlePlacesInfo;
  private FloatingActionButton navigationFab;
  private RestaurantRatingView ratingView;
  private RestaurantRatingView googleRatingView;
  private String restaurantName;
  private DetailsReviewResponse data;
  private GoogleApiClient googleApiClient;
  private CharSequence placeName;

  public static RestaurantDetailsFragment newInstance(@NonNull String restaurantId,
      @NonNull String name, float distance) {
    Bundle args = new Bundle();
    args.putString(ID_KEY, restaurantId);
    args.putString(NAME_KEY, name);
    args.putFloat(DISTANCE_KEY, distance);
    RestaurantDetailsFragment fragment = new RestaurantDetailsFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    ((BistroApp) activity.getApplication()).getComponent().inject(this);

    googleApiClient = new GoogleApiClient.Builder(getContext()).addApi(Places.PLACE_DETECTION_API)
        .addApi(Places.GEO_DATA_API)
        .enableAutoManage(getAppCompatActivity(), new GoogleApiClient.OnConnectionFailedListener() {
          @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            showError(null, false);
          }
        })
        .build();
  }

  @Override public RestaurantDetailsContract.Presenter createPresenter() {
    detailsPresenter = new RestaurantDetailsContract.Presenter(service);
    return detailsPresenter;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_restaurant_details, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    presenter.attachView(this);
    distance = (TextView) view.findViewById(R.id.restaurant_distance);
    cuisinesLabel = (TextView) view.findViewById(R.id.restaurant_cuisines);
    ratingView = (RestaurantRatingView) view.findViewById(R.id.details_rating_view);
    googlePlacesInfo = (TextView) view.findViewById(R.id.google_place_info);
    googlePlacesInfo.setOnClickListener(this);
    googleRatingView = (RestaurantRatingView) view.findViewById(R.id.google_rating_view);
    final Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    getAppCompatActivity().setSupportActionBar(toolbar);
    restaurantName = getArguments().getString(NAME_KEY);
    getAppCompatActivity().setTitle(restaurantName);
    navigationFab = (FloatingActionButton) view.findViewById(R.id.navigation_fab);
    navigationFab.setOnClickListener(this);
  }

  public AppCompatActivity getAppCompatActivity() {
    return (AppCompatActivity) getActivity();
  }

  @Override public void onDestroyView() {
    presenter.detachView(false);
    distance = null;
    cuisinesLabel = null;
    navigationFab = null;
    ratingView = null;
    googleRatingView = null;
    googlePlacesInfo = null;
    super.onDestroyView();
  }

  @Override public void loadData(boolean pullToRefresh) {
    presenter.loadDetails(getArguments().getString(ID_KEY));
  }

  @Override protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
    return getString(networkMonitor.isOnline() ? R.string.download_error_click_to_refresh
        : R.string.internet_connection_unavailable_error_click_to_retry);
  }

  @Override public DetailsReviewResponse getData() {
    return data;
  }

  @Override public void setData(DetailsReviewResponse data) {
    this.data = data;
    final float distanceInMeters = getArguments().getFloat(DISTANCE_KEY);
    if (distanceInMeters > 0) {
      distance.setText(getString(R.string.distance_subtitle_pattern, distanceInMeters / 1000));
    }
    cuisinesLabel.setText(data.restaurant.getCuisines());
    RestaurantLocation location = data.restaurant.getLocation();
    if (location != null) {
      navigationFab.setVisibility(View.VISIBLE);
    }
    UserRating rating = data.restaurant.getUserRating();
    if (rating != null) {
      ratingView.setRatingValue(rating.getVotes(), rating.getAggregateRating(),
          rating.getRatingColor());
    } else {
      ratingView.setVisibility(View.GONE);
    }

    double latitude = location.getLatitude();
    double longitude = location.getLongitude();
    AutocompleteFilter filter =
        new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT)
            .build();

    LatLngBounds bounds = new LatLngBounds(new LatLng(latitude - 0.1, longitude - 0.1),
        new LatLng(latitude + 0.1, longitude + 0.1

        ));
    String placeWithCityQuery =
        data.restaurant.getName() + ' ' + data.restaurant.getLocation().getCity();
    PendingResult<AutocompletePredictionBuffer> result =
        Places.GeoDataApi.getAutocompletePredictions(googleApiClient, placeWithCityQuery, bounds,
            filter);
    result.setResultCallback(new ResultCallback<AutocompletePredictionBuffer>() {
      @Override
      public void onResult(@NonNull AutocompletePredictionBuffer autocompletePredictions) {
        boolean googlePlaceFound = autocompletePredictions.getCount() > 0;
        googleRatingView.setVisibility(googlePlaceFound ? View.VISIBLE : View.GONE);
        if (googlePlaceFound) {
          AutocompletePrediction autocompletePrediction = autocompletePredictions.get(0);
          String placeId = autocompletePrediction.getPlaceId();
          PendingResult<PlaceBuffer> pendingResult =
              Places.GeoDataApi.getPlaceById(googleApiClient, placeId);
          pendingResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
            @Override public void onResult(@NonNull PlaceBuffer places) {
              if (places.getStatus().isSuccess() && places.getCount() > 0) {
                Place place = places.get(0);
                googleRatingView.setRatingValue(place.getRating());
                placeName = place.getName();
                googlePlacesInfo.setText(placeName
                    + "\n"
                    + place.getAddress()
                    + '\n'
                    + place.getPhoneNumber()
                    + "\n"
                    + place.getWebsiteUri()
                    + "\n");
              }
              places.release();
            }
          });
        }
        autocompletePredictions.release();
      }
    });
  }

  @Override public void onClick(View v) {
    if (v.getId() == R.id.navigation_fab) {
      Intents.showOnMap(getContext(), data.restaurant.getLocation().getLatitude(),
          data.restaurant.getLocation().getLongitude(), restaurantName, this);
    } else if (v.getId() == R.id.google_place_info) {
      Intents.showOnMap(getActivity(), placeName, null);
    }
  }

  @Override public void onComponentUnavailable(Intent unsupportedIntent) {
    AndroidUtils.showShortSnackbar(getActivity(), R.string.no_navigation_intent_map_available);
  }

  @NonNull @Override
  public LceViewState<DetailsReviewResponse, RestaurantDetailsContract.RestaurantDetailsView> createViewState() {
    return new ParcelableDataLceViewState<>();
  }
}
