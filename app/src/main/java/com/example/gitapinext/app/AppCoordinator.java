package com.example.gitapinext.app;

import com.example.gitapinext.repository.GitRepository;
import com.example.gitapinext.repository.LocalDB;
import com.example.gitapinext.rest.RestApiBuilder;

public class AppCoordinator {

    private static AppCoordinator appCoordinator = new AppCoordinator();
    private GitRepository repository;

    private AppCoordinator() {
    }

    public static AppCoordinator getInstance() {
        return appCoordinator;
    }

    public GitRepository getRepository() {
        if (repository == null) {
            repository = new GitRepository(new RestApiBuilder(), new LocalDB());
        }
        return repository;
    }

}
