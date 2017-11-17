package com.bfs.mbistro.module.restaurant.mvp;

import com.bfs.mbistro.model.DetailsReviewResponse;
import com.bfs.mbistro.network.ApiService;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public interface RestaurantDetailsContract {

  interface RestaurantDetailsView extends MvpLceView<DetailsReviewResponse> {

  }

  class Presenter extends MvpBasePresenter<RestaurantDetailsView> {

    private final ApiService service;
    private ResponseObserver observer;

    public Presenter(ApiService service) {
      this.service = service;
    }

    public void loadDetails(final String id) {
      getView().showLoading(false);
      dispose();
      observer = new ResponseObserver();

      service.getRestaurant(id)
          .flatMap(restaurantDetails -> service.getReviews(restaurantDetails.getId()),
              DetailsReviewResponse::new)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribeWith(observer);
    }

    @Override public void detachView(boolean retainInstance) {
      super.detachView(retainInstance);
      dispose();
    }

    private void dispose() {
      observer = null;
    }

    private class ResponseObserver extends DisposableObserver<DetailsReviewResponse> {

      @Override public void onError(Throwable error) {
        getView().showError(error, false);
      }

      @Override public void onComplete() {

      }

      @Override public void onNext(DetailsReviewResponse value) {
        getView().setData(value);
        getView().showContent();
      }
    }
  }
}
