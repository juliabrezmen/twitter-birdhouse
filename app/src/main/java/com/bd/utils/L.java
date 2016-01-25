package com.bd.utils;

import android.util.Log;

public class L {
    static String TAG = "TwitterBirdhouse";

    public static void i(String msg) {
        Log.i(TAG, msg);
    }

    public static void v(String msg) {
        Log.v(TAG, msg);
    }

    public static void v(String message, Object...args) {
        Log.v(TAG, String.format(message, args));
    }

    public static void w(String msg, Throwable t) {
        Log.w(TAG, msg, t);
    }
}
