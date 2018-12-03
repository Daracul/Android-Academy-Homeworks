package com.daracul.android.secondexercizeapp.sync;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.daracul.android.secondexercizeapp.R;
import com.daracul.android.secondexercizeapp.database.Db;
import com.daracul.android.secondexercizeapp.utils.NetworkUtils;
import com.daracul.android.secondexercizeapp.utils.NotificationUtils;
import com.daracul.android.secondexercizeapp.utils.VersionUtils;
import java.util.concurrent.TimeUnit;

import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class NewsLoadService extends Service {
    private static final String DEFAULT_NEWS_CATEGORY = "home";
    private static final int SERVICE_FOREGROUND_ID = 4322;
    private Db db;
    private Disposable disposable;

    public static void start(Context context) {
        Intent intent = new Intent(context,NewsLoadService.class);
        if (VersionUtils.atLeastOreo()){
            context.startForegroundService(intent);
        } else context.startService(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Notification notification = NotificationUtils.createForegroundNotification(this);
        startForeground(SERVICE_FOREGROUND_ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        db = new Db();
        disposable = NetworkUtils.networkUtils.getOnlineNetwork()
                .timeout(1,TimeUnit.MINUTES)
                .flatMap(new Function<Boolean, SingleSource<?>>() {
                    @Override
                    public SingleSource<?> apply(Boolean aBoolean) throws Exception {
                        return DownloadingNews.updateNews(db, DEFAULT_NEWS_CATEGORY);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        NotificationUtils.showResultNotification(NewsLoadService.this,
                                NewsLoadService.this.getString(R.string.sucess_message));
                        NewsLoadService.this.stopSelf();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        NotificationUtils.showResultNotification(NewsLoadService.this,
                                throwable.getClass().getSimpleName());
                        NewsLoadService.this.stopSelf();
                    }
                });
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (disposable!=null&&!disposable.isDisposed()){
            disposable.dispose();
        }
        if (db!=null){
            db=null;
        }
    }
}
