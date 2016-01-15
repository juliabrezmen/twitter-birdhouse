package com.bd.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.bd.broadcasts.TimelineUpdateBroadcast;
import com.bd.database.Converter;
import com.bd.database.TweetDAO;
import com.bd.utils.L;
import com.twitter.sdk.android.core.*;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TwitterSyncService extends Service {

    private static final String ACTION_KEY = "ACTION_KEY";
    private StatusesService statusesService;
    private ExecutorService executorService;

    @Override
    public void onCreate() {
        super.onCreate();
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        statusesService = twitterApiClient.getStatusesService();
        executorService = Executors.newFixedThreadPool(2);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        L.i("onStartCommand");
        if (intent != null) {
            Action action = Action.valueOf(intent.getStringExtra(ACTION_KEY));
            if (action == Action.LOAD_TWEETS) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loadTweets();
                    }
                });
                executorService.execute(thread);
            }
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
                    TimelineUpdateBroadcast.send(getApplicationContext());
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

}
