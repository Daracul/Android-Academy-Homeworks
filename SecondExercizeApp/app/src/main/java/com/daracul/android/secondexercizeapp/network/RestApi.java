package com.daracul.android.secondexercizeapp.network;

import android.support.annotation.NonNull;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RestApi {

    private static final String URL = "https://api.nytimes.com/svc/topstories/v2/";
    public static final String API_KEY = "a1a6c2e7986e4279a9b01de45e7f19db";

    private static final int TIMEOUT_IN_SECONDS = 2;
    private static RestApi sRestApi;

    private final NewsEndpoint newsEndpoint;


    public static synchronized RestApi getInstance() {
        if (sRestApi == null) {
            sRestApi = new RestApi();
        }
        return sRestApi;
    }


    private RestApi() {
        final Retrofit retrofit = buildRetrofitClient();

        //init endpoints here. It's can be more then one endpoint
        newsEndpoint = retrofit.create(NewsEndpoint.class);
    }

    @NonNull
    private Retrofit buildRetrofitClient() {
        return new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public NewsEndpoint news() {
        return newsEndpoint;
    }

}
