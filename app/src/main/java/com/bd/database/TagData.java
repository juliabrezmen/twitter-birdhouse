package com.bd.database;

import io.realm.RealmObject;

public class TagData extends RealmObject {
    private String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
