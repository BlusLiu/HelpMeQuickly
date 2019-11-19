package com.example.helpmequickly_my;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.task.MyTask;
import com.example.task.MyTaskAdapter;
import com.example.task.Task;

import java.util.ArrayList;
import java.util.List;

public class MyTaskActivity extends AppCompatActivity {

    private List<MyTask> myTaskList = new ArrayList<>();
    private List banners = new ArrayList();
    private TextView type1;
    private TextView type2;
    ImageView comeback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  // 竖屏
        setContentView(R.layout.my_task);
        ActionBar actionbar = getSupportActionBar();        //隐藏标题栏
        if (actionbar !=null){
            actionbar.hide();
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_list_task);
        initTask();//初始化数据
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        MyTaskAdapter adapter = new MyTaskAdapter(myTaskList);
        recyclerView.setAdapter(adapter);
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
        comeback  = (ImageView) findViewById(R.id.my_task_back);

        Intent intent = getIntent();
        if("action".equals(intent.getAction())){
            String select = intent.getStringExtra("select_item");
            if(select.equals("release_task")){
                type1.setSelected(true);
                type1.setTextSize(20);
            }else if(select.equals("complete_task")){
                type2.setSelected(true);
                type2.setTextSize(20);
            }
        }
        for(int i = 0; i < 2; i++) {
            MyTask task1= new MyTask("小李","已完成","五餐楼下需要一把雨伞","5");
            myTaskList .add(task1);
            MyTask task2= new MyTask("小张","已完成","阳光长跑需要一个陪跑","5");
            myTaskList .add(task2);
            MyTask task3= new MyTask("小陈","已发布","需要代取快递","5");
            myTaskList .add(task3);
        }
    }

    private void changeSelect(int resId){
        type1.setSelected(false);
        type1.setTextSize(18);
        type2.setSelected(false);
        type2.setTextSize(18);
        switch (resId) {
            case R.id.release_left:
                type1.setSelected(true);
                type1.setTextSize(20);
                break;
            case R.id.complete_right:
                type2.setSelected(true);
                type2.setTextSize(20);
                break;
        }
    }
}
