package com.example.helpmequickly_my;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fragment.DynamicFragment;
import com.example.fragment.HomeFragment;
import com.example.fragment.MessageFragment;
import com.example.fragment.PersonFragment;

public class MainActivity extends AppCompatActivity {
    //要切换显示的四个Fragment
    private HomeFragment homeFragment;
    private DynamicFragment dynamicFragment;
    private MessageFragment messageFragment;
    private PersonFragment personFragment;
    private int currentId = R.id.tv_main;// 当前选中id,默认是主页

    private TextView tvMain, tvDynamic, tvMessage, tvPerson;//底部四个TextView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Glide.get(getBaseContext()).clearDiskCache();
//                Glide.get(getBaseContext()).clearMemory();
//            }
//        });
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);     //引入布局

        ActionBar actionbar = getSupportActionBar();        //隐藏标题栏
        if (actionbar !=null){
            actionbar.hide();
        }

        tvMain = (TextView) findViewById(R.id.tv_main);
        tvMain.setSelected(true);//首页默认选中
        tvDynamic = (TextView) findViewById(R.id.tv_dynamic);
        tvMessage = (TextView) findViewById(R.id.tv_message);
        tvPerson = (TextView) findViewById(R.id.tv_person);

        //默认加载首页
        homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.main_container,homeFragment).commit();

        tvMain.setOnClickListener(tabClickListener);
        tvDynamic.setOnClickListener(tabClickListener);
        tvMessage.setOnClickListener(tabClickListener);
        tvPerson.setOnClickListener(tabClickListener);
        findViewById(R.id.iv_make).setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_make:
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, TypeActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_dw, R.anim.slide_up);
                    Toast.makeText(MainActivity.this,"点击了发布按钮",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private View.OnClickListener tabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() != currentId) {//如果当前选中跟上次选中的一样,不需要处理
                changeSelect(v.getId());//改变图标跟文字颜色的选中
                changeFragment(v.getId());//fragment的切换
                currentId = v.getId();//设置选中id
            }
        }
    };

    private void changeFragment(int resId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();//开启一个Fragment事务

        hideFragments(transaction);//隐藏所有fragment
        if(resId==R.id.tv_main){//主页
            if(homeFragment==null){//如果为空先添加进来.不为空直接显示
                homeFragment = new HomeFragment();
                transaction.add(R.id.main_container,homeFragment);
            }else {
                transaction.show(homeFragment);
            }
        }else if(resId==R.id.tv_dynamic){//任务
            if(dynamicFragment==null){
                dynamicFragment = new DynamicFragment();
                transaction.add(R.id.main_container,dynamicFragment);
            }else {
                transaction.show(dynamicFragment);
            }
        }else if(resId==R.id.tv_message){//消息中心
            if(messageFragment==null){
                messageFragment = new MessageFragment();
                transaction.add(R.id.main_container,messageFragment);
            }else {
                transaction.show(messageFragment);
            }
        }else if(resId==R.id.tv_person){//我
            if(personFragment==null){
                personFragment = new PersonFragment();
                transaction.add(R.id.main_container,personFragment);
            }else {
                transaction.show(personFragment);
            }
        }
        transaction.commit();//一定要记得提交事务
    }

//    隐藏之前所有的Fragment
    private void hideFragments(FragmentTransaction transaction){
        if (homeFragment != null)//不为空才隐藏,如果不判断第一次会有空指针异常
            transaction.hide(homeFragment);
        if (dynamicFragment != null)
            transaction.hide(dynamicFragment);
        if (messageFragment != null)
            transaction.hide(messageFragment);
        if (personFragment != null)
            transaction.hide(personFragment);
    }

    private void changeSelect(int resId){
        tvMain.setSelected(false);
        tvDynamic.setSelected(false);
        tvMessage.setSelected(false);
        tvPerson.setSelected(false);

        switch (resId) {
            case R.id.tv_main:
                tvMain.setSelected(true);
                break;
            case R.id.tv_dynamic:
                tvDynamic.setSelected(true);
                break;
            case R.id.tv_message:
                tvMessage.setSelected(true);
                break;
            case R.id.tv_person:
                tvPerson.setSelected(true);
                break;
        }
    }
}


