package com.peleg.tweetslibrary.rest;

import android.content.SharedPreferences;
import android.util.Base64;

import java.io.IOException;
import okhttp3.Interceptor;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by hannypeleg on 5/17/17.
 */

public class TweetsApi {

    public static final String BASE_URL = "https://api.twitter.com/";
    private static Retrofit retrofit = null;

    private static String authValue;

    private static OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
        @Override
        public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
            Request originalRequest = chain.request();


            Request.Builder builder = originalRequest.newBuilder()
                    .header("Authorization",authValue);
            Request newRequest = builder.build();
            return chain.proceed(newRequest);
        }
    }).build();

    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

    public static void setAuthValue(String value) {
        authValue = value;
    }



}
