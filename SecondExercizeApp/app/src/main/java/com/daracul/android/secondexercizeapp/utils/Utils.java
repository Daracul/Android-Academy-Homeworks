package com.daracul.android.secondexercizeapp.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static int convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int)px;
    }

    public static void loadImageAndSetToView (String url, ImageView imageView, int size){
        Picasso.get().load(url).
                resize(size,size).centerCrop().into(imageView);
    }

    public static void loadImageAndSetToView (String url, ImageView imageView){
        Picasso.get().load(url).into(imageView);
    }

    public static String convertDateToString (Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm 'at' dd.MM.yyyy",Locale.getDefault());
        return sdf.format(date);
    }
}
