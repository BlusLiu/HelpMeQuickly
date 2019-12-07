package com.example.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.helpmequickly_my.R;
import com.example.task.DyTask;
import com.example.task.DyTaskAdapter;
import com.example.task.TaskPostAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DynamicFragment extends Fragment implements View.OnClickListener {
    private TextView text1;
    private TextView text2;
    private RecyclerView re;
    ArrayList<DyTask> tasks;
    ArrayList<DyTask> tasks1;
    String col;
    private SwipeRefreshLayout swipeRefresh;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_task, null);
        re= rootView.findViewById(R.id.MTrecycleView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        re.setLayoutManager(layoutManager);
        sendRequestWithOkHttp();
        text1=rootView.findViewById(R.id.myReceiveText);
        text2=rootView.findViewById(R.id.myPostText);
        text1.setSelected(true);
        text1.setOnClickListener(this);
        text2.setOnClickListener(this);
        swipeRefresh=rootView.findViewById(R.id.SwipeRefreshLayout);
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sendRequestWithOkHttp();
                swipeRefresh.setRefreshing(false);
            }
        });





        return rootView;
    }

/*    private void initTasks() {

        SharedPreferences pref=getContext().getSharedPreferences("tasks", Context.MODE_PRIVATE);
        String tasksJson=pref.getString("tasksdata","我爱你");
        //Log.d("!!!!!!!!!!!!",tasksJson);
        if (!tasksJson.equals("我爱你")) {
            Gson gson=new Gson();
            tasks=gson.fromJson(tasksJson,new TypeToken<List<DyTask>>(){}.getType());
        }

    }*/
    private void sendRequestWithOkHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().addHeader("Authorization","Bearer eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE1NzUyNzc3NDYsInVzZXJpZCI6IjEiLCJ1c2VyYWNjb3VudCI6IjEyMzQ1NiJ9.WcRMUwgf8tBZL9fLOQnwLU0k_JQnkL8zvLo88sBAmY8cJLBTOfil--ic-DUbeP6sdfgj-RR5bOqYG_m7oRQoxA")
                            .url("http://www.braisedweever.top/klb/user/ID/re_tasks?userid=4&ordertype=1&taskstate=0")
                            .build();
                    final Message message=new Message();
                    Call call=client.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("dyklb","失败");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData=response.body().string();
                            message.obj=responseData;
                            handler.sendMessage(message);


                        }
                    });



                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private ArrayList<DyTask> parseJSONWithJSONobject(String jsonData) {
        ArrayList<DyTask> tasks= new ArrayList<>();

        try{
            JSONArray ja=new JSONArray(jsonData);
            for(int i=0;i<ja.length();i++) {
                JSONObject jo = ja.getJSONObject(i);
                String taskMoney=jo.getString("TaskMoney");
                String taskStartTime=jo.getString("TaskStartTime");
                String taskEndTime=jo.getString("TaskEndTime");
                String taskTitle=jo.getString("TaskTitle");
                String taskContent=jo.getString("TaskContent");
                DyTask task=new DyTask();
                task.setResttime(taskEndTime);
                task.setStarttime(taskStartTime);
                task.setTitle(taskTitle);
                task.setContent(taskContent);
                task.setMoney("¥"+taskMoney);
                tasks.add(task);

            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return  tasks;


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.myReceiveText:

                text2.setSelected(false);
                text1.setSelected(true);
                DyTaskAdapter adapter = new DyTaskAdapter(tasks);
                re.setAdapter(adapter);
                break;
            case R.id.myPostText:

                TaskPostAdapter adapter1 = new TaskPostAdapter(tasks);
                re.setAdapter(adapter1);
                text2.setSelected(true);
                text1.setSelected(false);


                break;


        }


    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            col=msg.obj.toString();
            tasks=parseJSONWithJSONobject(col);
            DyTaskAdapter adapter=new DyTaskAdapter(tasks);
            re.setAdapter(adapter);
        }

    };

}
