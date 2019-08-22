package com.example.gitapinext.repository;

import android.util.Log;

import com.example.gitapinext.model.Count;
import com.example.gitapinext.model.Repository;
import com.example.gitapinext.model.User;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class LocalDB {

    private Realm realmDB;

    private Realm getRealmDB() {
        if (realmDB == null) {
            RealmConfiguration config = new RealmConfiguration.Builder()
                    .name("myrealm9.realm")
                    .schemaVersion(9)
                    .build();
            Realm.setDefaultConfiguration(config);
            Log.i("log_tag", "getRealmDB thread  " + Thread.currentThread().getName());
            realmDB = Realm.getDefaultInstance();
        }
        return realmDB;
    }


    public boolean addCount(Count count) {
        Realm realmDB = getRealmDB();
        Log.i("log_tag", "addCount thread1:  " + Thread.currentThread().getName());

        realmDB.executeTransaction(realm -> {
            Log.i("log_tag", "addCount thread2:  " + Thread.currentThread().getName());
            Number currentIdNum = realm.where(Count.class).max("id");
            int nextId;
            if (currentIdNum == null) {
                nextId = 1;
            } else {
                nextId = currentIdNum.intValue() + 1;
            }
            count.setId(nextId);
            realm.insertOrUpdate(count);
        });
        return true;
    }

    public List<Count> getAllCount() {
        Realm realmDB = getRealmDB();
        List<Count> list = realmDB.where(Count.class).findAll();
        return list;
    }

    public boolean saveUser(List<User> users) {
        Realm realmDB = getRealmDB();
//        User u = realmDB.where(User.class).equalTo("id", user.getId()).findFirst();
        realmDB.executeTransaction(realm -> {
            Log.i("log_tag", "saveUser thread  " + Thread.currentThread().getName());
            realmDB.insertOrUpdate(users);
        });
        return true;
    }

    public RealmResults<User> getAllUser() {
        Realm realmDB = getRealmDB();
        Log.i("log_tag", "getAllUser thread  " + Thread.currentThread().getName());
        RealmResults<User> list = realmDB.where(User.class).sort("login").findAllAsync();
        return list;
    }

    public boolean saveRepository(Repository repository) {
        Realm realmDB = getRealmDB();
        realmDB.executeTransaction(realm -> {
            Log.i("log_tag", "saveRepository thread  " + Thread.currentThread().getName());
            Repository r = realm.copyToRealmOrUpdate(repository);
        });
        return true;
    }

    public RealmResults<Repository> getAllRepositoryByName(String name) {
        Log.i("log_tag", "getAllRepositoryByName thread  " + Thread.currentThread().getName());
        Realm realmDB = getRealmDB();
        RealmResults<Repository> list = realmDB
                .where(Repository.class)
                .and().equalTo("userNameId", name)
                .sort("id")
                .findAll();
        Log.i("log_tag", "getAllRepositoryByName:  " + list.size());
        return list;
    }

    void dispose() {
        realmDB.close();
    }
}
