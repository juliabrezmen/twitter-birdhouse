package com.bd.managers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class PreferencesManager {

    private static final String TWITTER_BIRDHOUSE_PREFS = "twitter-birdhouse-prefs";
    private static final String USER_NAME = "user name";

    public static void saveUserName(@NonNull String username, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(TWITTER_BIRDHOUSE_PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(USER_NAME, username);
        editor.apply();
    }

    @Nullable
    public static String getUserName(Context context){
        SharedPreferences prefs = context.getSharedPreferences(TWITTER_BIRDHOUSE_PREFS, Activity.MODE_PRIVATE);
        return prefs.getString(USER_NAME,null);
    }
}
