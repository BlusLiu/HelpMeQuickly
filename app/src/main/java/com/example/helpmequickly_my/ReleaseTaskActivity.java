package com.example.helpmequickly_my;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.sqlite.User_Database;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.qqtheme.framework.picker.DateTimePicker;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ReleaseTaskActivity extends AppCompatActivity {
    String startTime, endTime, title, money, content, type;

// TODO token 存到了表里
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.release_task);

        View time_select = findViewById(R.id.time_select);
        final TextView time_show = findViewById(R.id.time_show);
        final TextView task_title = findViewById(R.id.task_title);
        final TextView task_content = findViewById(R.id.task_content);
        RadioButton urgent_task = findViewById(R.id.urgent_task);
        final TextView task_money = findViewById(R.id.task_money);
        final Spinner spinner = findViewById(R.id.task_spinner);
        Button submit_task = findViewById(R.id.submit_task);

        ActionBar actionbar = getSupportActionBar();        //隐藏标题栏
        if (actionbar !=null){
            actionbar.hide();
        }

        String[] ctype = new String[]{"全部", "线上任务", "打印任务", "跑腿任务", "代拿快递","超市代购"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ctype);  //创建一个数组适配器
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     //设置下拉列表框的下拉选项样式

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DATE);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        //int second = c.get(Calendar.SECOND);

        final DateTimePicker picker = new DateTimePicker(this, DateTimePicker.HOUR_24);//24小时值
        picker.setDateRangeStart(year, month, date);//日期起点
        picker.setDateRangeEnd(2020, 1,31);//日期终点
        picker.setTimeRangeStart(hour, minute);//时间范围起点
        picker.setTimeRangeEnd(23, 59);//时间范围终点
        picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
            @Override
            public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
                //year:年，month:月，day:日，hour:时，minute:分
                endTime = year + "-" + month + "-" + day + " " + hour + ":" + minute;
                Toast.makeText(getApplicationContext(), endTime, Toast.LENGTH_LONG).show();
                time_show.setText(endTime);

            }
        });

        time_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker.show();

            }
        });

        submit_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
                money = task_money.getText().toString();
                title = task_title.getText().toString();
                content = task_content.getText().toString();

                submit(startTime, endTime, title, money, content, type);
                // 成功之后
            }
        });

    }

    private void submit(String startTime, String endTime, String title, String money, String content, String type) {
        OkHttpClient client = new OkHttpClient();

        //发起get请求
        String taskstarttime;
        FormBody formBody = new FormBody.Builder()
                .add("taskstarttime", startTime)
                .add("taskendtime", endTime)
                .add("tasktitle", title)
                .add("taskcontent", content)
                .add("typeid", type)
                .add("taskmoney", money)
                .add("tasknumberre","1")

                .build();
        Request request_one = new Request.Builder()
                .addHeader("Authorization",getToken())
                .url("http://www.braisedweever.top/klb/task") //携带参数
                .post(formBody)
                .build();
        System.out.println("运行到请求已发送");
        okhttp3.Call call_one = client.newCall(request_one);
        call_one.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

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
                        String taskid = json.get("taskid").toString();
                        Log.d("taskid","taskid："+taskid);
                        //弹出AlerDialog跳转到登录界面
                        AlertDialog.Builder dialog = new AlertDialog.Builder(ReleaseTaskActivity.this);
                        dialog.setTitle("提交成功");
                        dialog.setMessage("点击确认返回任务界面");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                String select = "release_task";
                                Intent intent = new Intent(ReleaseTaskActivity.this, MainActivity.class);
                                intent.setAction("action");
                                intent.putExtra("select_item",select);
                                startActivity(intent);
                                ReleaseTaskActivity.this.finish();
                            }
                        });
                        dialog.show();
                    }
                });
            }
        });
    }

    private String getToken() {
        String token = null;
        User_Database user = new User_Database(this);
        SQLiteDatabase sqL_read = user.getReadableDatabase();
        Cursor cursor = sqL_read.query("user",null,null,null,null,null,"1");
        if(cursor.moveToFirst()){
            do{
                token = cursor.getString(cursor.getColumnIndex("token"));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return token;

    }


}

