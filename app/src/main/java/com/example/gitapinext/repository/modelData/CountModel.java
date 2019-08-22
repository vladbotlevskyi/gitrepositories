package com.example.gitapinext.repository.modelData;

import com.example.gitapinext.model.Count;

import java.util.List;

public class CountModel extends IUsersModel {
    public List<Count> counts;

    public CountModel(List<Count> counts) {
        this.counts = counts;
    }

    //    @Override
    public Object getValueList() {
        return null;
    }
}
