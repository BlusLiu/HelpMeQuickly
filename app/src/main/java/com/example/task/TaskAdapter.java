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
    private OnItemClickListener onItemClickListener = null;

    //setter方法
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //回调接口
    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

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
    public void onBindViewHolder(@NonNull TaskAdapter.ViewHolder holder, final int position) {
        holder.itemView.findViewById(R.id.home_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, position);
                }
            }
        });
        Task task = mTasks.get(position);
        holder.name.setText(task.getName());
        holder.detail.setText(task.getDetail());
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

        TextView startTime;
        TextView restTime;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            detail = view.findViewById(R.id.detail);
            startTime = view.findViewById(R.id.start);
            restTime = view.findViewById(R.id.end);

        }
    }

}
