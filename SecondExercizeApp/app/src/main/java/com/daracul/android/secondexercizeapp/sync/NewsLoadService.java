package com.daracul.android.secondexercizeapp.sync;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.daracul.android.secondexercizeapp.R;
import com.daracul.android.secondexercizeapp.database.Db;
import com.daracul.android.secondexercizeapp.database.News;
import com.daracul.android.secondexercizeapp.model.NewsMapper;
import com.daracul.android.secondexercizeapp.model.ResultDTO;
import com.daracul.android.secondexercizeapp.network.DefaultResponse;
import com.daracul.android.secondexercizeapp.network.RestApi;
import com.daracul.android.secondexercizeapp.utils.NotificationUtils;
import com.daracul.android.secondexercizeapp.utils.VersionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

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
        db = new Db(this.getApplicationContext());
        disposable = RestApi.getInstance()
                .news()
                .newsObject(DEFAULT_NEWS_CATEGORY)
                .subscribeOn(Schedulers.io())
                .delay(10,TimeUnit.SECONDS)
                .observeOn(Schedulers.io())
                .map(new Function<Response<DefaultResponse<List<ResultDTO>>>, List<News>>() {
                    @Override
                    public List<News> apply(Response<DefaultResponse<List<ResultDTO>>> defaultResponseResponse) throws Exception {
                        return NewsMapper.convertDTOListToNewsItem(defaultResponseResponse);
                    }
                })
                .flatMap(new Function<List<News>, SingleSource<?>>() {
                    @Override
                    public SingleSource<?> apply(List<News> newsList) throws Exception {
                        return db.saveNews(newsList);
                    }
                }).subscribe(new Consumer<Object>() {
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
                                throwable.getMessage());
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
