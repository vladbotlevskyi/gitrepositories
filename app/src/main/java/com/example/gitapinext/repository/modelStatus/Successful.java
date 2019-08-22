package com.example.gitapinext.repository.modelStatus;

import com.example.gitapinext.model.Repository;

import java.util.List;

public class Successful implements IStatus {

    public List<Repository> repository;

    public Successful(List<Repository> repository) {
        this.repository = repository;
    }
}
