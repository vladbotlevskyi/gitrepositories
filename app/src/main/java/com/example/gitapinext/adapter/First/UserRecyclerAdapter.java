package com.example.gitapinext.adapter.First;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gitapinext.R;
import com.example.gitapinext.utils.OnClickNameListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.ViewHolder> {
    private List<UserModel> userModels = new ArrayList<>();
    private OnClickNameListener nameListener;

    public UserRecyclerAdapter(OnClickNameListener listener) {
        this.nameListener = listener;
    }

    public void setData(List<UserModel> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new UserDiffUtilCallback(userModels, newList));
        userModels.clear();
        userModels.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserModel model = userModels.get(position);
        holder.bind(model);
    }

    @Override
    public int getItemCount() {
        return userModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.main_item_text)
        TextView name;
        @BindView(R.id.main_item_count)
        TextView counter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(UserModel model) {
            name.setText(model.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nameListener.onClickName(model.getName());
                }
            });
            int count = model.getCounter();

            if (count == 0) {
                counter.setVisibility(View.GONE);
            } else {
                counter.setVisibility(View.VISIBLE);
                counter.setText(count + "");
            }
        }

    }
}
