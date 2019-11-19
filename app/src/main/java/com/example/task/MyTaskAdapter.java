package com.example.task;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.helpmequickly_my.R;

import java.util.List;


public class MyTaskAdapter extends RecyclerView.Adapter<MyTaskAdapter.ViewHolder> {

    private List<MyTask> myTaskList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView user_name;
        TextView task_state;
        TextView task_detail;
        TextView task_price;

        public ViewHolder(View view) {
            super(view);
            user_name = view.findViewById(R.id.user_name);
            task_state = view.findViewById(R.id.task_state);
            task_detail = view.findViewById(R.id.task_detail);
            task_price = view.findViewById(R.id.task_price);
        }
    }

    public MyTaskAdapter(List<MyTask> myTaskList){
        this.myTaskList = myTaskList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_task_little,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        MyTask myTask = myTaskList.get(position);
        holder.user_name.setText(myTask.getUserName());
        holder.task_state.setText(myTask.getTaskState());
        holder.task_detail.setText(myTask.getTaskDetail());
        holder.task_price.setText(myTask.getTaskPrice());
    }

    @Override
    public int getItemCount(){
        return myTaskList.size();
    }
}
