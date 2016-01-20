package com.bd.database;

import io.realm.RealmObject;

public class UserMentionData extends RealmObject {
    private String screenName;

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }
}
