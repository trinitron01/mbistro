package com.bfs.mbistro.di;

import com.bfs.mbistro.network.ApiService;
import com.bfs.mbistro.network.NetworkMonitor;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class BistroServiceModule {

    private static final int RESPONSE_CACHE_SIZE = 10 * 1024 * 1024;
    private final String baseUrl;
    private final File cacheDir;
    private final NetworkMonitor networkMonitor;
    private final boolean useCache = true;

    public BistroServiceModule(String baseUrl, File cacheDir, NetworkMonitor networkMonitor) {
        this.baseUrl = baseUrl;
        this.cacheDir = cacheDir;
        this.networkMonitor = networkMonitor;
    }

    @Provides
    @Singleton
    GsonConverterFactory provideGsonConverter() {
        return GsonConverterFactory.create(new GsonBuilder().create());
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {

        return new OkHttpClient.Builder().addNetworkInterceptor(new NetworkInterceptor())
            .cache(new Cache(cacheDir, RESPONSE_CACHE_SIZE))
            .addInterceptor(new OfflineInterceptor())
            .build();

    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(GsonConverterFactory gsonConverter, OkHttpClient okHttpClient) {
        return new Retrofit.Builder().addConverterFactory(gsonConverter).baseUrl(baseUrl).client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
    }

    @Provides
    @Singleton
    ApiService provideService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }

    private class NetworkInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();
            builder.addHeader("user-key", "b5ae9d15d056fbb3c6145958f14ad4d8").addHeader("Accept-Language", "pl,en-US;q=0.7,en;q=0.3");
            if (!networkMonitor.isOnline()) {
                // To be used with Application Interceptor to use Expired cache
                builder.cacheControl(CacheControl.FORCE_CACHE);
            }
            Response response = chain.proceed(builder.build());
            if (useCache) {
                return response.newBuilder().removeHeader("Pragma").removeHeader("Cache-Control")
                        .header("Cache-Control", "max-age=" + TimeUnit.MINUTES.toSeconds(60)).build();
            } else {
                return response;
            }

        }
    }

    private class OfflineInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();
            if (useCache && !networkMonitor.isOnline()) {
                // To be used with Application Interceptor to use Expired cache
                builder.cacheControl(CacheControl.FORCE_CACHE);
            }
            return chain.proceed(builder.build());
        }
    }
}
