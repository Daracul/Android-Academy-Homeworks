package com.daracul.android.secondexercizeapp.sync;

import android.content.Context;
import android.support.annotation.NonNull;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class NewsLoadWork extends Worker {

    public NewsLoadWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        NewsLoadService.start(getApplicationContext());
        return Result.SUCCESS;
    }
}
