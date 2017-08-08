package com.bfs.mbistro.module.restaurant.mvp;

import com.bfs.mbistro.model.DetailsReviewResponse;
import com.bfs.mbistro.model.RestaurantDetails;
import com.bfs.mbistro.model.ReviewsResponse;
import com.bfs.mbistro.network.ApiService;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public interface RestaurantDetailsContract {

  interface RestaurantDetailsView extends MvpLceView<DetailsReviewResponse> {

  }

  class Presenter extends MvpBasePresenter<RestaurantDetailsView> {

    private final ApiService service;
    private Subscriber<DetailsReviewResponse> subscriber;

    public Presenter(ApiService service) {
      this.service = service;
    }

    public void loadDetails(final String id) {
      getView().showLoading(false);
      unsubscribe();
      subscriber = new ResponseSubscriber();

      Observable<RestaurantDetails> restaurantSource = service.getRestaurant(id);
      restaurantSource.flatMap(new Func1<RestaurantDetails, Observable<ReviewsResponse>>() {
        @Override public Observable<ReviewsResponse> call(RestaurantDetails restaurantDetails) {
          return service.getReviews(restaurantDetails.getId());
        }
      }, new Func2<RestaurantDetails, ReviewsResponse, DetailsReviewResponse>() {
        @Override public DetailsReviewResponse call(RestaurantDetails restaurantDetails,
            ReviewsResponse reviewsResponse) {
          return new DetailsReviewResponse(restaurantDetails, reviewsResponse);
        }
      })
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(subscriber);
    }

    @Override public void detachView(boolean retainInstance) {
      super.detachView(retainInstance);
      unsubscribe();
    }

    private void unsubscribe() {
      if (subscriber != null && !subscriber.isUnsubscribed()) {
        subscriber.unsubscribe();
      }
      subscriber = null;
    }

    private class ResponseSubscriber extends Subscriber<DetailsReviewResponse> {

      @Override public void onCompleted() {

      }

      @Override public void onError(Throwable error) {
        getView().showError(error, false);
      }

      @Override public void onNext(DetailsReviewResponse value) {
        getView().setData(value);
        getView().showContent();
      }
    }
  }
}
