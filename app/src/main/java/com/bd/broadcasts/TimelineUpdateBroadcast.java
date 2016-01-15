package com.bd.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import com.bd.utils.L;

public class TimelineUpdateBroadcast {

    private static final String UPDATED_TWEETS_EVENT = "UPDATED_TWEETS_EVENT";

    private BroadcastReceiver mMessageReceiver;
    private Context context;
    private boolean isRegistered;

    public TimelineUpdateBroadcast(Context context) {
        this.context = context;
    }

    public static void send(Context context) {
        Intent intent = new Intent(UPDATED_TWEETS_EVENT);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public void register(@NonNull final Listener listener) {
        if (!isRegistered) {
            isRegistered = true;
            mMessageReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    L.i("Received broadcast");
                    listener.onTimelineUpdated();
                }
            };
            IntentFilter intentFilter = new IntentFilter(UPDATED_TWEETS_EVENT);
            LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver, intentFilter);
        }
    }

    public void unregister() {
        if (isRegistered) {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
        }
    }

    public interface Listener{
        void onTimelineUpdated();
    }
}
