package com.daracul.android.secondexercizeapp.screens.list.mvp;

import android.content.Context;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.daracul.android.secondexercizeapp.R;
import com.daracul.android.secondexercizeapp.application.MyApplication;
import com.daracul.android.secondexercizeapp.database.Db;
import com.daracul.android.secondexercizeapp.database.News;
import com.daracul.android.secondexercizeapp.sync.DownloadingNews;
import com.daracul.android.secondexercizeapp.utils.State;

import java.io.IOException;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class ListPresenter extends MvpPresenter<ListView> {
    private Db db;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private int spinnerPosition = 0;
    private Context context;

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        context = MyApplication.getContext();
        db = new Db();
        subcribeToDataFromDb();
    }

    public void setSpinnerPosition(int spinnerPosition) {
        this.spinnerPosition = spinnerPosition;
    }

    public int getSpinnerPosition() {
        return spinnerPosition;
    }

    private String getCurrentNewsCategory() {
        return context.getResources().getStringArray(R.array.category_spinner)[spinnerPosition].toLowerCase();
    }

    private void subcribeToDataFromDb() {
        Disposable disposable = db.getNewsObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<News>>() {
                    @Override
                    public void accept(List<News> newsList) throws Exception {
                        getViewState().subcribeDataFromDb(newsList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        handleError(throwable);
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void loadNews() {
        getViewState().showState(State.Loading);
//        recyclerScreen.setRefreshing(false);
        final Disposable disposable = DownloadingNews.updateNews(db,getCurrentNewsCategory())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        handleResponse();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        handleError(throwable);
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void handleResponse() {
        getViewState().showState(State.HasData);
    }

    private void handleError(Throwable throwable) {
        Log.d("myLogs",throwable.getClass().getName() +" message: "+throwable.getMessage());
        if (throwable instanceof IOException) {
            getViewState().showState(State.ServerError);
            return;
        }
        getViewState().showState(State.NetworkError);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
