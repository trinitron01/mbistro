package com.bfs.mbistro.analytics;

import android.content.Context;
import android.util.Pair;
import java.util.List;

public interface AnalyticsTracker {

  void attach(Context context);

  void detach();

  void traceEvent(String eventName, List<Pair<String, String>> values);
}
