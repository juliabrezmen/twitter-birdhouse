package com.bd.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.bd.R;

public class NewTweetActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_tweet_activity);
    }

    public static void start(@NonNull Activity activity) {
        Intent intent = new Intent(activity, NewTweetActivity.class);
        activity.startActivity(intent);
    }
}
