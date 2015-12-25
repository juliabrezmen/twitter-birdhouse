package com.bd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterSession;

public class HomeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        if (!isUserSignedIn()) {
            Log.i("app:", "start if");
            finish();
            startLoginActivity();
        }
    }

    private boolean isUserSignedIn() {
        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        return session != null;
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
