package com.bd.presenters;

import com.bd.broadcasts.TimelineUpdateBroadcast;
import com.bd.database.TweetDAO;
import com.bd.database.TweetData;
import com.bd.services.Action;
import com.bd.services.TwitterSyncService;
import com.bd.ui.HomeActivity;
import com.bd.ui.LoginActivity;
import com.bd.utils.L;
import com.twitter.sdk.android.Twitter;

import java.util.List;

public class HomePresenter {
    private HomeActivity activity;
    private TweetDAO tweetDAO;
    private TimelineUpdateBroadcast timelineUpdateBroadcast;

    public HomePresenter(HomeActivity activity) {
        this.activity = activity;
    }

    public void initPresenter() {
        if (isUserSignedIn()) {
            tweetDAO = new TweetDAO(activity.getApplicationContext());
            timelineUpdateBroadcast = new TimelineUpdateBroadcast(activity.getApplicationContext());
            timelineUpdateBroadcast.register(new TimelineUpdateBroadcast.Listener() {
                @Override
                public void onTimelineUpdated() {
                    loadTweetsFromDatabase();
                }
            });
            loadTweetsFromDatabase();
            TwitterSyncService.start(activity.getApplicationContext(), Action.LOAD_TWEETS);
        } else {
            LoginActivity.start(activity);
            activity.finish();
        }
    }

    public void onPullToRefresh() {
        loadTweetsFromDatabase();
        activity.hidePullToRefresh();
    }


    public void onActivityDestroy() {
        if(timelineUpdateBroadcast != null) {
            timelineUpdateBroadcast.unregister();
        }
        if (tweetDAO != null) {
            tweetDAO.close();
        }
    }

    private boolean isUserSignedIn() {
        return Twitter.getSessionManager().getActiveSession() != null;
    }

    private void loadTweetsFromDatabase() {
        List<TweetData> results = tweetDAO.getAllTweets();
        L.v("Load tweets from database: %d", results.size());
        activity.setHomeTimelineList(results);
    }

}
