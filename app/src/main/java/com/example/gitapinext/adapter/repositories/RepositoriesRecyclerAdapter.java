package com.example.gitapinext.adapter.repositories;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gitapinext.R;
import com.example.gitapinext.adapter.repositories.model.IRepositoryModel;
import com.example.gitapinext.adapter.repositories.model.RepositoryError;
import com.example.gitapinext.adapter.repositories.model.RepositoryLoader;
import com.example.gitapinext.adapter.repositories.model.RepositoryModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RepositoriesRecyclerAdapter extends RecyclerView.Adapter<RepositoriesRecyclerAdapter.ViewHolder> {
    private List<IRepositoryModel> firstModels = new ArrayList<>();
    private int MODEL = 1;
    private int LOADER = 2;
    private int ERROR = 3;

    public void setData(List<IRepositoryModel> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new RepositoriesDiffUtilCallback(firstModels, newList));
        firstModels.clear();
        firstModels.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemViewType(int position) {
        IRepositoryModel model = firstModels.get(position);
        if (model instanceof RepositoryModel) {
            return MODEL;
        }
        if (model instanceof RepositoryLoader) {
            return LOADER;
        }
        if (model instanceof RepositoryError) {
            return ERROR;
        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MODEL) {
            return new ViewHolderModel(LayoutInflater.from(parent.getContext()).inflate(R.layout.repository_item, parent, false));
        }
        if (viewType == LOADER) {
            return new ViewHolderLoader(LayoutInflater.from(parent.getContext()).inflate(R.layout.loader_item, parent, false));
        }
        return new ViewHolderError(LayoutInflater.from(parent.getContext()).inflate(R.layout.error_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (firstModels.get(position) instanceof RepositoryModel) {
            RepositoryModel model = (RepositoryModel) firstModels.get(position);
            ((ViewHolderModel) holder).bind(model);
        }
    }

    @Override
    public int getItemCount() {
        return firstModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class ViewHolderLoader extends ViewHolder {

        public ViewHolderLoader(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class ViewHolderError extends ViewHolder {

        public ViewHolderError(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class ViewHolderModel extends ViewHolder {
        @BindView(R.id.repository_item_id)
        TextView id;
        @BindView(R.id.repository_item_name)
        TextView name;

        public ViewHolderModel(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(RepositoryModel model) {
            name.setText(model.getName());
            id.setText(model.getId());
        }
    }
}
