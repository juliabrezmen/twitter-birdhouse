package com.bd;

import android.app.Application;
import android.util.Log;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

public class BirdhouseApplication extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "IkgMPY7xbqEN2D86EDDadiBbM";
    private static final String TWITTER_SECRET = "cM6rrMTxMYkGKndlOd1E0hjrbxOfOzZw9TbJN9slfVOt7YNjFd";

    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        Log.i("app", "from App");

    }
}
