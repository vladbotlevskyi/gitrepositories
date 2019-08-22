package com.example.gitapinext;

import android.app.Application;

import io.realm.Realm;

//import io.realm.Realm;
//import io.realm.RealmConfiguration;

public class GitTestApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
//        RealmConfiguration config = new RealmConfiguration.Builder()
//                .name("myrealm5.realm")
//                .schemaVersion(5)
//                .build();
//        Realm.setDefaultConfiguration(config);

    }
}
