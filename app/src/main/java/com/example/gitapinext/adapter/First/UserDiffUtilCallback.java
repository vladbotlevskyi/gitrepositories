package com.example.gitapinext.adapter.First;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class UserDiffUtilCallback extends DiffUtil.Callback {

    private List<UserModel> oldList;
    private List<UserModel> newList;

    public UserDiffUtilCallback(List<UserModel> oldList, List<UserModel> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        UserModel oldModel = oldList.get(oldItemPosition);
        UserModel newModel = newList.get(newItemPosition);
        return newModel.getId().equals(oldModel.getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        UserModel oldModel = oldList.get(oldItemPosition);
        UserModel newModel = newList.get(newItemPosition);
        if (newModel.getCounter() != oldModel.getCounter()) return false;
        return newModel.equals(oldModel);
    }
}
