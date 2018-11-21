package com.daracul.android.secondexercizeapp.utils;

import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import com.daracul.android.secondexercizeapp.R;
import com.daracul.android.secondexercizeapp.sync.StopServiceReciever;
import com.daracul.android.secondexercizeapp.ui.list.MainActivity;

import androidx.core.app.NotificationCompat;

public class NotificationUtils {

    private static final String CHANNEL_ID = "news:notification:channel";
    private static final String CHANNEL_ID_POP_UP = "news:notification:channel:pops";
    private static final int RESULT_NOTIFY_ID = 5432;

    public static Notification createForegroundNotification(Context context){
        if (VersionUtils.atLeastOreo()) {
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            NotificationChannel channel = notificationManager.getNotificationChannel(CHANNEL_ID);
            if (channel == null) {
                int importance = NotificationManager.IMPORTANCE_LOW;
                CharSequence name = context.getString(R.string.channel_name);
                channel = new NotificationChannel(CHANNEL_ID, name, importance);
            }
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.notification_news_update))
                .addAction(cancelUpdate(context))
                .setAutoCancel(true);
        if (!VersionUtils.atLeastOreo()) {
            mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }
        return mBuilder.build();
    }


    private static NotificationCompat.Action cancelUpdate(Context context) {
        Intent stopSelf = new Intent(context, StopServiceReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int)System.currentTimeMillis(), stopSelf, PendingIntent.FLAG_CANCEL_CURRENT);
        return new NotificationCompat.Action(R.drawable.baseline_account_circle_24,"Cancel",pendingIntent);
    }


    public static void showResultNotification(Context context, String message) {
        String name = context.getString(R.string.channel_name_pop_up);
        NotificationManager notifManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (VersionUtils.atLeastOreo()) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(CHANNEL_ID_POP_UP);
            if (mChannel == null) {
                mChannel = new NotificationChannel(CHANNEL_ID_POP_UP, name, importance);
                mChannel.enableVibration(true);
                mChannel.setLightColor(Color.GREEN);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_POP_UP);

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        builder.setContentTitle(message)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentText(context.getString(R.string.app_name))
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setTicker(message)
                .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        if (!VersionUtils.atLeastOreo()) {
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        Notification notification = builder.build();
        notifManager.notify(RESULT_NOTIFY_ID, notification);
    }
}
