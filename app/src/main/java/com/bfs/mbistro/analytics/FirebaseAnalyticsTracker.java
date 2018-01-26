package com.bfs.mbistro.analytics;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Pair;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.List;

public class FirebaseAnalyticsTracker implements AnalyticsTracker {

  private FirebaseAnalytics firebaseAnalytics;

  public FirebaseAnalyticsTracker() {

  }

  @Override public void attach(Context context) {
    firebaseAnalytics = FirebaseAnalytics.getInstance(context);
  }

  @Override public void detach() {

  }

  @Override public void traceEvent(String eventName, @NonNull List<Pair<String, String>> values) {
    Bundle bundle = new Bundle();
    for (Pair<String, String> pair : values) {
      bundle.putString(pair.first, pair.second);
    }
    firebaseAnalytics.logEvent(eventName, bundle);
  }
}
