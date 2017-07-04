package com.bfs.mbistro.di;

import com.bfs.mbistro.base.BaseActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {BistroServiceModule.class, AppModule.class})
public interface BistroComponent {

    void inject(BaseActivity activity);

}
