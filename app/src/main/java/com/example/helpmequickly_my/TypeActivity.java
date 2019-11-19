package com.example.helpmequickly_my;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class TypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        setStatusBarFullTransparent();
        getWindow().getDecorView().findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up1, R.anim.slide_dw1);
            }
        });
        getWindow().getDecorView().findViewById(R.id.type1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(TypeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.type1).setOnClickListener(onClickListener);
        findViewById(R.id.type2).setOnClickListener(onClickListener);
        findViewById(R.id.type3).setOnClickListener(onClickListener);
        findViewById(R.id.type4).setOnClickListener(onClickListener);
        findViewById(R.id.type5).setOnClickListener(onClickListener);


    }

    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.type1:
                    Intent intent = new Intent();
                    intent.setClass(TypeActivity.this, ReleaseTaskActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_dw, R.anim.slide_up);
                    break;
                    default:
                        Intent intent1 = new Intent();
                        intent1.setClass(TypeActivity.this, ReleaseTaskActivity.class);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.slide_dw, R.anim.slide_up);
            }
        }
    };

    protected void setStatusBarFullTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }
    }
}
