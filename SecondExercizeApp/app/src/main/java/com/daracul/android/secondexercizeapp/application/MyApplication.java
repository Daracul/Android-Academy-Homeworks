package com.daracul.android.secondexercizeapp.application;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.daracul.android.secondexercizeapp.sync.NewsLoadWork;
import com.daracul.android.secondexercizeapp.utils.NetworkUtils;

import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

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

        Constraints constraints = new Constraints.Builder().setRequiresCharging(true).build();

        WorkRequest workRequest = new PeriodicWorkRequest.Builder(NewsLoadWork.class,3,TimeUnit.HOURS)
                .setConstraints(constraints).build();
        WorkManager.getInstance().enqueue(workRequest);

        registerReceiver(NetworkUtils.networkUtils.getReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }


}
