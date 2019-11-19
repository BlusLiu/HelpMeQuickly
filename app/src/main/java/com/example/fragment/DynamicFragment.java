package com.example.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helpmequickly_my.R;
import com.example.task.DyTask;
import com.example.task.DyTaskAdapter;
import com.example.task.Task;
import com.example.task.TaskAdapter;
import com.example.task.TaskPostAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DynamicFragment extends Fragment implements View.OnClickListener {
    private TextView text1;
    private TextView text2;
    private RecyclerView re;
    ArrayList<DyTask> tasks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_task, null);
        re= rootView.findViewById(R.id.MTrecycleView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        re.setLayoutManager(layoutManager);
        initTasks();
        DyTaskAdapter adapter=new DyTaskAdapter(tasks);
        re.setAdapter(adapter);
        text1=rootView.findViewById(R.id.myReceiveText);
        text2=rootView.findViewById(R.id.myPostText);
        text1.setSelected(true);
        text1.setOnClickListener(this);
        text2.setOnClickListener(this);




        return rootView;
    }

    private void initTasks() {
        sendRequestWithOkHttp();
        SharedPreferences pref=getContext().getSharedPreferences("tasks", Context.MODE_PRIVATE);
        String tasksJson=pref.getString("tasksdata","我爱你");
        //Log.d("!!!!!!!!!!!!",tasksJson);
        if (!tasksJson.equals("我爱你")) {
            Gson gson=new Gson();
            tasks=gson.fromJson(tasksJson,new TypeToken<List<DyTask>>(){}.getType());
        }

    }
    private void sendRequestWithOkHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().addHeader("Authorization","Bearer eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE1NzUyNzc3NDYsInVzZXJpZCI6IjEiLCJ1c2VyYWNjb3VudCI6IjEyMzQ1NiJ9.WcRMUwgf8tBZL9fLOQnwLU0k_JQnkL8zvLo88sBAmY8cJLBTOfil--ic-DUbeP6sdfgj-RR5bOqYG_m7oRQoxA")
                            .url("http://www.braisedweever.top/klb/user/ID/re_tasks?userid=4&ordertype=1&taskstate=0")
                            .build();
                    Response response=client.newCall(request).execute();
                    String responseData=response.body().string();
                    tasks=parseJSONWithJSONobject(responseData);
                    Gson gson=new Gson();
                    String tasksJson=gson.toJson(tasks);
                    //Log.d("test11111",tasksJson);
                    SharedPreferences.Editor editor=getContext().getSharedPreferences("tasks", Context.MODE_PRIVATE).edit();
                    editor.putString("tasksdata",tasksJson);
                    editor.apply();



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

                //sendOkHttp
                break;
            case R.id.myPostText:

                TaskPostAdapter adapter1 = new TaskPostAdapter(tasks);
                re.setAdapter(adapter1);
                text2.setSelected(true);
                text1.setSelected(false);
                //sendOkHttp

                break;


        }


    }
}
