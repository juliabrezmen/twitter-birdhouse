package com.bd.database;

import android.content.Context;
import android.support.annotation.NonNull;
import io.realm.Realm;

import java.util.List;

public class TweetDAO {
    private  Realm realm;
    private Context context;
    public TweetDAO(Context context){
        this.context=context;
        realm = Realm.getInstance(context);
    }

    @NonNull
    public List<TweetData> getAllTweets(){
        return realm.where(TweetData.class).findAll();
    }

    public void saveTweet(@NonNull  final List<TweetData> tweetDataList){
        Realm.getInstance(context).executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.clear(TweetData.class);
                for (TweetData tweetData : tweetDataList) {
                    realm.copyToRealm(tweetData);
                }
                realm.close();
            }
        });
    }

    public void close() {
        realm.close();
    }
}
