package com.example.helpmequickly_my;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.utils.MyApplication.getContext;

public class RegisterActivity extends Activity implements View.OnClickListener , View.OnFocusChangeListener {
    public boolean check_value;
    ImageView comeback;
    EditText edit_username,edit_useraccount,edit_paswd,edit_paswd_again;
    Button register;        //注册按钮
    @Override
    protected void onCreate(Bundle savedInstanceState){
        //注册界面
        super.onCreate(savedInstanceState);
        setContentView(R.layout.klb_register);
        //初始化
        init_register();

//        edit_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus){
//                    Toast.makeText(RegisterActivity.this,"获取焦点",Toast.LENGTH_SHORT).show();
//                    //获取焦点
//                }else{
//                    String username = edit_username.getText().toString();
//                    if(username.equals("")){
//                        Toast.makeText(RegisterActivity.this,"用户名不可为空",Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });
    }

    public void init_register(){
        //绑定按钮
        comeback    = findViewById(R.id.register_back);
        edit_username    = findViewById(R.id.register_username);
        edit_useraccount = findViewById(R.id.register_useraccount);
        edit_paswd       = findViewById(R.id.register_paswd);
        edit_paswd_again = findViewById(R.id.register_paswd_again);
        register    = findViewById(R.id.register);
        //注册点击事件
        register.setOnClickListener(this);
        comeback.setOnClickListener(this);
        //绑定焦点事件
        edit_username.setOnFocusChangeListener(this);
        edit_useraccount.setOnFocusChangeListener(this);
        edit_paswd.setOnFocusChangeListener(this);
        edit_paswd_again.setOnFocusChangeListener(this);
        //数据初始化
        check_value = false;
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.register_back:
                this.finish();
                break;
            case R.id.register://用于写注册接口
                //判断两次输入的密码是否一致
                String username = edit_username.getText().toString();
                String useraccount = edit_useraccount.getText().toString();
                String userpaswd = edit_paswd.getText().toString();
                String userpaswd_agagin = edit_paswd_again.getText().toString();
                String userschool = "null";
                String userrealname = "null";
                //判断用户名和账号是否为空
                if(!username.equals("")&&!useraccount.equals("")){
                    check_value=true;
                }else{
                    Toast.makeText(RegisterActivity.this,"用户名或账号为空",Toast.LENGTH_SHORT).show();
                }
                if(check_value&&userpaswd.equals(userpaswd_agagin)&&(!userpaswd.equals(""))&&(!userpaswd_agagin.equals(""))){
                    //两次输入的密码如果一致并且都不为空
                    try{
                        Log.d("RegisterActivity","用户名:"+username);
                        //进行请求
                        Send_post(username,useraccount,userpaswd,userschool,userrealname);
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(RegisterActivity.this,"请求出错",Toast.LENGTH_SHORT).show();
                    }
                }
                else if(!userpaswd.equals(userpaswd_agagin)){
                    Log.d("RegisterActivity","注册:账号或密码错误");
                    Toast.makeText(RegisterActivity.this,"两次输入的密码不一致，请重新输入",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void Send_post(final String username, String useraccount, String userpaswd, String userschool, String userrealname){
        //使用okhttp进行post请求
        OkHttpClient client = new OkHttpClient();
        //设置post请求
        FormBody formBody = new FormBody.Builder()
                .add("username",username)
                .add("useraccount", useraccount)
                .add("userpaswd", userpaswd)
                .add("userschool",userschool)
                .add("userrealname",userrealname)
                .build();
        final Request request = new Request.Builder()
                //.url("http://10.0.2.2:8889/user/")
                .url("http://www.braisedweever.top/klb/user/register/")
                .post(formBody)
                .build();
        okhttp3.Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
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
                        //获取返回的数据
                        JSONObject json = JSONObject.parseObject(res);
                        Log.d("RegisterActivity","run:注册成功");
                        //弹出AlerDialog跳转到登录界面
                        AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                        dialog.setTitle("注册成功");
                        dialog.setMessage("点击确认返回登录界面");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                RegisterActivity.this.finish();
                            }
                        });
                        dialog.show();

                    }
                });
            }
        });
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            switch (v.getId()){
                case R.id.register_username:
                    break;
                case R.id.register_useraccount:
                    break;
                case R.id.register_paswd:
                    break;
                case R.id.register_paswd_again:
//                    String paswd = edit_paswd.getText().toString();
//                    String paswd_again = edit_paswd_again.getText().toString();
//                    if(paswd.equals(paswd_again)){
//                        Toast.makeText(RegisterActivity.this,"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
//                    }
                    break;
                default:break;
            }
        }else{
            switch (v.getId()){
                case R.id.register_username:
                    String username = edit_username.getText().toString();
                    if(username.equals("")){
                        Toast.makeText(RegisterActivity.this,"用户名不可为空",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.register_useraccount:
                    String useraccount = edit_useraccount.getText().toString();
                    if(useraccount.length()<"10000000000".length()|useraccount.length()>"99999999999".length()){
                        Toast.makeText(RegisterActivity.this,"请输入11位手机号",Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:break;
            }
        }
    }
}
