package com.daracul.android.secondexercizeapp.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.recyclerview.widget.GridLayoutManager;

public class Utils {

    public static int convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) px;
    }


    public static void loadImageAndSetToView(String url, ImageView imageView) {
        Picasso.get().load(url).into(imageView);
    }

    public static String convertDateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm 'at' dd.MM.yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    public static boolean isHorizontal(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static void imitateWork(int waitInSecs) {
        try {
            Thread.sleep(waitInSecs * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String formatDateFromApi(String publishedDate) {
        String result = "";
        if (publishedDate != null && publishedDate.contains("T")) {
            String[] date = publishedDate.split("T");
            String time = date[1].split("-")[0];
            String formattedTime = time.substring(0, time.lastIndexOf(":"));
            result = date[0] + " at " + formattedTime;
        }
        return result;
    }

}
