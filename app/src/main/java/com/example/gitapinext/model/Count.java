package com.example.gitapinext.model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Count extends RealmObject {

    @PrimaryKey
    private long id;
    private String userName;

    public Count() {
    }

    public Count(String userName) {
        this.userName = userName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
