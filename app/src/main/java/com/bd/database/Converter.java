package com.bd.database;

import com.twitter.sdk.android.core.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Converter {
    private static TweetData convertTweet(Tweet tweet) {
        TweetData tweetData = new TweetData();
        tweetData.setText(tweet.text);
        tweetData.setFavoriteCount(tweet.favoriteCount);
        tweetData.setRetweetCount(tweet.retweetCount);
        tweetData.setFavorited(tweet.favorited);
        tweetData.setRetweeted(tweet.retweeted);
        setTweetDate(tweet, tweetData);
        tweetData.setFullName(tweet.user.name);
        tweetData.setNickName("@" + tweet.user.screenName);
        tweetData.setAvatarUrl(tweet.user.profileImageUrl);
        return tweetData;
    }

    private static void setTweetDate(Tweet tweet, TweetData tweetData) {
        String createdAt = tweet.createdAt;
        SimpleDateFormat parser = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");
        try {
            Date convertedDate = parser.parse(createdAt);
            tweetData.setDate(convertedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<TweetData> convertTweetList(List<Tweet> tweetList) {
        List<TweetData> tweetDataList = new ArrayList<>(tweetList.size());
        for (Tweet tweet : tweetList) {
            tweetDataList.add(convertTweet(tweet));
        }
        return (ArrayList<TweetData>) tweetDataList;
    }

}
