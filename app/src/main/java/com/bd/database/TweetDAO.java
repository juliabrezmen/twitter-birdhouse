package com.bd.database;

import android.content.Context;
import android.support.annotation.Nullable;
import io.realm.Realm;

import java.util.List;

public class TweetDAO {
    private  Realm realm;
    public TweetDAO(Context context){
        realm = Realm.getInstance(context);
    }

    @Nullable
    public List<TweetData> getAllTweets(){
        return realm.where(TweetData.class).findAll();
    }

    public void saveTweet(List<TweetData> tweetDataList){
        realm.beginTransaction();
        realm.clear(TweetData.class);
        for (TweetData tweetData : tweetDataList) {
            realm.copyToRealm(tweetData);
        }
        realm.commitTransaction();
    }
}
