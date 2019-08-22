package com.example.gitapinext.rest;

import java.util.List;

import com.example.gitapinext.model.Repository;
import com.example.gitapinext.model.User;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RestApi {
    @GET("/users")
    Single<List<User>> getUserList();

    @GET("/users/{login}/repos")
    Single<List<Repository>> getRepositoryList(@Path("login") String login);
}
