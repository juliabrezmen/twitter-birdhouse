package com.bd.database;

import android.support.annotation.NonNull;
import io.realm.Realm;
import io.realm.RealmResults;

import java.util.List;

public class TweetDAO {

    public static TweetDAO newInstance(){
        return new TweetDAO();
    }

    @NonNull
    public List<TweetData> getAllTweets(@NonNull Realm realm) {
        return realm.where(TweetData.class).findAll();
    }

    public void saveTweet(@NonNull final List<TweetData> tweetDataList) {
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(tweetDataList);
                realm.close();
            }
        });
    }

    public void saveTweet(@NonNull final TweetData tweetData) {
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(tweetData);
                realm.close();
            }
        });
    }

    public void updateTweetFavourite(final long id, final boolean favourite, final int count) {

        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TweetData editTweet = realm.where(TweetData.class).equalTo("id", id).findFirst();
                if (editTweet != null) {
                    editTweet.setFavorited(favourite);
                    editTweet.setFavoriteCount(count);
                    editTweet.setFavouriteModified(true);
                }
                realm.close();
            }
        });
    }

    public RealmResults<TweetData> getFavoriteModifiedTweets(@NonNull Realm realm) {
        return realm.where(TweetData.class).equalTo("isFavouriteModified", true).findAll();
    }
}
