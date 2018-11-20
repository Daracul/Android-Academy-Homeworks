package com.daracul.android.secondexercizeapp.utils;

import android.os.Build;

public class VersionUtils {
    public static boolean atLeastOreo(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }
}
