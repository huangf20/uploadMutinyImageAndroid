package com.example.statebartest;

import android.os.UserManager;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

@SuppressWarnings("FieldCanBeLocal")
public class RetrofitManager {
    //读超时时长，单位：毫秒
    public static final int READ_TIME_OUT = 15 * 1000;
    //写超时时长，单位：毫秒
    public static final int WRITE_TIME_OUT = 15 * 1000;
    //连接超时时长，单位：毫秒
    public static final int CONNECT_TIME_OUT = 15 * 1000;

    private static Retrofit mRetrofit; //Retrofit对象
    private static ApiService mApiService; //接口类实例对象
    private static OkHttpClient mClient; //OKHttp对象
    public static ApiService getInstance() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.MILLISECONDS)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .addInterceptor(getLoggerInterceptor());

        builder.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("Cookie", "sid=" + "sssssssd")
                        .addHeader("Content-Type", "text/html;charset:utf-8")
                        .build();
                return chain.proceed(request);
            }
        });
        mClient=builder.build();
        //配置Retrofit
        mRetrofit = new Retrofit.Builder()
                .baseUrl(ApiService.baseUrl)
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(mClient)
                .build();
        //创建ApiService对象
        mApiService = mRetrofit.create(ApiService.class);
        return mApiService;
    }

    //设置日志拦截器，打印返回的数据
    private static HttpLoggingInterceptor getLoggerInterceptor() {
        //日志显示级别
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NonNull String message) {
                Log.e("ApiUrl------>", message);
            }
        });
        loggingInterceptor.setLevel(level);
        return loggingInterceptor;
    }

}
