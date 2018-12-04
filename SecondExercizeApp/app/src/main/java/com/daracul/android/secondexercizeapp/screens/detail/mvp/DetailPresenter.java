package com.daracul.android.secondexercizeapp.screens.detail.mvp;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.daracul.android.secondexercizeapp.database.Db;
import com.daracul.android.secondexercizeapp.database.News;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class DetailPresenter extends MvpPresenter<DetailView> {
    private News news;
    private Db db;
    private String position;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        db = new Db();
        if (position!=null){
            loadDataFromDb(position);
        }
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void updateDb(String title, String fullText) {
        if (news != null) {
            news.setTitle(title);
            news.setPreviewText(title);
            Disposable disposable = db.updateNews(news)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe();
            compositeDisposable.add(disposable);

        }
    }

    public void deleteFromdDb() {
        if (news != null) {
            Disposable disposable = db.deleteNews(news)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action() {
                        @Override
                        public void run() throws Exception {
                            getViewState().popBackStack();
                        }
                    });
            compositeDisposable.add(disposable);
        }
    }


    private void loadDataFromDb(String position) {
        Disposable disposable = db.getNewsById(position)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<News>() {
                    @Override
                    public void accept(News news) throws Exception {
                        handleResult(news);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        handleError(throwable);
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void handleResult(News news) {
        if (news!=null){
            this.news = news;
            getViewState().showActionBar(news.getCategory());
            getViewState().showDate(news.getPublishDate());
            getViewState().showPicture(news.getImageUrl());
            getViewState().showTopic(news.getTitle());
            getViewState().showFullText(news.getPreviewText());
        }

    }

    private void handleError(Throwable throwable) {
        getViewState().showError(throwable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        db=null;
    }
}
