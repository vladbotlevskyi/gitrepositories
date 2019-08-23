package com.example.gitapinext.rest;

import com.example.gitapinext.model.Repository;
import com.example.gitapinext.model.User;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RestApi {
    @GET("/users")
    Single<List<User>> getUserCall();

    @GET("/users/{login}/repos")
    Single<List<Repository>> getRepositoryCall(@Path("login") String login);
}
