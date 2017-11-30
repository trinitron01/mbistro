package com.bfs.mbistro;

import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.Callable;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * This rule registers Handlers for RxJava and RxAndroid to ensure that subscriptions
 * always subscribeOn and observeOn Schedulers.trampoline().
 * Warning, this rule will reset RxAndroidPlugins and RxJavaPlugins before and after each test so
 * if the application code uses RxJava plugins this may affect the behaviour of the testing method.
 */
public class RxSchedulersOverrideRule implements TestRule {

  private final Function<Callable<Scheduler>, Scheduler> mRxAndroidSchedulersHook =
      schedulerCallable -> trampolineScheduler();

  private final Function<Scheduler, Scheduler> mRxJavaImmediateScheduler =
      scheduler -> trampolineScheduler();

  private static Scheduler trampolineScheduler() {
    return Schedulers.trampoline();
  }

  @Override public Statement apply(final Statement base, Description description) {
    return new Statement() {
      @Override public void evaluate() throws Throwable {
        RxAndroidPlugins.reset();
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(mRxAndroidSchedulersHook);

        RxJavaPlugins.reset();
        RxJavaPlugins.setIoSchedulerHandler(mRxJavaImmediateScheduler);
        RxJavaPlugins.setNewThreadSchedulerHandler(mRxJavaImmediateScheduler);

        base.evaluate();

        RxAndroidPlugins.reset();
        RxJavaPlugins.reset();
      }
    };
  }
}

