package com.bd.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.bd.utils.L;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TwitterSyncService extends Service {

    private static final String ACTION_KEY = "ACTION_KEY";
    private ExecutorService executorService;
    private TweetSync tweetSync;

    @Override
    public void onCreate() {
        super.onCreate();
        executorService = Executors.newFixedThreadPool(1);
        tweetSync = new TweetSync(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        L.i("onStartCommand");
        if (intent != null) {
            Action action = Action.valueOf(intent.getStringExtra(ACTION_KEY));
            if (action == Action.GET_TWEETS) {
                executorService.execute(tweetSync.getTweetsCommand());
            }
            if (action == Action.POST_TWEETS) {
                executorService.execute(tweetSync.postTweetsCommand());
            }
        }
        return super.onStartCommand(intent, flags, startId);
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
