package com.bd.presenters;

import android.util.Log;
import com.bd.database.Converter;
import com.bd.database.TweetDAO;
import com.bd.database.TweetData;
import com.bd.ui.HomeActivity;
import com.bd.ui.LoginActivity;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.*;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.List;

public class HomePresenter {
    private HomeActivity activity;
    private StatusesService statusesService;

    public HomePresenter(HomeActivity activity) {
        this.activity = activity;
    }

    // FIXME: 13.01.2016 check for network
    public void initPresenter() {
        if (isUserSignedIn()) {
            TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
            statusesService = twitterApiClient.getStatusesService();
            loadTweetsFromDatabase();
        } else {
            LoginActivity.start(activity);
            activity.finish();
        }
    }

    public void onPullToRefresh() {
        loadTweets();
    }

    private boolean isUserSignedIn() {
        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        return session != null;
    }

    private void loadTweetsFromDatabase() {
        TweetDAO tweetDAO = new TweetDAO(activity.getApplicationContext());
        List<TweetData> results = tweetDAO.getAllTweets();
        if (results != null) {
            Log.i("app", String.valueOf(results.size()));
            activity.setHomeTimelineList(results);
        }
    }

    private void loadTweets() {
        statusesService.homeTimeline(50, null, null, null, null, null, null, new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                if (result.data != null) {
                    // write to realm database
                    List<TweetData> tweetDataList = Converter.toTweetDataList(result.data);
                    TweetDAO tweetDAO = new TweetDAO(activity.getApplicationContext());
                    tweetDAO.saveTweet(tweetDataList);
                    activity.setHomeTimelineList(tweetDataList);
                }
                activity.hidePullToRefresh();
            }

            @Override
            public void failure(TwitterException e) {
                Log.i("Birdhouse", "Failed " + e.getMessage());
                activity.hidePullToRefresh();
            }
        });
    }
}
