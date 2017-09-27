package com.bfs.mbistro.base.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import com.bfs.mbistro.BistroApp;
import com.bfs.mbistro.R;
import com.bfs.mbistro.di.BistroComponent;
import com.bfs.mbistro.network.ApiService;
import com.bfs.mbistro.network.NetworkMonitor;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.lce.LceAnimator;
import com.hannesdorfmann.mosby3.mvp.lce.MvpLceFragment;
import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView;
import javax.inject.Inject;

public abstract class BaseMvpFragment<CV extends View, M, V extends MvpLceView<M>, P extends MvpPresenter<V>>
    extends MvpLceFragment<CV, M, V, P> {

  @Inject protected ApiService service;
  @Inject protected NetworkMonitor networkMonitor;
  private View emptyView;

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    inject(((BistroApp) activity.getApplication()).getComponent());
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    emptyView = view.findViewById(R.id.emptyView);
  }

  protected abstract void inject(BistroComponent component);

  @Override protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
    return getString(networkMonitor.isOnline() ? R.string.download_error_click_to_refresh
        : R.string.internet_connection_unavailable_error_click_to_retry);
  }

  protected final void animateEmptyViewIn() {
    if (emptyView != null) {
      LceAnimator.showErrorView(loadingView, contentView, emptyView);
    }
  }

  @Override public void showLoading(boolean pullToRefresh) {
    super.showLoading(pullToRefresh);
    if (!pullToRefresh && emptyView != null) {
      emptyView.setVisibility(View.GONE);
    }
  }

  @Override public void showError(Throwable e, boolean pullToRefresh) {
    super.showError(e, pullToRefresh);
    if (!pullToRefresh && emptyView != null) {
      emptyView.setVisibility(View.GONE);
    }
  }
}
