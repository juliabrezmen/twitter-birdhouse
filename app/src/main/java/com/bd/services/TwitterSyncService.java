package com.bd.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.bd.database.Converter;
import com.bd.database.TweetDAO;
import com.twitter.sdk.android.core.*;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.List;

public class TwitterSyncService extends Service {

    private static final String ACTION_KEY = "ACTION_KEY";
    private StatusesService statusesService;

    @Override
    public void onCreate() {
        super.onCreate();
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        statusesService = twitterApiClient.getStatusesService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Action action = Action.valueOf(intent.getStringExtra(ACTION_KEY));
        if (action == Action.LOAD_TWEETS) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    loadTweets();
                }
            }).start();

        }
        return super.onStartCommand(intent, flags, startId);
    }


    private void loadTweets() {
        statusesService.homeTimeline(50, null, null, null, null, null, null, new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                if (result.data != null) {
                    TweetDAO tweetDAO = new TweetDAO(getApplicationContext());
                    tweetDAO.saveTweet(Converter.toTweetDataList(result.data));
                }
            }

            @Override
            public void failure(TwitterException e) {
                Log.i("Birdhouse", "Failed " + e.getMessage());
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void start(@NonNull Context context, @NonNull Action action) {
        Intent intent = new Intent(context, TwitterSyncService.class);
        intent.putExtra(ACTION_KEY, String.valueOf(action));
        context.startService(intent);
    }

    public enum Action {
        LOAD_TWEETS
    }
}
