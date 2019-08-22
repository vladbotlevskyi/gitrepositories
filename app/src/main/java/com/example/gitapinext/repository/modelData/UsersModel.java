package com.example.gitapinext.repository.modelData;

import com.example.gitapinext.model.User;

import java.util.List;

public class UsersModel extends IUsersModel {
    public List<User> users;

    public UsersModel(List<User> users) {
        this.users = users;
    }

    //    @Override
    public Object getValueList() {
        return users;
    }
}

