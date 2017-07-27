package com.bfs.mbistro.module.restaurant.mvp;

import com.bfs.mbistro.model.RestaurantDetails;
import com.bfs.mbistro.network.ApiService;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public interface RestaurantDetailsContract {

  interface RestaurantDetailsView extends MvpLceView<RestaurantDetails> {

  }

  class RestaurantDetailsPresenter extends MvpBasePresenter<RestaurantDetailsView> {

    private final ApiService service;
    private SingleSubscriber<RestaurantDetails> subscriber;

    public RestaurantDetailsPresenter(ApiService service) {
      this.service = service;
    }

    public void loadDetails(String id) {
      getView().showLoading(false);
      unsubscribe();
      subscriber = new SingleSubscriber<RestaurantDetails>() {
        @Override public void onSuccess(RestaurantDetails value) {
          getView().setData(value);
          getView().showContent();
        }

        @Override public void onError(Throwable error) {
          getView().showError(error, false);
        }
      };
      service.getRestaurant(id, "pl")
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
  }
}
