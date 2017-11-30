package com.bfs.mbistro;

import android.content.Context;
import android.provider.Settings;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class) public class SystemAnimationToggleTest {

  @Before public void disableAnimations() throws IOException {
    Context appContext = InstrumentationRegistry.getTargetContext();
    String packageName = InstrumentationRegistry.getTargetContext().getPackageName();
    UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        .executeShellCommand("pm grant " + packageName + " android.permission.SET_ANIMATION_SCALE");

    UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        .executeShellCommand("settings put global window_animation_scale 0");
    UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        .executeShellCommand("settings put global transition_animation_scale 0");
    UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        .executeShellCommand("settings put global animator_duration_scale 0");

    float duration = Settings.Global.getFloat(appContext.getContentResolver(),
        Settings.Global.ANIMATOR_DURATION_SCALE, 1);
    float transition = Settings.Global.getFloat(appContext.getContentResolver(),
        Settings.Global.TRANSITION_ANIMATION_SCALE, 1);
    float windowAnimationScale = Settings.Global.getFloat(appContext.getContentResolver(),
        Settings.Global.WINDOW_ANIMATION_SCALE, 1);
    int value = 0;
    assertEquals(duration, value, 0);
    assertEquals(transition, value, 0);
    assertEquals(windowAnimationScale, value, 0);
  }

  @After public void enableAnimations() throws IOException {
/*    Context appContext = InstrumentationRegistry.getTargetContext();
    UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        .executeShellCommand("settings put global window_animation_scale 1");
    UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        .executeShellCommand("settings put global transition_animation_scale 1");
    UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        .executeShellCommand("settings put global animator_duration_scale 1");


    float duration = Settings.Global.getFloat(appContext.getContentResolver(),
        Settings.Global.ANIMATOR_DURATION_SCALE, 1);
    float transition = Settings.Global.getFloat(appContext.getContentResolver(),
        Settings.Global.TRANSITION_ANIMATION_SCALE, 1);
    float windowAnimationScale = Settings.Global.getFloat(appContext.getContentResolver(),
        Settings.Global.WINDOW_ANIMATION_SCALE, 1);
    int value = 1;
    assertEquals(duration, value, 0);
    assertEquals(transition, value, 0);
    assertEquals(windowAnimationScale, value, 0);*/
  }
}
