package com.daracul.android.secondexercizeapp.database;

import android.content.Context;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class Db {

    private final Context context;

    public Db(Context context) {
        this.context = context;
    }

    public Completable saveNews(final List<News> newsList){
        return Completable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                AppDataBase dataBase = AppDataBase.getAppDatabase(context);
                dataBase.newsDao().deleteAll();

                News[] news = newsList.toArray(new News[newsList.size()]);

                dataBase.newsDao().insertAll(news);
                return null;
            }
        });
    }

    public Single<List<News>> getNews(){
        return Single.fromCallable(new Callable<List<News>>() {
            @Override
            public List<News> call() throws Exception {
                return AppDataBase.getAppDatabase(context).newsDao().getAllNews();
            }
        });
    }

    public Observable<List<News>> getNewsObservable(){
        return AppDataBase.getAppDatabase(context).newsDao().getAllNewsObservable();
    }

    public News getNewsById(int id){
        return AppDataBase.getAppDatabase(context).newsDao().getNewsById(id);
    }

    public void dropAllData(){
        AppDataBase.getAppDatabase(context).newsDao().deleteAll();
    }
}
