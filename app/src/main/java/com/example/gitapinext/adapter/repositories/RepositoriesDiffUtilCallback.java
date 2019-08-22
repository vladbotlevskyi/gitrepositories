package com.example.gitapinext.adapter.repositories;

import androidx.recyclerview.widget.DiffUtil;

import com.example.gitapinext.adapter.repositories.model.IRepositoryModel;
import com.example.gitapinext.adapter.repositories.model.RepositoryError;
import com.example.gitapinext.adapter.repositories.model.RepositoryLoader;
import com.example.gitapinext.adapter.repositories.model.RepositoryModel;

import java.util.List;

public class RepositoriesDiffUtilCallback extends DiffUtil.Callback {

    private List<IRepositoryModel> oldList;
    private List<IRepositoryModel> newList;

    public RepositoriesDiffUtilCallback(List<IRepositoryModel> oldList, List<IRepositoryModel> newList) {
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
        IRepositoryModel oldModel = oldList.get(oldItemPosition);
        IRepositoryModel newModel = newList.get(newItemPosition);
        if (oldModel instanceof RepositoryModel && newModel instanceof RepositoryModel) {
            return ((RepositoryModel) newModel).getId().equals(((RepositoryModel) oldModel).getId());
        }
        if (oldModel instanceof RepositoryLoader && newModel instanceof RepositoryLoader) {
            return newModel.equals(oldModel);
        }
        if (oldModel instanceof RepositoryError && newModel instanceof RepositoryError) {
            return newModel.equals(oldModel);
        }
        return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        IRepositoryModel oldModel = oldList.get(oldItemPosition);
        IRepositoryModel newModel = newList.get(newItemPosition);
        if (oldModel instanceof RepositoryModel && newModel instanceof RepositoryModel) {
            return ((RepositoryModel) newModel).getId().equals(((RepositoryModel) oldModel).getId()) &&
                    ((RepositoryModel) newModel).getName().equals(((RepositoryModel) oldModel).getName());
        }
        if (oldModel instanceof RepositoryLoader && newModel instanceof RepositoryLoader) {
            return newModel.equals(oldModel);
        }
        if (oldModel instanceof RepositoryError && newModel instanceof RepositoryError) {
            return newModel.equals(oldModel);
        }
        return false;
//        return newModel.equals(oldModel);
    }
}
