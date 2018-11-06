package com.daracul.android.secondexercizeapp.database;

import android.content.Context;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Action;

public class Db {


    private AppDataBase dataBase;

    public Db(Context context) {
        dataBase = AppDataBase.getAppDatabase(context);
    }

    public Completable saveNews(final List<News> newsList){
        return Completable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
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
                return dataBase.newsDao().getAllNews();
            }
        });
    }

    public Observable<List<News>> getNewsObservable(){
        return dataBase.newsDao().getAllNewsObservable();
    }

    public Single<News> getNewsById(final int id){
        return Single.fromCallable(new Callable<News>() {
            @Override
            public News call() throws Exception {
                return dataBase.newsDao().getNewsById(id);
            }
        });
    }

    public Completable updateNews(final News news){
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                dataBase.newsDao().updateNews(news);
            }
        });
    }

    public Completable deleteNews(final News news){
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                dataBase.newsDao().delete(news);
            }
        });
    }

    public void dropAllData(){
        dataBase.newsDao().deleteAll();
    }
}
