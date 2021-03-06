package com.joy.phomel.common.network;

import android.content.Context;
import android.util.Log;

import com.joy.phomel.common.utils.NetworkUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 初始化网络请求管理
 * 网络请求管理
 */

public class RetrofitManager {
    private static OkHttpClient mOkHttpClient;
    private static final String TAG = "OkHttp";
    private Context mContext;
    public static RetrofitManager instance;

    //短缓存1分钟
    public static final int CACHE_AGE_SHORT = 60;
    //长缓存有效期为1天
    public static final int CACHE_STALE_LONG = 60 * 60 * 24;
    //自己的base_url
    public static final String BASE_URL = "http://www.baidu.com";
    private final Retrofit mRetrofit;

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public static RetrofitManager getInstance() {
        return instance;
    }

    private RetrofitManager(Context context) {
        mContext = context;
        initOkHttpClient();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void initOkHttpClient() {
        //日志拦截，使用okhttputils的HttpLoggingInterceptor，设置请求级别
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e(TAG, message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //缓存的cookieJar负责管理cookies
        CookieJarImpl cookieJar = new CookieJarImpl(mContext);
        //单例
        if (mOkHttpClient == null) {
            synchronized (OkHttpClient.class) {
                if (mOkHttpClient == null) {
                    // 指定缓存路径,缓存大小10Mb
                    Cache cache = new Cache(new File(mContext.getCacheDir(), "HttpCache"),
                            1024 * 1024 * 10);
                    mOkHttpClient = new OkHttpClient.Builder()
                            .cache(cache)
                            .addInterceptor(interceptor)
                            .addNetworkInterceptor(mCacheControlInterceptor)
                            .cookieJar(cookieJar)
                            .retryOnConnectionFailure(true)
                            .connectTimeout(60, TimeUnit.SECONDS)
                            .readTimeout(60, TimeUnit.SECONDS)
                            .writeTimeout(60, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
    }

    /**
     * 初始化RetrofitManager,最初在application调用一次
     * @param context
     */
    public static void init(Context context) {
        if (instance == null) {
            instance = new RetrofitManager(context);
        }
    }

    // 响应头拦截器，用来配置缓存策略
    private Interceptor mCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetworkUtils.isHaveNetWork) {
                //没网时只使用缓存
                //自定义请求头，可以在响应头对请求头的header进行拦截，配置不同的缓存策略
                request = request.newBuilder()
                        .header("head-request", request.toString())
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response response = chain.proceed(request);
            if (NetworkUtils.isHaveNetWork) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                Log.e("Interceptor", "response: " + response.toString());
                //添加头信息，配置Cache-Control
                //removeHeader("Pragma") 使缓存生效
                return response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + CACHE_AGE_SHORT)
                        .removeHeader("Pragma")
                        .build();
            } else {
                Log.e("Interceptor", "net not connect");
                return response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_LONG)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };

}
