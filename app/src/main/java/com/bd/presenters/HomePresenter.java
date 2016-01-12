package com.bd.presenters;

import android.util.Log;
import com.bd.ui.HomeActivity;
import com.bd.ui.LoginActivity;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.*;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.List;

public class HomePresenter {
    HomeActivity mActivity;

    public void initPresenter(HomeActivity activity) {
        if (!isUserSignedIn()) {
            activity.finish();
            LoginActivity.start(activity);
        } else {
            mActivity = activity;
        }
    }

    private boolean isUserSignedIn() {
        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        return session != null;
    }

    public void onShowTweetsClicked() {
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        StatusesService statusesService = twitterApiClient.getStatusesService();
        statusesService.homeTimeline(50, null, null, null, null, null, null, new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                if (result.data != null) {
                    mActivity.setHomeTimelineList(result.data);
                    for (Tweet tweet : result.data) {
//                        Log.i("Birdhouse", tweet.text);

                    }
                }
            }

            @Override
            public void failure(TwitterException e) {
                Log.i("Birdhouse", "Failed " + e.getMessage());
            }
        });
    }
}
