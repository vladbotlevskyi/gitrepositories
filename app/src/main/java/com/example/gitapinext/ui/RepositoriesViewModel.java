package com.example.gitapinext.ui;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gitapinext.adapter.repositories.model.IRepositoryModel;
import com.example.gitapinext.adapter.repositories.model.RepositoryError;
import com.example.gitapinext.adapter.repositories.model.RepositoryLoader;
import com.example.gitapinext.adapter.repositories.model.RepositoryModel;
import com.example.gitapinext.app.AppCoordinator;
import com.example.gitapinext.model.Repository;
import com.example.gitapinext.repository.GitRepository;
import com.example.gitapinext.repository.modelStatus.Error;
import com.example.gitapinext.repository.modelStatus.Loading;
import com.example.gitapinext.repository.modelStatus.Successful;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class RepositoriesViewModel extends AndroidViewModel {

    private GitRepository repository = AppCoordinator.getInstance().getRepository();
    private MutableLiveData<List<IRepositoryModel>> liveData = new MutableLiveData<>();
    private CompositeDisposable cd = new CompositeDisposable();
    private List<IRepositoryModel> models = new ArrayList<>();

    public RepositoriesViewModel(@NonNull Application application) {
        super(application);
        onChangesData();
    }

    public LiveData<List<IRepositoryModel>> getRepositoryModel() {
        return liveData;
    }

    public void fetchData(String name) {
        models.clear();
        repository.fetchRepository(name);
    }

    public void onChangesData() {
        models.clear();
        Log.i("log_tag", "before fetchRepository");
        cd.add(repository.subscribeRepository()
                .subscribe(result -> {
                    if (result instanceof Loading) {
                        liveData.setValue(new ArrayList<IRepositoryModel>() {{
                            add(new RepositoryLoader());
                        }});
                    } else if (result instanceof Successful) {
                        List<Repository> list = ((Successful) result).repository;
                        Log.i("log_tag", "after fetchRepository: " + list.size());
                        for (int i = 0; i < list.size(); i++) {
                            Repository r = (Repository) list.get(i);
                            models.add(new RepositoryModel("" + r.getId(), r.getName()));
                        }
                        liveData.setValue(models);
                    } else if (result instanceof Error) {
                        liveData.setValue(new ArrayList<IRepositoryModel>() {{
                            add(new RepositoryError());
                        }});
                    }

                }, throwable -> {
                    Log.i("log_tag", "fetchRepository thread  " + Thread.currentThread().getName());

                    Log.e("log_tag", "fetchRepository: " + throwable.getMessage());
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        cd.dispose();
    }
}
