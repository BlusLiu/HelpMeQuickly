package com.example.task;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helpmequickly_my.R;

import java.util.List;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 21:44 2019/10/22
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<Task> mTasks;

    public TaskAdapter(List<Task> mTasks) {
        this.mTasks = mTasks;
    }

    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_task, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.ViewHolder holder, int position) {
        Task task = mTasks.get(position);
        holder.name.setText(task.getName());
        holder.detail.setText(task.getDetail());
        holder.supplement.setText(task.getSupplement());
        holder.startTime.setText(task.getStartTime());
        holder.restTime.setText(task.getRestTime());
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView detail;
        TextView supplement;
        TextView startTime;
        TextView restTime;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            detail = view.findViewById(R.id.detail);
            supplement = view.findViewById(R.id.supplement);
            startTime = view.findViewById(R.id.start);
            restTime = view.findViewById(R.id.end);

        }
    }

}
