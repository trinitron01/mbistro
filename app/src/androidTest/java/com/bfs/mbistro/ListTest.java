package com.bfs.mbistro;

import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import com.bfs.mbistro.module.restaurant.list.RestaurantsActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class) public class ListTest {

  @Rule public final SchedulerRule schedulerRule = new SchedulerRule();
  @Rule public ActivityTestRule<RestaurantsActivity> activityRule =
      new ActivityTestRule<>(RestaurantsActivity.class, false, false);
  @Rule public GrantPermissionRule grantPermissionRule =
      GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

  @Test public void listVisible() {
    activityRule.launchActivity(null);
    schedulerRule.getIdlingResource().waitForIdle();
    onView(withId(R.id.contentView)).check(matches(isDisplayed()));
  }

  @Test public void locationUpdateChangesTitle() throws Exception {
    activityRule.launchActivity(null);
    RestaurantsActivity activity = activityRule.getActivity();
    activity.onNewLocation(51.66111d, 22.66111d);
    schedulerRule.getIdlingResource().waitForIdle();
    assertEquals("Lublin", activity.getTitle());
  }
}
