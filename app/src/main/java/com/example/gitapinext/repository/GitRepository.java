package com.example.gitapinext.repository;

import android.util.Log;

import com.example.gitapinext.model.Count;
import com.example.gitapinext.model.Repository;
import com.example.gitapinext.repository.modelData.CountModel;
import com.example.gitapinext.repository.modelData.IUsersModel;
import com.example.gitapinext.repository.modelData.UsersModel;
import com.example.gitapinext.repository.modelStatus.Error;
import com.example.gitapinext.repository.modelStatus.IStatusRepository;
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

    private ObservableEmitter<IUsersModel> emitterUsers;
    private ObservableEmitter<IStatusRepository> emitterRepository;
    private CompositeDisposable cd = new CompositeDisposable();
    private RestApiBuilder api;
    private LocalDB db;

    public GitRepository(RestApiBuilder api, LocalDB db) {
        this.api = api;
        this.db = db;
    }

    public Observable<IUsersModel> subscribeUser() {
        return Observable.create(emitter -> {
            emitterUsers = emitter;
            fetchUser();
            realmChangesUsers();
            getAllCount();
        });
    }

    private void realmChangesUsers() {
        Disposable disposable = db.getAllUser().asChangesetObservable().subscribe(users -> {
            emitterUsers.onNext(new UsersModel(users.getCollection()));
        }, throwable -> {
            Log.e("log_tag", throwable.toString());
        });
        cd.add(disposable);
    }

    private void fetchUser() {
        cd.add(new RestApiBuilder().getApi().getUserCall()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(users -> {
                    boolean isSaved = db.saveUser(users);
                }, throwable -> {
                    Log.e("log_tag", "error " + throwable.getMessage());
                }));

    }

    public Observable<IStatusRepository> subscribeRepository() {
        return Observable.create(emitter -> {
            emitterRepository = emitter;
        });
    }

    public void fetchRepository(String loginName) {
        if (emitterRepository == null) {
            return;
        }
        List<Repository> realmRepositoryList = db.getAllRepositoryByName(loginName);
        if (realmRepositoryList.size() > 0) {
            emitterRepository.onNext(new Successful(realmRepositoryList));
        } else {
            emitterRepository.onNext(new Loading());
        }

        cd.add(api.getApi().getRepositoryCall(loginName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(repositories -> {
                    for (int i = 0; i < repositories.size(); i++) {
                        Repository r = repositories.get(i);
                        r.setUserNameId(loginName);
                        db.saveRepository(r);
                    }

                    emitterRepository.onNext(new Successful(db.getAllRepositoryByName(loginName)));
                }, throwable -> {
                    if (realmRepositoryList.size() > 0) {
                        emitterRepository.onNext(new Successful(realmRepositoryList));
                    } else
                        emitterRepository.onNext(new Error());

                    Log.e("log_tag", "fetchRepository error " + throwable.getMessage());
                }));
    }

    private void getAllCount() {
        emitterUsers.onNext(new CountModel(db.getAllCount()));
    }

    public void setCountByUser(String userName) {
        db.addCount(new Count(userName));
        List<Count> list = new ArrayList<>();
        list.add(new Count(userName));
        if (emitterUsers != null) {
            emitterUsers.onNext(new CountModel(list));
        }
        if (emitterRepository != null) {
            fetchRepository(userName);
        }
    }

    public void dispose() {
        cd.clear();
        db.dispose();
    }
}
