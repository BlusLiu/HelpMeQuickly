package com.example.helpmequickly_my;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.evaluate.Evaluate;
import com.example.evaluate.EvaluateAdapter;
import com.example.sqlite.User_Database;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyEvaluateActivity extends AppCompatActivity {

    public User_Database user;
    public SQLiteDatabase sqL_read;
    private List<Evaluate> mEvaluates = new ArrayList<>();
    private List banners = new ArrayList();
    private TextView type1;
    private TextView type2;
    private TextView type3;
    RecyclerView recyclerView;
    ImageView comeback;
    EvaluateAdapter adapter;
    static String User_id;
    static String token;
    static  int flag = 0;
    private List<Evaluate> mEvaluatesAll = new ArrayList<>();
    private List<Evaluate> mEvaluatesSend = new ArrayList<>();
    private List<Evaluate> mEvaluatesReceive = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  // 竖屏
        setContentView(R.layout.my_evaluate);
        ActionBar actionbar = getSupportActionBar();        //隐藏标题栏
        if (actionbar !=null){
            actionbar.hide();
        }
        //打开一个数据库
        user = new User_Database(MyEvaluateActivity.this);
        sqL_read = user.getReadableDatabase();
        recyclerView = (RecyclerView) findViewById(R.id.my_list_evaluate);
        initEvaluate();//初始化数据
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new EvaluateAdapter(mEvaluates);
        recyclerView.setAdapter(adapter);
        type1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSelect(R.id.all_evaluate);
            }
        });
        type2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSelect(R.id.release_evaluate);
            }
        });
        type3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSelect(R.id.receive_evaluate);
            }
        });
        comeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initEvaluate(){
        type1 = (TextView)findViewById(R.id.all_evaluate);
        type2 = (TextView)findViewById(R.id.release_evaluate);
        type3 = (TextView)findViewById(R.id.receive_evaluate);
        comeback  = (ImageView) findViewById(R.id.my_evaluate_back);
        type1.setSelected(true);
        type1.setTextSize(20);
        //获取用户id
        get_user_id();

    }

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
                        //默认获取全部评价列表
                        get_all_evaluate(User_id,"0","2");
                    }
                });
            }
        });
    }
    /**
     * GMY
     * @param user_id
     * @param order_type
     * @param evaluate_state
     */
    private void get_all_evaluate(final String user_id, String order_type, String evaluate_state ){
        Log.d("user_id","get_all_evaluate中user_id为："+user_id);
        OkHttpClient client = new OkHttpClient();
        Log.d("token","token:"+token);
        //发起get请求
        Request request = new Request.Builder()
                .addHeader("Authorization",token)
                .url("http://www.braisedweever.top/klb/user/ID/LeaveMessages?userID="+user_id) //携带参数
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
                        String user_name="",theme="",content="",ratingbar="",date="",evaluate_state="";
                        if(!res.equals("[]")){
                            JSONArray array = JSON.parseArray(res);
                            for(int i=0;i<array.size();i++){
                                JSONObject json = (JSONObject) array.get(i);
                                user_name = "小李";
                                theme = "任务主题";
                                content = json.get("LeaveContent").toString();
                                ratingbar = json.get("UserScore").toString();
                                date = json.get("LeaveTime").toString();
                                evaluate_state = "发布评价";
                                float Ratingbar = Float.parseFloat(ratingbar);  //将String类型转为float
                                Long long1 = Long.valueOf(date);    //long类型时间
                                String date_time = formatTime("yyyy-MM-dd",long1);
                                Evaluate evaluate3 = new Evaluate(user_name,theme,date_time,evaluate_state,Ratingbar,content);
                                mEvaluates.add(evaluate3);
                                mEvaluatesSend.add(evaluate3);

                            }
                            //同步刷新
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);

                            Message messageSend = new Message();
                            messageSend.what = 2;
                            messageSend.obj = mEvaluatesSend;
                            handler.sendMessage(messageSend);
                        }
                    }
                });
            }
        });



        OkHttpClient client_one = new OkHttpClient();
        Log.d("token","token:"+token);
        //发起get请求
        Request request_one = new Request.Builder()
                .addHeader("Authorization",token)
                .url("http://www.braisedweever.top/klb/User/ID/ReLeaveMessage?userID="+user_id) //携带参数
                .build();
        System.out.println("运行到请求已发送");
        Call call_one = client_one.newCall(request_one);
        call_one.enqueue(new Callback() {
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
                        String user_name="",theme="",content="",ratingbar="",date="",evaluate_state="";
                        if(!res.equals("[]")){
                            JSONArray array = JSON.parseArray(res);
                            for(int i=0;i<array.size();i++){
                                JSONObject json = (JSONObject) array.get(i);
                                user_name = "小李";
                                theme = "任务主题";
                                content = json.get("LeaveContent").toString();
                                ratingbar = json.get("UserScore").toString();
                                date = json.get("LeaveTime").toString();
                                evaluate_state = "收到评价";
                                float Ratingbar = Float.parseFloat(ratingbar);  //将String类型转为float
                                Long long1 = Long.valueOf(date);    //long类型时间
                                String date_time = formatTime("yyyy-MM-dd",long1);
                                Evaluate evaluate3 = new Evaluate(user_name,theme,date_time,evaluate_state,Ratingbar,content);
                                mEvaluates.add(evaluate3);
                                mEvaluatesReceive.add(evaluate3);
                            }

                            //同步刷新
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);

                            Message messageReceive = new Message();
                            messageReceive.what = 2;
                            messageReceive.obj = mEvaluatesReceive;
                            handler.sendMessage(messageReceive);
                        }
                    }
                });
            }
        });
    }

//    private void get_release_evaluate(final String user_id, String order_type, String evaluate_state){
//        OkHttpClient client = new OkHttpClient();
//        Log.d("token","token:"+token);
//        //发起get请求
//        Request request = new Request.Builder()
//                .addHeader("Authorization",token)
//                .url("http://www.braisedweever.top/klb/user/ID/LeaveMessages?userID="+user_id) //携带参数
//                .build();
//        System.out.println("运行到请求已发送");
//        okhttp3.Call call = client.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                //当get失败的时候
//                e.printStackTrace();
//                System.out.println("Error in Get ReleaseTask info");
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                final String res = response.body().string();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        System.out.println("运行到回调成功");
//                        Log.d("res", "run: "+res);
//                        String user_name="",theme="",content="",ratingbar="",date="",evaluate_state="";
//                        if(!res.equals("[]")){
//                            JSONArray array = JSON.parseArray(res);
//                            for(int i=0;i<array.size();i++){
//                                JSONObject json = (JSONObject) array.get(i);
//                                user_name = "小李";
//                                theme = "任务主题";
//                                content = json.get("LeaveContent").toString();
//                                ratingbar = json.get("UserScore").toString();
//                                date = json.get("LeaveTime").toString();
//                                evaluate_state = "发布评价";
//                                float Ratingbar = Float.parseFloat(ratingbar);  //将String类型转为float
//                                Long long1 = Long.valueOf(date);    //long类型时间
//                                String date_time = formatTime("yyyy-MM-dd",long1);
//                                Evaluate evaluate1 = new Evaluate(user_name,theme,date_time,evaluate_state,Ratingbar,content);
//                                mEvaluates.add(evaluate1);
//                            }
//                            //同步刷新
//                            Message message = new Message();
//                            message.what = 1;
//                            handler.sendMessage(message);
//                        }
//                    }
//                });
//            }
//        });
//    }

//    private void get_receive_evaluate(final String user_id, String order_type, String evaluate_state){
//        OkHttpClient client = new OkHttpClient();
//        Log.d("token","token:"+token);
//        //发起get请求
//        Request request = new Request.Builder()
//                .addHeader("Authorization",token)
//                .url("http://www.braisedweever.top/klb/User/ID/ReLeaveMessage?userID="+user_id) //携带参数
//                .build();
//        System.out.println("运行到请求已发送");
//        okhttp3.Call call = client.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                //当get失败的时候
//                e.printStackTrace();
//                System.out.println("Error in Get ReleaseTask info");
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                final String res = response.body().string();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        System.out.println("运行到回调成功");
//                        Log.d("res", "run: "+res);
//                        String user_name="",theme="",content="",ratingbar="",date="",evaluate_state="";
//                        if(!res.equals("[]")){
//                            JSONArray array = JSON.parseArray(res);
//                            for(int i=0;i<array.size();i++){
//                                JSONObject json = (JSONObject) array.get(i);
//                                user_name = "小李";
//                                theme = "任务主题";
//                                content = json.get("LeaveContent").toString();
//                                ratingbar = json.get("UserScore").toString();
//                                date = json.get("LeaveTime").toString();
//                                evaluate_state = "收到评价";
//                                float Ratingbar = Float.parseFloat(ratingbar);  //将String类型转为float
//                                Long long1 = Long.valueOf(date);    //long类型时间
//                                String date_time = formatTime("yyyy-MM-dd",long1);
//                                Evaluate evaluate2 = new Evaluate(user_name,theme,date_time,evaluate_state,Ratingbar,content);
//                                mEvaluates.add(evaluate2);
//                            }
//                            //同步刷新
//                            Message message = new Message();
//                            message.what = 1;
//                            handler.sendMessage(message);
//                        }
//                    }
//                });
//            }
//        });
//    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1){
                adapter.notifyDataSetChanged();
            }else if(msg.what == 2) {
                if (flag == 0) {
                    flag = 1;
                } else {
                    mEvaluatesAll.addAll(mEvaluatesReceive);
                    mEvaluatesAll.addAll(mEvaluatesSend);
                    Log.d("mEvaluatesAll_error", "a: "+mEvaluatesReceive.size()+"b: "+mEvaluatesSend.size());

                    Collections.sort(mEvaluatesAll, new Comparator<Evaluate>() {
                        @Override
                        public int compare(Evaluate o1, Evaluate o2) {
//                            long a = Long.valueOf(o1.getTime()).longValue();
//                            long b = Long.valueOf(o2.getTime()).longValue();
                            String a = o1.getTime();
                            String b = o2.getTime();
                            if (a.compareTo(b) == 1) {    //大于
                                return 1;
                            }
                            if (a.compareTo(b) == -1) {      //小于
                                return -1;
                            }
                            return 0;
                        }
                    });
                    mEvaluates.clear();
                    mEvaluates.addAll(mEvaluatesAll);
                    adapter.notifyDataSetChanged();
                    flag = 0;
                }
            }
        }
    };


    public static String formatTime(String format, long time){
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date(time));
    }


    private void changeSelect(int resId){
        type1.setSelected(false);
        type1.setTextSize(18);
        type2.setSelected(false);
        type2.setTextSize(18);
        type3.setSelected(false);
        type3.setTextSize(18);
        switch (resId) {
            case R.id.all_evaluate:
                type1.setSelected(true);
                type1.setTextSize(20);
                //调用发送请求函数
                mEvaluates.clear();
                mEvaluates.addAll(mEvaluatesAll);
                adapter.notifyDataSetChanged();
//                String user_id1 = User_id, order_type1 = "0" ,evaluate_state1 = "2";
//                get_all_evaluate(user_id1,order_type1,evaluate_state1);
                break;
            case R.id.release_evaluate:
                type2.setSelected(true);
                type2.setTextSize(20);
                //调用发送请求函数
                Log.d("release_evaluate","发布评价列表中数据为："+mEvaluatesSend.size());
                mEvaluates.clear();
                mEvaluates.addAll(mEvaluatesSend);
                adapter.notifyDataSetChanged();
                break;
            case R.id.receive_evaluate:
                type3.setSelected(true);
                type3.setTextSize(20);
                //调用发送请求函数
                Log.d("receive_evaluate","收到评价列表中数据为："+mEvaluatesReceive.size());
                mEvaluates.clear();
                mEvaluates.addAll(mEvaluatesReceive);
                adapter.notifyDataSetChanged();
                break;
        }
    }
}
