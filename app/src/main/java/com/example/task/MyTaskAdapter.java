package com.example.task;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.helpmequickly_my.R;

import java.util.List;


public class MyTaskAdapter extends RecyclerView.Adapter<MyTaskAdapter.ViewHolder> implements View.OnClickListener {

    private List<MyTask> myTaskList;
    private Context context;//上下文
    public MyTaskAdapter(List<MyTask> myTaskList, Context context){
        this.myTaskList = myTaskList;
        this.context = context;
    }

    //static String input_title;
    //static String input_taskid;
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView user_name;
        TextView task_state;
        TextView task_detail;
        TextView task_price;
        TextView task_title;
        TextView task_evaluate;
        TextView task_id;

        public ViewHolder(View view) {
            super(view);
            user_name = view.findViewById(R.id.user_name);
            task_state = view.findViewById(R.id.task_state);
            task_detail = view.findViewById(R.id.task_detail);
            task_price = view.findViewById(R.id.task_price);
            task_title = view.findViewById(R.id.task_title);
            task_evaluate = view.findViewById(R.id.task_evaluate);
            task_id = view.findViewById(R.id.task_id);

            // 为ItemView添加点击事件
            //view.setOnClickListener(MyTaskAdapter.this);
            task_evaluate.setOnClickListener(MyTaskAdapter.this);
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
        holder.task_title.setText(myTask.getTaskTitle());
        holder.task_id.setText(myTask.getTaskId());
        holder.task_evaluate.setTag(position);

    }

    @Override
    public int getItemCount(){
        return myTaskList.size();
    }



    //=======================以下为item中的button控件点击事件处理===================================

    //item里面有多个控件可以点击（item+item内部控件）
    public enum ViewName{
        ITEM,
        PRACTISE
    }

    //自定义一个回调接口来实现Click和LongClick事件
    public interface OnItemClickListener{
        void onItemClick(View v, ViewName viewName, int position, String input_title, String task_id);
        void onItemLongClick(View v);
    }

    private OnItemClickListener mOnItemClickListener;//声明自定义的接口

    //定义方法并传给外面的使用者
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View v){
        int position = (int) v.getTag();    //getTag()获取数据
        if(mOnItemClickListener != null){
            switch (v.getId()){
                case R.id.my_list_task:
                    mOnItemClickListener.onItemClick(v, ViewName.PRACTISE,position,"","");
                    break;
                default:
                    String input_title,taskid;
                    MyTask getTaskInfo = myTaskList.get(position);
                    input_title = getTaskInfo.getTaskTitle();
                    taskid = getTaskInfo.getTaskId();
                    Log.d("传递数据处","input_title:"+input_title);
                    mOnItemClickListener.onItemClick(v, ViewName.ITEM,position,input_title,taskid);
                    break;
            }
        }
    }
}

