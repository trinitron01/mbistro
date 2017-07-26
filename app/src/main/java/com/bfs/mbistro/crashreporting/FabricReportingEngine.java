package com.bfs.mbistro.crashreporting;

import android.content.Context;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class FabricReportingEngine implements CrashReportingEngine {

    private final Context context;

    public FabricReportingEngine(Context context) {
        this.context = context;
    }

    @Override
    public void registerCrashReporting() {
        Fabric.with(context, new Crashlytics());
    }

}
