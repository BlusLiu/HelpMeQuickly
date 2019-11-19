package com.example.helpmequickly_my;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SchoolAuthenticationActivity extends Activity implements View.OnClickListener {
    Button school_authen_support;
    EditText school_paswd,school_account;
    Spinner school_choose;
    String school_str = "请选择学校";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.klb_school_authen);

        Init();
    }

    public void Init(){
        school_authen_support  = findViewById(R.id.school_authen_support);
        school_paswd           = findViewById(R.id.school_paswd);
        school_account         = findViewById(R.id.school_account);
        school_choose          = findViewById(R.id.school_choose);

        final String[] school = new String[]{"杭州电子科技大学","杭州电子柯基大学"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,school);
        school_choose.setAdapter(adapter);

        //城市下拉列表添加监听事件
        school_choose.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            /*有东西被选中*/
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){
                school_str = school[i];
            }
            /*没有东西被选中的时候*/
            @Override
            public void onNothingSelected(AdapterView<?> adapterView){}
        });
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.school_authen_support:

                break;
        }
    }
}
