package com.example.helpmequickly_my;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.example.sqlite.User_Database;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WriteEvaluateActivity extends AppCompatActivity {

    public User_Database user;
    public SQLiteDatabase sqL_read;
    static String User_id;
    static String token;
    private TextView task_theme;
    private TextView evaluate_content;
    private TextView evaluate_commit;
    private ImageView comeback;
    private RatingBar evaluate_rb;
    static String task_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_evaluate);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  // 竖屏
        ActionBar actionbar = getSupportActionBar();        //隐藏标题栏
        if (actionbar !=null){
            actionbar.hide();
        }
        //打开一个数据库
        user = new User_Database(WriteEvaluateActivity.this);
        sqL_read = user.getReadableDatabase();
        init();
        evaluate_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = "" ;
                int star = 5;
                content = evaluate_content.getText().toString();
                star =  (int) evaluate_rb.getRating();
                String Star;
                Star = String.valueOf(star);
                sent_post(content ,Star);
            }
        });
        comeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init(){
        //获取传递过来的数据
        Intent intent = getIntent();
        String task_title = intent.getStringExtra("input_title");
        task_id = intent.getStringExtra("task_id");
        //任务主题赋值
        task_theme = (TextView)findViewById(R.id.task_theme);
        task_theme.setText(task_title);
        //数据绑定
        evaluate_content = (TextView)findViewById(R.id.evaluate_content);
        evaluate_commit = (TextView)findViewById(R.id.evaluate_commit);
        comeback = (ImageView)findViewById(R.id.write_evaluate_back);
        evaluate_rb = (RatingBar)findViewById(R.id.evaluate_rb);
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
                        //选择获取哪个任务列表
                        //select_task_info(User_id);
                    }
                });
            }
        });
    }

    private void sent_post(String content , String star){
        Log.d("sent_data","task_id:"+task_id+"User_id:"+User_id+"content:"+content+"star:"+star);
        OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("TaskID",task_id)
                .add("UserID",User_id)
                .add("LeaveContent",content)
                .add("UserScore",star)
                .build();
        final Request request = new Request.Builder()
                .addHeader("Authorization",token)
                .url("http://www.braisedweever.top/klb/task/ID/score")
                .post(formBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //当post失败的时候
                e.printStackTrace();
                System.out.println("Error in Login Request!");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //获取返回的数据
                        Log.d("返回的数据","返回的数据："+res);
                        JSONObject json = JSONObject.parseObject(res);
                        //String LeaveMessageID = json.get("LeaveMessageID").toString();
                        //Log.d("LeaveMessageID","LeaveMessageID为："+LeaveMessageID);
                        //弹出AlerDialog跳转到登录界面
                        AlertDialog.Builder dialog = new AlertDialog.Builder(WriteEvaluateActivity.this);
                        dialog.setTitle("提交成功");
                        dialog.setMessage("点击确认返回任务界面");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                String select = "release_task";
                                Intent intent = new Intent(WriteEvaluateActivity.this, MyTaskActivity.class);
                                intent.setAction("action");
                                intent.putExtra("select_item",select);
                                startActivity(intent);
                                WriteEvaluateActivity.this.finish();
                            }
                        });
                        dialog.show();
                    }
                });
            }
        });

    }


}
