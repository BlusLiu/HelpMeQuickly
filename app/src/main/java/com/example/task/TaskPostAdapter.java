package com.example.task;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helpmequickly_my.R;

import java.util.List;

public class TaskPostAdapter extends RecyclerView.Adapter<DyTaskAdapter.ViewHolder>{
        private List<DyTask> mtasklist;
        static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView content;
        TextView addition;
        TextView starttime;
        TextView resttime;
        TextView money;
        public ViewHolder(View view){
            super(view);
            title=view.findViewById(R.id.MTname);
            content=view.findViewById(R.id.MTcontent);
            addition=view.findViewById(R.id.MTaddition);
            starttime=view.findViewById(R.id.MTstarttime);
            resttime=view.findViewById(R.id.MTresttime);
            money=view.findViewById(R.id.money);
        }
    }
        public TaskPostAdapter(List<DyTask> tasklist){
            mtasklist=tasklist;
        }

        @NonNull
        @Override
        public DyTaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_my_task_post_item,parent,false);
            DyTaskAdapter.ViewHolder holder=new DyTaskAdapter.ViewHolder(view);

            return holder;
        }

    @Override
    public void onBindViewHolder(@NonNull DyTaskAdapter.ViewHolder holder, int position) {
        DyTask task=mtasklist.get(position);
        holder.title.setText(task.getTitle());
        holder.content.setText(task.getContent());
        holder.addition.setText(task.getAddition());
        holder.starttime.setText(task.getStarttime());
        holder.resttime.setText(task.getResttime());
        holder.money.setText(task.getMoney());
    }



    @Override
    public int getItemCount() {
        return mtasklist.size();
    }
}
