package com.example.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.helpmequickly_my.R;
import com.example.task.ImLoader;
import com.example.task.Task;
import com.example.task.TaskAdapter;
import com.youth.banner.Banner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class HomeFragment extends Fragment {

    private List<Task> mTasks = new ArrayList<>();
    private List banners = new ArrayList();
    private TextView type1;
    private TextView type2;
    private TextView type3;
    private   RecyclerView  recyclerView;
    String url;
    private ImageView run;
    private SwipeRefreshLayout swipeRefresh;

    String col;
//url:"http://www.braisedweever.top/klb/tasks?limitstart=0&limitnumber=10&typeid=1&order=0&isdesc=true&state=0&startTime&endTime"
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home, null);
            recyclerView = rootView.findViewById(R.id.list_task);
            type1 = (TextView) rootView.findViewById(R.id.t1);
            type2 = (TextView) rootView.findViewById(R.id.t2);
        type3 = (TextView) rootView.findViewById(R.id.t3);
        type1.setSelected(true);
        Banner banner = (Banner) rootView.findViewById(R.id.banner);
        banner.setImageLoader(new ImLoader());
        banner.setImages(banners);
        banner.start();
        GridLayoutManager gridLayoutManager= new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        run=rootView.findViewById(R.id.run);
        type1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSelect(R.id.t1);
            }
        });
        type2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSelect(R.id.t2);
            }
        });
        type3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSelect(R.id.t3);
            }
        });
        run.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                url="http://www.braisedweever.top/klb/tasks?limitstart=0&limitnumber=10&order=0&isdesc=true&state=0&startTime&endTime&typeid="+2;
                sendRequestWithOkHttp(url);

            }
        });
        sendRequestWithOkHttp("http://www.braisedweever.top/klb/tasks?limitstart=0&limitnumber=10&typeid=1&order=0&isdesc=true&state=0&startTime&endTime");
        swipeRefresh=rootView.findViewById(R.id.homeswiperefreshlayout);
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /*url="http://www.braisedweever.top/klb/tasks?limitstart=0&limitnumber=10&order=0&isdesc=true&state=0&startTime&endTime&typeid=1";
                sendRequestWithOkHttp(url);*/
                List<Task> Tasks = new ArrayList<>();
                Tasks.add(mTasks.get(0));
                final TaskAdapter adapter = new TaskAdapter(Tasks);
                recyclerView.setAdapter(adapter);
                swipeRefresh.setRefreshing(false);
            }
        });
        return rootView;

    }



    private void changeSelect(int resId){
        type1.setSelected(false);
        type2.setSelected(false);
        type3.setSelected(false);
        switch (resId) {
            case R.id.t1:
                type1.setSelected(true);
                break;
            case R.id.t2:
                type2.setSelected(true);
                break;
            case R.id.t3:
                type3.setSelected(true);
                break;
        }
    }
    private void sendRequestWithOkHttp(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().addHeader("Authorization","Bearer eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE1NzY1ODkyMDUsInVzZXJpZCI6IjEiLCJ1c2VyYWNjb3VudCI6IjEyMzQ1NiJ9._n6EI3qDuJ-xSnPlGh9H3vrb9ASaS4Fpctw71KDESDULMYnqQSkgaH8me_kkb3PdfoTezLb8xrtica-q6zswCQ")
                            .url(url)
                            .build();
                    final Message message=new Message();
                    Call call=client.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("home","失败");
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
    private List<Task> parseJSONWithJSONobject(String jsonData) {
        List<Task> tasks= new ArrayList<>();

        try{
            JSONArray ja=new JSONArray(jsonData);
            for(int i=0;i<ja.length();i++) {
                JSONObject jo = ja.getJSONObject(i);
                String taskStartTime=jo.getString("TaskStartTime");
                String taskEndTime=jo.getString("TaskEndTime");
                String taskTitle=jo.getString("TaskTitle");
                String taskContent=jo.getString("TaskContent");
                String taskID=jo.getString("TaskID");
                Task task=new Task();
                SimpleDateFormat format = new SimpleDateFormat("MM-dd-HH:mm");
                long st=Long.parseLong(taskStartTime);
                long et=Long.parseLong(taskEndTime);
                task.setRestTime(format.format(st));
                task.setStartTime(format.format(et));
                task.setName(taskTitle);
                task.setDetail(taskContent);
                task.setTaskID(taskID);
                tasks.add(task);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return  tasks;


    }
    Handler handler=new Handler() {
        @Override
        public void handleMessage(@NonNull final Message msg) {
            col = msg.obj.toString();
             mTasks= parseJSONWithJSONobject(col);
            final TaskAdapter adapter = new TaskAdapter(mTasks);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v,int position) {
                    String url= "http://www.braisedweever.top/klb/task/ID";
                    sendRequestWithOkHttpByPut(url,mTasks.get(position).getTaskID());
                   // Log.d("hello",mTasks.get(position).getName());


                }
            });

        }
    };
    private void sendRequestWithOkHttpByPut(final String url, final String TaskID){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                  OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody=new FormBody.Builder()
                            .add("taskid",TaskID)
                            .build();

                    Request request = new Request.Builder().addHeader("Authorization","Bearer eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE1NzY1ODkyMDUsInVzZXJpZCI6IjEiLCJ1c2VyYWNjb3VudCI6IjEyMzQ1NiJ9._n6EI3qDuJ-xSnPlGh9H3vrb9ASaS4Fpctw71KDESDULMYnqQSkgaH8me_kkb3PdfoTezLb8xrtica-q6zswCQ")
                            .url("http://www.braisedweever.top/klb/task/ID/getTask/test")
                            .post(requestBody)
                            .build();
                    Call call=client.newCall(request);
                    call.enqueue(new Callback() {
                       @Override
                       public void onFailure(Call call, IOException e) {
                      Log.d("sorry","sorr");
                       }

                       @Override
                       public void onResponse(Call call, Response response) throws IOException {
                           String responseData=response.body().string();
                        /*   Log.d("s",responseData);
                         String  url="http://www.braisedweever.top/klb/tasks?limitstart=0&limitnumber=10&order=0&isdesc=true&state=0&startTime&endTime&typeid=1";
                           sendRequestWithOkHttp(url);
                           Log.d("s",responseData);*/

                       }



                  /* String responseData=response.body().string();
                    Log.d("s",responseData);*/
                   });

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
