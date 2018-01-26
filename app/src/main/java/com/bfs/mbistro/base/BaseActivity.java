package com.bfs.mbistro.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.bfs.mbistro.BistroApp;
import com.bfs.mbistro.analytics.AnalyticsTracker;
import com.bfs.mbistro.crashreporting.CrashReportingEngine;
import com.bfs.mbistro.di.BistroComponent;
import com.bfs.mbistro.network.ApiService;
import javax.inject.Inject;

public abstract class BaseActivity extends AppCompatActivity {

  @Inject protected ApiService service;
  @Inject protected CrashReportingEngine crashReportingEngine;
  @Inject protected AnalyticsTracker analyticsTracker;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    inject(((BistroApp) getApplication()).getComponent());
    crashReportingEngine.registerCrashReporting();
  }

  @Override protected void onResume() {
    super.onResume();
    analyticsTracker.attach(this);
  }

  @Override protected void onPause() {
    super.onPause();
    analyticsTracker.detach();
  }

  protected abstract void inject(BistroComponent component);

  protected <T extends View> T findView(@IdRes int id) {
    //noinspection unchecked
    return (T) findViewById(id);
  }

  protected View getSnackbarContainer() {
    return findViewById(android.R.id.content);
  }

  protected final void showSnackbar(Activity activity, final int mainTextStringId,
      final int actionStringId, View.OnClickListener listener) {
    Snackbar.make(getSnackbarContainer(), activity.getString(mainTextStringId),
        Snackbar.LENGTH_INDEFINITE).setAction(activity.getString(actionStringId), listener).show();
  }

  /**
   * Shows a {@link Snackbar} using {@code text}.
   *
   * @param text The Snackbar text.
   */
  protected final void showSnackbar(final CharSequence text) {
    View container = getSnackbarContainer();
    if (container != null) {
      Snackbar snackbar = Snackbar.make(container, text, Snackbar.LENGTH_LONG);
      snackbar.show();
    }
  }
}
