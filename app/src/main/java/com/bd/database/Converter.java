package com.bd.database;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.twitter.sdk.android.core.models.*;
import io.realm.RealmList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Converter {

    private static final SimpleDateFormat TWEET_DATE_FORMATTER = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.getDefault());

    @NonNull
    private static TweetData toTweetData(@NonNull Tweet tweet) {
        TweetData tweetData = new TweetData();
        tweetData.setText(tweet.text);
        tweetData.setFavoriteCount(tweet.favoriteCount);
        tweetData.setRetweetCount(tweet.retweetCount);
        tweetData.setFavorited(tweet.favorited);
        tweetData.setRetweeted(tweet.retweeted);
        tweetData.setDate(parseDate(tweet.createdAt));

        tweetData.setFullName(tweet.user.name);
        tweetData.setNickName("@" + tweet.user.screenName);
        tweetData.setAvatarUrl(tweet.user.profileImageUrl);

        TweetEntities tweetEntities = tweet.entities;

        //TODO: method setTweetImgUrl()
        List<MediaEntity> mediaList = tweetEntities.media;
        if (mediaList != null) {
            if (!mediaList.isEmpty()) {
                tweetData.setTweetImageUrl(mediaList.get(0).mediaUrl);
            }
        }

        setTagList(tweetData, tweetEntities);
        setUserMentions(tweetData, tweetEntities);

        return tweetData;
    }

    private static void setUserMentions(TweetData tweetData, TweetEntities tweetEntities) {
        List<MentionEntity> userMentionsList = tweetEntities.userMentions;
        RealmList<RealmString> userMentionDataList = new RealmList<>();
        RealmString userData;
        for (MentionEntity user : userMentionsList) {
            userData = new RealmString();
            userData.setString(user.screenName);
            userMentionDataList.add(userData);
        }
        tweetData.setUserMentionsList(userMentionDataList);
    }

    private static void setTagList(TweetData tweetData, TweetEntities tweetEntities) {
        List<HashtagEntity> hashtags = tweetEntities.hashtags;
        RealmList<RealmString> tagDataList = new RealmList<>();
        RealmString tag;
        for (HashtagEntity hashtag : hashtags) {
            tag = new RealmString();
            tag.setString(hashtag.text);
            tagDataList.add(tag);
        }
        tweetData.setHashtagList(tagDataList);
    }

    @Nullable
    private static Date parseDate(@Nullable String createdAt) {
        Date convertedDate = null;
        if (createdAt != null) {
            try {
                convertedDate = TWEET_DATE_FORMATTER.parse(createdAt);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return convertedDate;
    }

    @NonNull
    public static List<TweetData> toTweetDataList(@NonNull List<Tweet> tweetList) {
        List<TweetData> tweetDataList = new ArrayList<>(tweetList.size());
        for (Tweet tweet : tweetList) {
            tweetDataList.add(toTweetData(tweet));
        }
        return tweetDataList;
    }

}