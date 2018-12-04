package com.daracul.android.secondexercizeapp.screens.about.mvp;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface AboutView extends MvpView {
    void showActionBar(@NonNull String text);
    void showDataInTextViews();
    @StateStrategyType(SkipStrategy.class)
    void openEmailClient(@NonNull String email, @NonNull String message);
    @StateStrategyType(SkipStrategy.class)
    void showToastMessage(@NonNull String toastText);
    @StateStrategyType(SkipStrategy.class)
    void startBrowser(String url);



}
