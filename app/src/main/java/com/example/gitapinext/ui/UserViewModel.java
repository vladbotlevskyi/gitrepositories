package com.example.gitapinext.ui;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.gitapinext.adapter.First.UserModel;
import com.example.gitapinext.app.AppCoordinator;
import com.example.gitapinext.model.Count;
import com.example.gitapinext.model.User;
import com.example.gitapinext.repository.GitRepository;
import com.example.gitapinext.repository.modelData.CountModel;
import com.example.gitapinext.repository.modelData.UsersModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;

public class UserViewModel extends AndroidViewModel {

    private GitRepository repository = AppCoordinator.getInstance().getRepository();
    private MutableLiveData<List<UserModel>> userLiveData = new MutableLiveData<>();
    private CompositeDisposable cd = new CompositeDisposable();
    private List<User> users;
    private Map<String, Integer> mapCounts;

    public UserViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<UserModel>> getUserLiveData() {
        return userLiveData;
    }

    void fetchData() {
        mapCounts = new HashMap<>();
        users = new ArrayList<>();
        onChangesData();
    }

    private void createUsers() {
        List<UserModel> models = new ArrayList<>();
        for (int j = 0; j < users.size(); j++) {
            User u = users.get(j);
            UserModel model = new UserModel((long) u.getId(), u.getLogin());
            for (Map.Entry c : mapCounts.entrySet()) {
                if (u.getLogin().equals(c.getKey().toString())) {
                    model.setCounter((Integer) c.getValue());
                }
            }
            models.add(model);
        }

        userLiveData.setValue(models);
    }

    private void onChangesData() {
        cd.add(repository.subscribeUser()
                .subscribe(users -> {
                    if (users instanceof UsersModel) {
                        this.users = ((UsersModel) users).users;
                        createUsers();
                    }
                    if (users instanceof CountModel) {
                        List<Count> counts = ((CountModel) users).counts;
                        for (Count c : counts) {
                            Integer count = mapCounts.get(c.getUserName());
                            if (count == null) {
                                count = 0;
                            }
                            mapCounts.put(c.getUserName(), (count + 1));
                        }
                        if (this.users.size() != 0) {
                            createUsers();
                        }
                    }
                    createUsers();
                }, throwable -> {
                    Log.e("log_tag", "onChangesData error " + throwable.toString());
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        cd.clear();
    }
}
