package com.daracul.android.secondexercizeapp.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.daracul.android.secondexercizeapp.application.MyApplication;


import io.reactivex.Single;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

public class NetworkUtils {
    public static NetworkUtils networkUtils = new NetworkUtils();
    private NetworkReceiver networkReceiver = new NetworkReceiver();
    private Subject<Boolean> networkState = BehaviorSubject.createDefault(isNetworkAvailable());

    public NetworkReceiver getReceiver(){
        return networkReceiver;
    }

    public Single<Boolean> getOnlineNetwork(){
        return networkState
                .subscribeOn(Schedulers.io())
                .filter(new Predicate<Boolean>() {
                    @Override
                    public boolean test(Boolean aBoolean) throws Exception {
                        return aBoolean;
                    }
                })
                .firstOrError();
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager)MyApplication.
                getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager==null){
            return false;
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo!=null&&networkInfo.isConnected();
    }

    public class NetworkReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            networkState.onNext(isNetworkAvailable());
        }
    }
}
