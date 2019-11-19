package com.example.helpmequickly_my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.sqlite.User_Database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.utils.MyApplication.getContext;

public class LoginActivity extends Activity implements View.OnClickListener {

    public User_Database user;
    public SQLiteDatabase sqL_read;
    //绑定控件
    EditText account, password;
    TextView forgetPassword;
    Button login, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.klb_login);

        user = new User_Database(LoginActivity.this);
        //获取一个可读数据库
        sqL_read = user.getReadableDatabase();

        init();
    }

    //做组件初始化的工作
    public void init(){
        //绑定
        forgetPassword = (TextView) findViewById(R.id.login_forget_password);
        account = (EditText) findViewById(R.id.login_useraccount);
        password = (EditText) findViewById(R.id.login_userpaswd);
        login = (Button) findViewById(R.id.login_button_login);
        register = (Button) findViewById(R.id.login_button_register);

        //设置点击事件绑定
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);

    }

    //点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button_login:
                //登录按钮
                String useraccount  = account.getText().toString();
                String userpaswd    = password.getText().toString();
                try{
                    //进行请求
                    Login(useraccount,userpaswd);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(this,"请求出错",Toast.LENGTH_SHORT);
                }

                break;
            case R.id.login_button_register:
                //注册按钮,跳转页面
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.login_forget_password:
                Toast.makeText(this, "监修中。。。", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //-----------================================-------------------------
    //登录页面使用的函数
    private void Login(final String useraccount, String userpaswd) {
        //使用okhttp进行post请求
        OkHttpClient client = new OkHttpClient();
        //设置post请求
        FormBody formBody = new FormBody.Builder()
                .add("useraccount", useraccount)
                .add("userpaswd", userpaswd)
                .build();
        final Request request = new Request.Builder()
                //.url("http://10.0.2.2:8889/user/")
                .url("http://www.braisedweever.top/klb/user/")
                .post(formBody)
                .build();
        okhttp3.Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                //当post失败的时候
                e.printStackTrace();
                System.out.println("Error in Login Request!");
            }

            //当传输成功的时候
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //获取返回数据
                        JSONObject json = JSONObject.parseObject(res);
                        JSONObject json_check = JSONObject.parseObject(res);
                        json = JSONObject.parseObject(json.get("obj").toString());
                        if(json_check.get("msg").toString().equals("失败")){
                         //   Toast.makeText(getContext(),"账号或密码错误",Toast.LENGTH_SHORT).show();
                            Log.d("LoginActivity","run:账号或密码错误");
                        }else {
                            String time = json.get("expire-time").toString();
                            String token = json.get("token").toString();
                            //设置SQLite保存token
                            user.adddata(sqL_read, time, token);
                            //保存成功
                            System.out.println("保存成功!");
                            Log.i("#BraisedWeever", "保存成功！");
                            System.out.println(JSONObject.toJSONString(json));

                            //使用Intent穿梭
                            Log.d("LoginActivity","run:账号密码正确");
                            Intent intent2 = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent2);
                            LoginActivity.this.finish();
                        }
                    }
                });
            }
        });
    }
}