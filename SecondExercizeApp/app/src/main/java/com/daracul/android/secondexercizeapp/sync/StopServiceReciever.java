package com.daracul.android.secondexercizeapp.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StopServiceReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, NewsLoadService.class);
        context.stopService(service);
    }
}
