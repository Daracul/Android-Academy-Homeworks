package com.daracul.android.secondexercizeapp.screens.about.mvp;


import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.daracul.android.secondexercizeapp.R;
import com.daracul.android.secondexercizeapp.application.MyApplication;


@InjectViewState
public class AboutPresenter extends MvpPresenter<AboutView> {
    private Context context = MyApplication.getContext();

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().showActionBar(context.getString(R.string.my_name));
        getViewState().showDataInTextViews();

    }

    public void onClickSendMessage(String emailAddress, String message) {
        if (!message.isEmpty()){
            getViewState().openEmailClient(emailAddress,message);
        } else getViewState().showToastMessage(context.getString(R.string.edit_text_empty_message));

    }

    public void onClickOpenBrowser(String url) {
        getViewState().startBrowser(url);
    }
}
