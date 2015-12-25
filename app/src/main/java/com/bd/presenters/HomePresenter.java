package com.bd.presenters;

import android.app.Activity;
import android.util.Log;
import com.bd.ui.LoginActivity;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterSession;

public class HomePresenter {

    public void initPresenter(Activity activity) {
        if (!isUserSignedIn()) {
            Log.i("app:", "start if");
            activity.finish();
            LoginActivity.start(activity);
        }
    }

    private boolean isUserSignedIn() {
        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        return session != null;
    }

}
