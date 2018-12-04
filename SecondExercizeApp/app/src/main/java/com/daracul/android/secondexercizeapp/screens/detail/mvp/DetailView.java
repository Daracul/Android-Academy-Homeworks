package com.daracul.android.secondexercizeapp.screens.detail.mvp;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.Date;

public interface DetailView extends MvpView {
    void showPicture(@NonNull String url);
    void showTopic (@NonNull String topic);
    void showDate(@NonNull Date date);
    void showFullText(@NonNull String fullText);
    void showActionBar(@NonNull String text);
    @StateStrategyType(SkipStrategy.class)
    void popBackStack();
    @StateStrategyType(SkipStrategy.class)
    void showError(Throwable throwable);
}
