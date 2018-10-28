package com.daracul.android.secondexercizeapp.network;

import android.support.annotation.NonNull;

import com.daracul.android.secondexercizeapp.data.ResultDTO;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NewsEndpoint {
    @GET("{section}.json")
    Single<Response<DefaultResponse<List<ResultDTO>>>> newsObject(@Path("section") @NonNull String section);

}
