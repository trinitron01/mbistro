package com.bfs.mbistro;

import android.os.AsyncTask;
import io.reactivex.Scheduler;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class SchedulerRule implements TestRule {

  private final Scheduler asyncTaskScheduler = Schedulers.from(AsyncTask.SERIAL_EXECUTOR);

  private RxIdlingResource rxIdlingResource;

  @Override public Statement apply(final Statement base, Description d) {
    return new Statement() {
      @Override public void evaluate() throws Throwable {
        RxJavaPlugins.reset();

        rxIdlingResource = new RxIdlingResource();

        RxJavaPlugins.setScheduleHandler(rxIdlingResource);
        RxJavaPlugins.setIoSchedulerHandler(scheduler -> asyncTaskScheduler);
        RxJavaPlugins.setComputationSchedulerHandler(scheduler -> asyncTaskScheduler);
        RxJavaPlugins.setNewThreadSchedulerHandler(scheduler -> asyncTaskScheduler);

        try {
          base.evaluate();
        } finally {
          RxJavaPlugins.reset();
        }
      }
    };
  }

  public RxIdlingResource getIdlingResource() {
    return rxIdlingResource;
  }
}