package com.daracul.android.secondexercizeapp.sync;

import com.daracul.android.secondexercizeapp.database.Db;
import com.daracul.android.secondexercizeapp.database.News;
import com.daracul.android.secondexercizeapp.model.NewsMapper;
import com.daracul.android.secondexercizeapp.model.ResultDTO;
import com.daracul.android.secondexercizeapp.network.DefaultResponse;
import com.daracul.android.secondexercizeapp.network.RestApi;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class DownloadingNews {
    public static Single<List<News>> updateNews(final Db db, String category){
        return RestApi.getInstance()
                .news()
                .newsObject(category)
                .subscribeOn(Schedulers.io())
                .map(new Function<Response<DefaultResponse<List<ResultDTO>>>, List<News>>() {
                    @Override
                    public List<News> apply(Response<DefaultResponse<List<ResultDTO>>> defaultResponseResponse) throws Exception {
                        return NewsMapper.convertDTOListToNewsItem(defaultResponseResponse);
                    }
                })
                .flatMap(new Function<List<News>, SingleSource<? extends List<News>>>() {
                    @Override
                    public SingleSource<? extends List<News>> apply(List<News> newsList) throws Exception {
                        return db.saveNews(newsList);
                    }
                });
    }
}
