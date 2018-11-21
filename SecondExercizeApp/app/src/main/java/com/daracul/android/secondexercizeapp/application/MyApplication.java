package com.daracul.android.secondexercizeapp.application;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.daracul.android.secondexercizeapp.utils.NetworkUtils;

public class MyApplication extends Application {
    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    public static Context getContext(){
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();

        registerReceiver(NetworkUtils.networkUtils.getReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }
}
