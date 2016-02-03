package com.bd.database;

import android.support.annotation.NonNull;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import java.util.Date;

public class TweetData extends RealmObject {

    @PrimaryKey
    private String stringId;
    private long id;
    private int favoriteCount;
    private int retweetCount;
    private boolean favorited;
    private boolean retweeted;
    private boolean isFavouriteModified;
    private boolean isRetweetModified;
    private boolean isOrigin;
    private Date date;
    private String text;
    private String fullName;
    private String nickName;
    private String avatarUrl;
    private String tweetImageUrl;
    private RealmList<RealmString> hashtagList;
    private RealmList<RealmString> userMentionsList;
    private RealmList<RealmString> urlList;
    private TweetData originTweet;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TweetData getOriginTweet() {
        return originTweet;
    }

    public void setOriginTweet(TweetData originTweet) {
        this.originTweet = originTweet;
    }

    public boolean isOrigin() {
        return isOrigin;
    }

    public void setOrigin(boolean origin) {
        isOrigin = origin;
    }

    public boolean isFavouriteModified() {
        return isFavouriteModified;
    }

    public void setFavouriteModified(boolean favouriteModified) {
        isFavouriteModified = favouriteModified;
    }

    public boolean isRetweetModified() {
        return isRetweetModified;
    }

    public void setRetweetModified(boolean retweetModified) {
        isRetweetModified = retweetModified;
    }

    public String getStringId() {
        return stringId;
    }

    public void setStringId(String stringId) {
        this.stringId = stringId;
    }

    public RealmList<RealmString> getUrlList() {
        return urlList;
    }

    public void setUrlList(RealmList<RealmString> urlList) {
        this.urlList = urlList;
    }

    public RealmList<RealmString> getUserMentionsList() {
        return userMentionsList;
    }

    public void setUserMentionsList(RealmList<RealmString> userMentionsList) {
        this.userMentionsList = userMentionsList;
    }

    public RealmList<RealmString> getHashtagList() {
        return hashtagList;
    }

    public void setHashtagList(RealmList<RealmString> hashtagList) {
        this.hashtagList = hashtagList;
    }

    public String getTweetImageUrl() {
        return tweetImageUrl;
    }

    public void setTweetImageUrl(String tweetImageUrl) {
        this.tweetImageUrl = tweetImageUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @NonNull
    public static TweetData copyFrom(@NonNull TweetData from) {
        TweetData to = new TweetData();

        to.setStringId(from.getStringId());
        to.setId(from.getId());
        to.setFavoriteCount(from.getFavoriteCount());
        to.setRetweetCount(from.getRetweetCount());
        to.setFavorited(from.isFavorited());
        to.setRetweeted(from.isRetweeted());
        to.setOrigin(from.isOrigin());
        to.setDate(from.getDate());
        to.setText(from.getText());
        to.setFullName(from.getFullName());
        to.setNickName(from.getNickName());
        to.setAvatarUrl(from.getAvatarUrl());
        to.setTweetImageUrl(from.getTweetImageUrl());
        to.setHashtagList(from.getHashtagList());
        to.setUserMentionsList(from.getUserMentionsList());
        to.setUrlList(from.getUrlList());

        return to;
    }
}
