package com.example.helpmequickly_my;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.sqlite.User_Database;
import com.example.task.MyTask;
import com.example.task.MyTaskAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyTaskActivity extends AppCompatActivity {

    public User_Database user;
    public SQLiteDatabase sqL_read;
    private List<MyTask> myTaskList = new ArrayList<>();
    private List banners = new ArrayList();
    private TextView type1;
    private TextView type2;
    RecyclerView recyclerView;
    ImageView comeback;
    MyTaskAdapter adapter;
    static String User_id;
    static String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  // 竖屏
        setContentView(R.layout.my_task);
        ActionBar actionbar = getSupportActionBar();        //隐藏标题栏
        if (actionbar !=null){
            actionbar.hide();
        }

        //打开一个数据库
        user = new User_Database(MyTaskActivity.this);
        sqL_read = user.getReadableDatabase();

        recyclerView = (RecyclerView) findViewById(R.id.my_list_task);
        initTask();//初始化数据
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyTaskAdapter(myTaskList);
        recyclerView.setAdapter(adapter);
        // 设置item及item中控件的点击事件
        adapter.setOnItemClickListener(MyItemClickListener);

        type1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSelect(R.id.release_left);
            }
        });
        type2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSelect(R.id.complete_right);
            }
        });
        comeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initTask(){
        type1 = (TextView)findViewById(R.id.release_left);
        type2 = (TextView)findViewById(R.id.complete_right);
        //type3 = (TextView)findViewById(R.id.task_evaluate);
        comeback  = (ImageView) findViewById(R.id.my_task_back);
        recyclerView = (RecyclerView) findViewById(R.id.my_list_task);
        //获取用户id
        get_user_id();
    }

    private MyTaskAdapter.OnItemClickListener MyItemClickListener = new MyTaskAdapter.OnItemClickListener(){
        @Override
        public void onItemClick(View v, MyTaskAdapter.ViewName viewName, int position, String input_title, String task_id){
            //viewName可区分item及item内部控件
            switch (v.getId()){
                case R.id.task_evaluate:
                    Log.d("评价按钮","当前任务主题为："+input_title);
                    Log.d("评价按钮","当前任务的id为："+task_id);
                    Toast.makeText(MyTaskActivity.this,"当前任务主题为："+input_title,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MyTaskActivity.this, WriteEvaluateActivity.class);
                    intent.setAction("action");
                    intent.putExtra("input_title",input_title);
                    intent.putExtra("task_id",task_id);
                    startActivity(intent);
                    break;
                default:
                    Toast.makeText(MyTaskActivity.this,"你点击了item按钮"+(position+1),Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void onItemLongClick(View v){

        }
    };


    private void get_user_id(){
        Cursor cursor = sqL_read.query("user",null,null,null,null,null,"1");
        if(cursor.moveToFirst()){
            do{
                token = cursor.getString(cursor.getColumnIndex("token"));
            }while(cursor.moveToNext());
        }
        cursor.close();
        OkHttpClient client = new OkHttpClient();
        //发起get请求
        Request request = new Request.Builder()
                .addHeader("Authorization",token)
                .url("http://www.braisedweever.top/klb/token/getInfo")
                .build();
        System.out.println("运行到请求已发送");
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //当get失败的时候
                e.printStackTrace();
                System.out.println("Error in Get ReleaseTask info");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //String User_id;
                        System.out.println("运行到回调成功");
                        Log.d("res", "run: "+res);
                        String exp = "" ;
                        String useraccount = "";
                        JSONObject json = JSONObject.parseObject(res);
                        exp = json.get("exp").toString();
                        User_id = json.get("userid").toString();
                        Log.d("获取用户id：","赋值，用户id:"+User_id);
                        useraccount = json.get("useraccount").toString();
                        Log.d("res:","接收成功");
                        //选择获取哪个任务列表
                        select_task_info(User_id);
                    }
                });
            }
        });
    }

    private void select_task_info(String User_id){
        Log.d("select_task_info","user_id:"+User_id);
        Intent intent = getIntent();
        if("action".equals(intent.getAction())){
            String select = intent.getStringExtra("select_item");
            if(select.equals("release_task")){
                type1.setSelected(true);
                type1.setTextSize(20);

                //调用发送请求函数
                String user_id = User_id, order_type = "0" ,task_state = "2";
                get_releasetask_info(user_id,order_type,task_state);
            }else if(select.equals("complete_task")){
                type2.setSelected(true);
                type2.setTextSize(20);

                //调用发送请求函数
                String user_id = User_id, order_type = "0" ,task_state = "5";
                get_completetask_info(user_id,order_type,task_state);
            }
        }
    }

    private void get_releasetask_info(final String user_id, String order_type, String task_state ){
        OkHttpClient client = new OkHttpClient();
        Log.d("token","token:"+token);
        //发起get请求
        Request request = new Request.Builder()
                .addHeader("Authorization",token)
                .url("http://www.braisedweever.top/klb/user/ID/tasks?userid="+user_id+"&ordertype=0&taskstate=2") //携带参数
                .build();
        System.out.println("运行到请求已发送");
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //当get失败的时候
                e.printStackTrace();
                System.out.println("Error in Get ReleaseTask info");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("运行到回调成功");
                        Log.d("res", "run: "+res);
                        String name="",state="已发布",detail="",price="",title="",taskid="";
                        if(!res.equals("[]")){
                            JSONArray array = JSON.parseArray(res);
                            for(int i=0;i<array.size();i++){
                                JSONObject json = (JSONObject) array.get(i);
                                name = json.get("UserIDRe").toString();
                                detail = json.get("TaskContent").toString();
                                price = json.get("TaskMoney").toString();
                                title = json.get("TaskTitle").toString();
                                taskid = json.get("TaskID").toString();
                                MyTask task1= new MyTask(name,state,detail,price,title,taskid);
                                myTaskList .add(task1);
                            }
                            //同步刷新
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }

                    }
                });
            }
        });
    }

    private void get_completetask_info(final String user_id, String order_type, String task_state ){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .addHeader("Authorization",token)
                .url("http://www.braisedweever.top/klb/user/ID/re_tasks?userid="+user_id+"&ordertype=0&taskstate=5") //携带参数
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //当post失败的时候
                e.printStackTrace();
                System.out.println("Error in Get ReleaseTask info");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String name="",state="已完成",detail="",price="",title="",taskid="";
                        Log.d("res", "run: "+res);
                        if(!res.equals("[]")){
                            //JSONObject jsonObject = JSON.parseObject(res);
                            JSONArray array = JSON.parseArray(res);
                            for(int i=0;i<array.size();i++){
                                JSONObject json = (JSONObject) array.get(i);
                                name = json.get("UserID").toString();
                                detail = json.get("TaskContent").toString();
                                price = json.get("TaskMoney").toString();
                                title = json.get("TaskTitle").toString();
                                taskid = json.get("TaskID").toString();
                                MyTask task2= new MyTask(name,state,detail,price,title,taskid);
                                myTaskList .add(task2);
                            }
//                            JSONArray array = JSON.parseArray(res);
//                            JSONObject json = (JSONObject) array.get(0);
//                            name = json.get("UserID").toString();
//                            detail = json.get("TaskContent").toString();
//                            price = json.get("TaskMoney").toString();
//                            MyTask task2= new MyTask(name,state,detail,price);
//                            myTaskList .add(task2);
                            //同步刷新
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }
                });
            }
        });
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1){
                adapter.notifyDataSetChanged();
            }
        }
    };

    private void changeSelect(int resId){
        type1.setSelected(false);
        type1.setTextSize(18);
        type2.setSelected(false);
        type2.setTextSize(18);
        myTaskList.clear();
        switch (resId) {
            case R.id.release_left:
                type1.setSelected(true);
                type1.setTextSize(20);
                //调用发送请求函数
                String user_id1 = User_id, order_type1 = "0" ,task_state1 = "2";
                get_releasetask_info(user_id1,order_type1,task_state1);
                break;
            case R.id.complete_right:
                type2.setSelected(true);
                type2.setTextSize(20);
                //调用发送请求函数
                String user_id2 = User_id, order_type2 = "0" ,task_state2 = "5";
                get_completetask_info(user_id2,order_type2,task_state2);
                break;
        }
    }
}
