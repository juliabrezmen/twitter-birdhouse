package com.bd.services;

import android.content.Context;
import android.util.Log;
import com.bd.broadcasts.TimelineUpdateBroadcast;
import com.bd.database.Converter;
import com.bd.database.TweetDAO;
import com.bd.database.TweetData;
import com.bd.utils.L;
import com.twitter.sdk.android.core.*;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.FavoriteService;
import com.twitter.sdk.android.core.services.StatusesService;
import io.realm.Realm;
import io.realm.RealmResults;

import java.util.List;

public class TweetSync {

    private final FavoriteService favoriteService;
    private StatusesService statusesService;
    private Context context;

    public TweetSync(Context context) {
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        statusesService = twitterApiClient.getStatusesService();
        favoriteService = twitterApiClient.getFavoriteService();

        this.context = context;
    }

    public Runnable postTweetsCommand() {
        return new Runnable() {
            @Override
            public void run() {
                postTweets();
            }
        };
    }

    public Runnable getTweetsCommand() {
        return new Runnable() {
            @Override
            public void run() {
                getTweets();
            }
        };
    }

    public void getTweets() {
        statusesService.homeTimeline(50, null, null, null, null, null, null, new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                L.v("getTweets success");
                if (result.data != null) {
                    TweetDAO.newInstance().saveTweet(Converter.toTweetDataList(result.data));
                    TimelineUpdateBroadcast.send(context);
                }
            }

            @Override
            public void failure(TwitterException e) {
                Log.i("Birdhouse", "Failed " + e.getMessage());
            }
        });
    }

    public void postTweets() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<TweetData> favoriteModifiedTweets = TweetDAO.newInstance().getFavoriteModifiedTweets(realm);
        for (TweetData tweet : favoriteModifiedTweets) {
            if (tweet.isFavorited()) {
                createFavorite(tweet);
            } else {
                destroyFavorite(tweet);
            }
        }
        realm.close();
    }

    private void createFavorite(TweetData tweet) {
        favoriteService.create(tweet.getId(), null, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                L.v("createFavorite success");
                if (result.data != null) {
                    TweetDAO.newInstance().saveTweet(Converter.toTweetData(result.data));
                }
            }

            @Override
            public void failure(TwitterException e) {
                L.w("Post favourites failure", e);
            }
        });
    }

    private void destroyFavorite(TweetData tweet) {
        favoriteService.destroy(tweet.getId(), null, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                L.v("destroyFavorite success");
                if (result.data != null) {
                    TweetDAO.newInstance().saveTweet(Converter.toTweetData(result.data));
                }
            }

            @Override
            public void failure(TwitterException e) {
                L.w("Post unfavourites failure", e);
            }
        });
    }
}
