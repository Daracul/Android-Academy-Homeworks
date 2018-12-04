package com.daracul.android.secondexercizeapp.screens.list.mvp;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpView;
import com.daracul.android.secondexercizeapp.database.News;
import com.daracul.android.secondexercizeapp.utils.State;

import java.util.List;

public interface ListView extends MvpView {
    void showState(@NonNull State state);
    void subcribeDataFromDb(@NonNull List<News> newsList);

}
