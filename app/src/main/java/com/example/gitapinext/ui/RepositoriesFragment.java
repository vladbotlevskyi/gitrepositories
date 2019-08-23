package com.example.gitapinext.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gitapinext.R;
import com.example.gitapinext.adapter.repositories.RepositoriesRecyclerAdapter;
import com.example.gitapinext.adapter.repositories.model.IRepositoryModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.gitapinext.utils.ConstantsApp.ARGS_LOGIN_NAME;

public class RepositoriesFragment extends Fragment {

    @BindView(R.id.repositories_recycler_view)
    RecyclerView recyclerView;

    private RepositoriesViewModel viewModel;
    private RepositoriesRecyclerAdapter repositoryAdapter;
    private Observer<List<IRepositoryModel>> observer = (list) -> {
        repositoryAdapter.setData(list);
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_repositories, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        String loginName = getArguments().getString(ARGS_LOGIN_NAME);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Repository " + loginName);

        createAdapter();
        viewModel = ViewModelProviders.of(getActivity()).get(RepositoriesViewModel.class);
        viewModel.getRepositoryModel().observe(this, observer);
        viewModel.fetchData(loginName);
    }

    private void createAdapter() {
        repositoryAdapter = new RepositoriesRecyclerAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(repositoryAdapter);
    }
}
