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
    public static TweetData toTweetData(@NonNull Tweet tweet) {
        TweetData parentTweetData = new TweetData();
        copyValues(tweet, parentTweetData);


        Tweet originTweet = tweet.retweetedStatus;
        if (originTweet != null) {
            TweetData originTweetData = new TweetData();
            copyValues(originTweet, originTweetData);
            originTweetData.setOrigin(true);
            originTweetData.setStringId(tweet.idStr + tweet.retweetedStatus.idStr);

            parentTweetData.setOriginTweet(originTweetData);
        }


        return parentTweetData;
    }

    private static void copyValues(@NonNull Tweet tweet, TweetData tweetData) {
        tweetData.setId(tweet.id);
        tweetData.setStringId(String.valueOf(tweet.id));
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

        List<MediaEntity> mediaList = tweetEntities.media;
        if (mediaList != null) {
            if (!mediaList.isEmpty()) {
                MediaEntity mediaEntity = mediaList.get(0);
                tweetData.setTweetImageUrl(mediaEntity.mediaUrl);

                String text=tweet.text.replace(mediaEntity.url,"");
                tweetData.setText(text);
            }
        }

        setTagList(tweetData, tweetEntities);
        setUserMentionsList(tweetData, tweetEntities);
        setUrlList(tweetData, tweetEntities);
    }


    private static void setUrlList(TweetData tweetData, TweetEntities tweetEntities) {
        List<UrlEntity> urlEntityList = tweetEntities.urls;
        RealmList<RealmString> urlDataList = new RealmList<>();
        RealmString url;
        for (UrlEntity urlEntity : urlEntityList) {
            url = new RealmString();
            url.setString(urlEntity.url);
            urlDataList.add(url);
        }
        tweetData.setUrlList(urlDataList);
    }

    private static void setUserMentionsList(TweetData tweetData, TweetEntities tweetEntities) {
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
