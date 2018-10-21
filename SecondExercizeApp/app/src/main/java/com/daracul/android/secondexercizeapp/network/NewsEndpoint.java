package com.daracul.android.secondexercizeapp.network;

import android.support.annotation.NonNull;

import com.daracul.android.secondexercizeapp.data.HomeDTO;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NewsEndpoint {
    @GET("{section}.json")
    Single<HomeDTO> newsObject(@Path("section") @NonNull String section, @Query("api_key") @NonNull String api);

}
