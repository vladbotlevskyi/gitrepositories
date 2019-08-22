package com.example.gitapinext.repository;

import android.util.Log;

import com.example.gitapinext.model.Count;
import com.example.gitapinext.model.Repository;
import com.example.gitapinext.repository.modelData.CountModel;
import com.example.gitapinext.repository.modelData.IUsersModel;
import com.example.gitapinext.repository.modelData.UsersModel;
import com.example.gitapinext.repository.modelStatus.Error;
import com.example.gitapinext.repository.modelStatus.IStatus;
import com.example.gitapinext.repository.modelStatus.Loading;
import com.example.gitapinext.repository.modelStatus.Successful;
import com.example.gitapinext.rest.RestApiBuilder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class GitRepository {

    private ObservableEmitter<IUsersModel> publishSubjectUsers;
    private ObservableEmitter<IStatus> emitterRepository;
    private CompositeDisposable cd = new CompositeDisposable();
    private RestApiBuilder api;
    private LocalDB db;

    public GitRepository(RestApiBuilder api, LocalDB db) {
        this.api = api;
        this.db = db;
    }

    public Observable<IUsersModel> subscribeUser() {
        return Observable.create(emitter -> {
            publishSubjectUsers = emitter;
            Log.i("log_tag", "subscribeUser thread  " + Thread.currentThread().getName());
            fetchUser();
            realmChangesUsers();
            getAllCount();
        });
    }

    private void realmChangesUsers() {
        Disposable disposable = db.getAllUser().asChangesetObservable().subscribe(users -> {
            Log.i("log_tag", "realmChangesUsers subscribe: " + users.getCollection()/*.getName()*/ + ":" + users.getCollection().size()/*.getAge()*/);
            publishSubjectUsers.onNext(new UsersModel(users.getCollection()));
        }, throwable -> {
            Log.i("log_tag", "realmChangesUsers throwable: " + throwable.toString());
        });
        cd.add(disposable);
    }

    private void fetchUser() {
        cd.add(new RestApiBuilder().getApi().getUserList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(users -> {
                    boolean isSaved = db.saveUser(users);
                    Log.i("log_tag", "fetchUser thread  " + Thread.currentThread().getName());
                    Log.i("log_tag", "fetchUser size  " + users.size());
                }, throwable -> {
                    Log.e("log_tag", "error " + throwable.getMessage());
                }));

    }

    public Observable<IStatus> subscribeRepository() {
        return Observable.create(emitter -> {
            Log.i("log_tag", "subscribeRepository thread  " + Thread.currentThread().getName());
            emitterRepository = emitter;
        });
    }

    public void fetchRepository(String loginName) {
        if (emitterRepository == null) {
            return;
        }
//        emitterRepository.onNext(db.getAllRepositoryByName(loginName));
        List<Repository> repositoryList = db.getAllRepositoryByName(loginName);
        if (repositoryList.size() > 0) {
            emitterRepository.onNext(new Successful(repositoryList));
        } else {
            emitterRepository.onNext(new Loading());
        }

        cd.add(api.getApi().getRepositoryList(loginName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(repositories -> {
                    for (int i = 0; i < repositories.size(); i++) {
                        Repository r = repositories.get(i);
                        r.setUserNameId(loginName);
                        db.saveRepository(r);
                    }

                    emitterRepository.onNext(new Successful(db.getAllRepositoryByName(loginName)));
                    Log.i("log_tag", "fetchRepository subscribe:  " + repositories.size());
                }, throwable -> {
                    if (repositoryList.size() > 0) {
                        emitterRepository.onNext(new Successful(repositoryList));
                    } else
                        emitterRepository.onNext(new Error());

                    Log.e("log_tag", "fetchRepository error " + throwable.getMessage());
                }));
    }

    private void getAllCount() {
        publishSubjectUsers.onNext(new CountModel(db.getAllCount()));
    }

    public void setCountByUser(String userName) {
        db.addCount(new Count(userName));
        List<Count> list = new ArrayList<>();
        list.add(new Count(userName));
        if (publishSubjectUsers != null) {
            publishSubjectUsers.onNext(new CountModel(list));
        }
        fetchRepository(userName);
    }

    public void dispose() {
        cd.clear();
        db.dispose();
    }
}
