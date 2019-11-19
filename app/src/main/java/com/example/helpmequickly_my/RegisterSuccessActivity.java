package com.example.helpmequickly_my;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class RegisterSuccessActivity extends Activity implements View.OnClickListener{
    Button school_authentication,school_authentication_later;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.klb_register_successful);

        Init();
    }

    public void Init(){
        school_authentication = findViewById(R.id.school_authentication);
        school_authentication_later = findViewById(R.id.school_authentication_later);

        school_authentication.setOnClickListener(this);
        school_authentication_later.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.school_authentication:
                //跳转到校园认证页面
                Intent intent = new Intent(this,SchoolAuthenticationActivity.class);
                startActivity(intent);
                break;
            case R.id.school_authentication_later:
                //返回登录页面
                Toast.makeText(this,"移除注册",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}