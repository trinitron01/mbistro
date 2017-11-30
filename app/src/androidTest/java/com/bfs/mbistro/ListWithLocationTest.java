package com.bfs.mbistro;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.ContextCompat;
import android.test.ActivityInstrumentationTestCase2;
import com.bfs.mbistro.di.AppModule;
import com.bfs.mbistro.di.BistroComponent;
import com.bfs.mbistro.di.BistroServiceModule;
import com.bfs.mbistro.di.DaggerBistroComponent;
import com.bfs.mbistro.model.location.UserLocation;
import com.bfs.mbistro.model.location.UserLocationResponse;
import com.bfs.mbistro.module.restaurant.list.RestaurantsActivity;
import com.bfs.mbistro.network.ApiService;
import io.reactivex.Observable;
import java.io.File;
import java.net.HttpURLConnection;
import java.util.concurrent.TimeoutException;
import okhttp3.ResponseBody;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class) public class ListWithLocationTest {

  @Rule public final SchedulerRule asyncTaskSchedulerRule = new SchedulerRule();
  @Rule public final MockitoRule mockitoRule = MockitoJUnit.rule();
  /**
   * A JUnit {@link Rule @Rule} to launch your activity under test. This is a replacement
   * for {@link ActivityInstrumentationTestCase2}.
   * <p>
   * Rules are interceptors which are executed for each test method and will run before
   * any of your setup code in the {@link Before @Before} method.
   * <p>
   * {@link ActivityTestRule} will create and launch of the activity for you and also expose
   * the activity under test. To get a reference to the activity you can use
   * the {@link ActivityTestRule#getActivity()} method.
   */
  @Rule public ActivityTestRule<RestaurantsActivity> activityRule =
      new ActivityTestRule<>(RestaurantsActivity.class, false, false);
  @Rule public GrantPermissionRule grantPermissionRule =
      GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);
  @Mock ApiService apiServiceMock;

  public static BistroApp getAppFromInstrumentation() {
    return (BistroApp) InstrumentationRegistry.getInstrumentation()
        .getTargetContext()
        .getApplicationContext();
  }

  @Test public void permissionsGranted() throws Exception {
    // Context of the app under test.
    Context appContext = InstrumentationRegistry.getTargetContext();

    int permissionState =
        ContextCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_FINE_LOCATION);
    assertEquals(permissionState, PackageManager.PERMISSION_GRANTED);
    activityRule.launchActivity(null);
    assertNotNull(activityRule.getActivity());
  }

  @Test public void geocodeExceptionShowsError() {

    Context context =
        InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
    File externalCacheDir = context.getExternalCacheDir();
    File cacheDir =
        externalCacheDir != null ? externalCacheDir : context.getCacheDir();//todo uwspólnić
    BistroComponent component =
        DaggerBistroComponent.builder().bistroServiceModule(new BistroServiceModule(cacheDir) {
          @Override protected ApiService provideService(Retrofit retrofit) {
            return apiServiceMock;
          }
        }).appModule(new AppModule(context)).build();
    getAppFromInstrumentation().setComponent(component);

    when(apiServiceMock.geocode(anyDouble(), anyDouble())).thenReturn(
        Observable.error(new TimeoutException()));

    activityRule.launchActivity(null);
    onView(withId(R.id.errorView)).check(matches(isDisplayed()));
    onView(withId(R.id.contentView)).check(matches(not(isDisplayed())));
    onView(withId(R.id.loadingView)).check(matches(not(isDisplayed())));
  }

  @Test public void restaurantsExceptionShowsError() {
    UserLocationResponse item = new UserLocationResponse();
    UserLocation userLocation = new UserLocation();
    userLocation.setCityId(1);
    item.setLocation(userLocation);

    when(apiServiceMock.geocode(anyDouble(), anyDouble())).thenReturn(Observable.just(item));
    when(apiServiceMock.getRestaurants(anyInt(), anyString(), anyInt(), anyInt())).thenReturn(
        Observable.error(new TimeoutException()));

    Context context =
        InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
    File externalCacheDir = context.getExternalCacheDir();
    File cacheDir =
        externalCacheDir != null ? externalCacheDir : context.getCacheDir();//todo uwspólnić
    BistroComponent component =
        DaggerBistroComponent.builder().bistroServiceModule(new BistroServiceModule(cacheDir) {
          @Override protected ApiService provideService(Retrofit retrofit) {
            return apiServiceMock;
          }
        }).appModule(new AppModule(context)).build();
    getAppFromInstrumentation().setComponent(component);
    activityRule.launchActivity(null);
    onView(withId(R.id.errorView)).check(matches(isDisplayed()));
    onView(withId(R.id.contentView)).check(matches(not(isDisplayed())));
    onView(withId(R.id.loadingView)).check(matches(not(isDisplayed())));
  }

  @Test public void notFoundExceptionShowsEmptyView() {

    UserLocationResponse item = new UserLocationResponse();
    UserLocation userLocation = new UserLocation();
    userLocation.setCityId(1);
    item.setLocation(userLocation);

    when(apiServiceMock.geocode(anyDouble(), anyDouble())).thenReturn(Observable.just(item));
    HttpException httpException = new HttpException(
        Response.error(HttpURLConnection.HTTP_NOT_FOUND, mock(ResponseBody.class)));
    when(apiServiceMock.getRestaurants(anyInt(), anyString(), anyInt(), anyInt())).thenReturn(
        Observable.error(httpException));

    Context context =
        InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
    File externalCacheDir = context.getExternalCacheDir();
    File cacheDir =
        externalCacheDir != null ? externalCacheDir : context.getCacheDir();//todo uwspólnić
    BistroComponent component =
        DaggerBistroComponent.builder().bistroServiceModule(new BistroServiceModule(cacheDir) {
          @Override protected ApiService provideService(Retrofit retrofit) {
            return apiServiceMock;
          }
        }).appModule(new AppModule(context)).build();
    getAppFromInstrumentation().setComponent(component);
    activityRule.launchActivity(null);
    onView(withId(R.id.emptyView)).check(matches(isDisplayed()));
    onView(withId(R.id.contentView)).check(matches(not(isDisplayed())));
    onView(withId(R.id.errorView)).check(matches(not(isDisplayed())));
    onView(withId(R.id.loadingView)).check(matches(not(isDisplayed())));
  }
}
