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
import io.realm.Realm;

import java.util.List;

public class HomePresenter {
    private Realm realm = Realm.getDefaultInstance();
    private HomeActivity activity;
    private TimelineUpdateBroadcast timelineUpdateBroadcast;

    public HomePresenter(HomeActivity activity) {
        this.activity = activity;
    }

    public void initPresenter() {
        if (isUserSignedIn()) {
            timelineUpdateBroadcast = new TimelineUpdateBroadcast(activity.getApplicationContext());
            timelineUpdateBroadcast.register(new TimelineUpdateBroadcast.Listener() {
                @Override
                public void onTimelineUpdated() {
                    loadTweetsFromDatabase();
                    activity.hidePullToRefresh();
                }
            });
            loadTweetsFromDatabase();
            TwitterSyncService.start(activity.getApplicationContext(), Action.POST_TWEETS);
            TwitterSyncService.start(activity.getApplicationContext(), Action.GET_TWEETS);
        } else {
            LoginActivity.start(activity);
            activity.finish();
        }
    }

    public void onPullToRefresh() {
        TwitterSyncService.start(activity.getApplicationContext(), Action.POST_TWEETS);
        TwitterSyncService.start(activity.getApplicationContext(), Action.GET_TWEETS);
    }

    public void onFavouriteClicked(TweetData tweet) {
        if (tweet.isFavorited()) {
            TweetDAO.newInstance().updateTweetFavourite(tweet.getId(), false, tweet.getFavoriteCount() - 1);
        } else {
            TweetDAO.newInstance().updateTweetFavourite(tweet.getId(), true, tweet.getFavoriteCount() + 1);
        }
        TwitterSyncService.start(activity.getApplicationContext(), Action.POST_TWEETS);
    }

    public void onRetweetClicked(TweetData tweet) {
        if (tweet.isRetweeted()) {
            TweetDAO.newInstance().setRetweeted(tweet.getId(), false, tweet.getRetweetCount() - 1);
        } else {
            TweetDAO.newInstance().setRetweeted(tweet.getId(), true, tweet.getRetweetCount() + 1);
        }
        TwitterSyncService.start(activity.getApplicationContext(), Action.POST_TWEETS);
    }

    public void onActivityDestroy() {
        if (timelineUpdateBroadcast != null) {
            timelineUpdateBroadcast.unregister();
        }
        if (realm != null) {
            realm.close();
        }
    }

    private boolean isUserSignedIn() {
        return Twitter.getSessionManager().getActiveSession() != null;
    }

    private void loadTweetsFromDatabase() {
        List<TweetData> results = TweetDAO.newInstance().getAllTweets(realm);
        L.v("size: " + results.size());
        activity.setHomeTimelineList(results);
    }

}
