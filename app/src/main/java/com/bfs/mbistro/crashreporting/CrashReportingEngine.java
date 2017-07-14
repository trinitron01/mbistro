package com.bfs.mbistro.crashreporting;

public interface CrashReportingEngine {

    void registerCrashReporting();

  void logError(Throwable error);
}
