package com.bfs.mbistro.di;

import com.bfs.mbistro.BuildConfig;
import com.bfs.mbistro.network.ApiService;
import com.bfs.mbistro.network.NetworkMonitor;
import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;
import com.squareup.moshi.Moshi;
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
import okhttp3.internal.platform.Platform;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module(includes = AppModule.class) public class BistroServiceModule {

  private static final int CONNECTION_TIME_OUT = 120000;
  private static final int READ_TIME_OUT = 120000;
  private static final int RESPONSE_CACHE_SIZE = 10 * 1024 * 1024;
  private static final String REQUEST_LOG_TAG = "Request";
  private static final String RESPONSE_LOG_TAG = "Response";
  private static final String HEADER_LOG_VERSION_TAG = "version";
  private final String baseUrl;
  private final File cacheDir;
  private final boolean useCache = true;
  public BistroServiceModule(String baseUrl, File cacheDir) {
    this.baseUrl = baseUrl;
    this.cacheDir = cacheDir;
  }

  @Provides @Singleton Converter.Factory provideConverterFactory() {
    MoshiConverterFactory moshiConverterFactory =
        MoshiConverterFactory.create(new Moshi.Builder().build());
    return moshiConverterFactory;
  }

  @Provides @Singleton OkHttpClient provideOkHttpClient(NetworkMonitor networkMonitor) {

    return new OkHttpClient.Builder().cache(new Cache(cacheDir, RESPONSE_CACHE_SIZE))
        .addInterceptor(new OfflineInterceptor(networkMonitor))
        .addNetworkInterceptor(new NetworkInterceptor(networkMonitor))
        .addInterceptor(getResponseLogInterceptor())
        .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
        .connectTimeout(CONNECTION_TIME_OUT, TimeUnit.MILLISECONDS)
        .build();
  }

  private LoggingInterceptor getResponseLogInterceptor() {
    return new LoggingInterceptor.Builder().loggable(BuildConfig.DEBUG)
        .setLevel(Level.BASIC)
        .log(Platform.INFO)
        .request(REQUEST_LOG_TAG)
        .response(RESPONSE_LOG_TAG)
        .addHeader(HEADER_LOG_VERSION_TAG, BuildConfig.VERSION_NAME)
        .build();
  }

  @Provides @Singleton Retrofit provideRetrofit(Converter.Factory converterFactory,
      OkHttpClient okHttpClient) {
    return new Retrofit.Builder().addConverterFactory(converterFactory)
        .baseUrl(baseUrl)
        .client(okHttpClient).addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build();
  }

  @Provides @Singleton ApiService provideService(Retrofit retrofit) {
    return retrofit.create(ApiService.class);
  }

  private class NetworkInterceptor implements Interceptor {

    private static final int RESPONSE_CACHE_TIME_MINUTES = 60;
    private static final String USER_KEY_CALUE = "b5ae9d15d056fbb3c6145958f14ad4d8";
    private final NetworkMonitor networkMonitor;

    NetworkInterceptor(NetworkMonitor networkMonitor) {
      this.networkMonitor = networkMonitor;
    }

    @Override public Response intercept(Chain chain) throws IOException {
      Request request = chain.request();
      Request.Builder builder = request.newBuilder();
      builder.addHeader("user-key", USER_KEY_CALUE)
          .addHeader("Accept-Language", "pl,en-US;q=0.7,en;q=0.3");
      if (!networkMonitor.isOnline()) {
        // To be used with Application Interceptor to use Expired cache
        builder.cacheControl(CacheControl.FORCE_CACHE);
      }
      Response response = chain.proceed(builder.build());
      if (useCache) {
        return response.newBuilder()
            .removeHeader("Pragma")
            .removeHeader("Cache-Control")
            .header("Cache-Control",
                "max-age=" + TimeUnit.MINUTES.toSeconds(RESPONSE_CACHE_TIME_MINUTES))
            .build();
      } else {
        return response;
      }
    }
  }

  private class OfflineInterceptor implements Interceptor {

    private final NetworkMonitor networkMonitor;

    OfflineInterceptor(NetworkMonitor networkMonitor) {

      this.networkMonitor = networkMonitor;
    }

    @Override public Response intercept(Chain chain) throws IOException {
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
