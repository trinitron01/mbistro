package com.bfs.mbistro.restaurant;

import com.bfs.mbistro.RxSchedulersOverrideRule;
import com.bfs.mbistro.model.DetailsReviewResponse;
import com.bfs.mbistro.model.RestaurantDetails;
import com.bfs.mbistro.model.ReviewsResponse;
import com.bfs.mbistro.module.restaurant.mvp.RestaurantDetailsContract;
import com.bfs.mbistro.network.ApiService;
import io.reactivex.Observable;
import java.util.concurrent.TimeoutException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class) public class RestaurantDetailsTest {

  @Rule public final RxSchedulersOverrideRule overrideSchedulersRule =
      new RxSchedulersOverrideRule();
  @Mock private ApiService apiService;
  @Mock private RestaurantDetailsContract.RestaurantDetailsView detailsView;

  @Test public void testLoadedDetailsShowLoadingAndContent() {
    //anyString() does not accept null but Mockito.<String>any() will
    RestaurantDetails detailsResponse = new RestaurantDetails();
    when(apiService.getRestaurant(anyString())).thenReturn(Observable.just(detailsResponse));
    ReviewsResponse reviewsResponse = new ReviewsResponse();
    when(apiService.getReviews(ArgumentMatchers.any())).thenReturn(
        Observable.just(reviewsResponse));

    RestaurantDetailsContract.Presenter presenter =
        new RestaurantDetailsContract.Presenter(apiService);
    presenter.attachView(detailsView);
    presenter.loadDetails("1");

    verify(detailsView, never()).showError(any(), anyBoolean());
    verify(detailsView, times(1)).showLoading(false);
    verify(detailsView, times(1)).showContent();
  }

  @Test public void testLoadedDetailsAndReviewsCombinedUnchanged() {
    //anyString() does not accept null but Mockito.<String>any() will
    RestaurantDetails detailsResponse = new RestaurantDetails();
    when(apiService.getRestaurant(anyString())).thenReturn(Observable.just(detailsResponse));
    ReviewsResponse reviewsResponse = new ReviewsResponse();
    when(apiService.getReviews(ArgumentMatchers.any())).thenReturn(
        Observable.just(reviewsResponse));

    RestaurantDetailsContract.Presenter presenter =
        new RestaurantDetailsContract.Presenter(apiService);
    presenter.attachView(detailsView);
    presenter.loadDetails("1");

    ArgumentCaptor<DetailsReviewResponse> argument =
        ArgumentCaptor.forClass(DetailsReviewResponse.class);
    verify(detailsView).setData(argument.capture());
    DetailsReviewResponse value = argument.getValue();
    assertEquals(value.restaurant, detailsResponse);
    assertEquals(value.reviewsResponse, reviewsResponse);
  }

  @Test public void testViewShowsErrorOnException() {
    TimeoutException exception = new TimeoutException();
    when(apiService.getRestaurant(anyString())).thenReturn(Observable.error(exception));

    RestaurantDetailsContract.Presenter presenter =
        new RestaurantDetailsContract.Presenter(apiService);
    presenter.attachView(detailsView);
    presenter.loadDetails("1");

    verify(detailsView, times(1)).showLoading(false);
    verify(detailsView, never()).showContent();
    verify(detailsView, times(1)).showError(exception, false);
  }
}
