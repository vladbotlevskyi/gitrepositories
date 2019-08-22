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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gitapinext.R;
import com.example.gitapinext.adapter.First.UserModel;
import com.example.gitapinext.adapter.First.UserRecyclerAdapter;
import com.example.gitapinext.utils.OnClickNameListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.gitapinext.utils.ConstantsApp.ARGS_LOGIN_NAME;

public class FirstFragment extends Fragment implements OnClickNameListener {

    @BindView(R.id.first_recycler_view)
    RecyclerView recyclerView;

    private NavController navController;
    private UserRecyclerAdapter userRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("First fragment");
    }

    private Observer<List<UserModel>> userObserver = new Observer<List<UserModel>>() {
        @Override
        public void onChanged(List<UserModel> list) {
            userRecyclerAdapter.setData(list);
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        createAdapter();
        navController = Navigation.findNavController(view);
        final UserViewModel viewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        viewModel.getUserLiveData().observe(this, userObserver);
        viewModel.fetchData();
    }

    private void createAdapter() {
        userRecyclerAdapter = new UserRecyclerAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(userRecyclerAdapter);
    }

    @Override
    public void onClickName(String name) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGS_LOGIN_NAME, name);
        navController.navigate(R.id.action_firstFragment_to_repositoriesFragment, bundle);
    }
}
