package com.example.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.entity.UserInfo;
import com.example.helpmequickly_my.MainActivity;
import com.example.helpmequickly_my.MyEvaluateActivity;
import com.example.helpmequickly_my.MyTaskActivity;
import com.example.helpmequickly_my.R;
import com.example.helpmequickly_my.UserInfoActivity;
import com.example.utils.InfoPrefs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import de.hdodenhof.circleimageview.CircleImageView;
//import com.example.helpmequickly_my.UserInfoActivity;


public class PersonFragment extends Fragment {
    private LinearLayout type1;
    private LinearLayout type2;
    private LinearLayout type3;
    private CircleImageView userImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_person, null);

        ImageView imageView_user = (ImageView) rootView.findViewById(R.id.user_image);      //点击跳转到个人信息页
        userImage = (CircleImageView) imageView_user;
        imageView_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UserInfoActivity.class);
                startActivity(intent);
            }
        });
        TextView textView = (TextView) rootView.findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UserInfoActivity.class);
                startActivity(intent);
            }
        });

        init(rootView);
        showHeadImage();
        type1.setOnClickListener(new View.OnClickListener() {   //跳转到已发布
            @Override
            public void onClick(View v) {
                String select = "release_task";
                Intent intent = new Intent(getActivity(),MyTaskActivity.class);
                intent.setAction("action");
                intent.putExtra("select_item",select);
                getActivity().startActivity(intent);
                //startActivity(new Intent(getActivity(),MyTaskActivity.class));
            }
        });
        type2.setOnClickListener(new View.OnClickListener() {   //跳转到已完成
            @Override
            public void onClick(View v) {
                String select = "complete_task";
                Intent intent = new Intent(getActivity(),MyTaskActivity.class);
                intent.setAction("action");
                intent.putExtra("select_item",select);
                getActivity().startActivity(intent);
                //startActivity(new Intent(getActivity(),MyTaskActivity.class));
            }
        });
        type3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyEvaluateActivity.class));
            }
        });
        ImageView imageView_setting = (ImageView) rootView.findViewById(R.id.setting_img);       //为设置按钮添加点击事件
        imageView_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"This is button of setting",Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        showHeadImage();
    }

    private void init(View view){
        type1 = (LinearLayout) view.findViewById(R.id.release_task);
        type2 = (LinearLayout) view.findViewById(R.id.complete_task);
        type3 = (LinearLayout) view.findViewById(R.id.my_evaluate);
    }

    private void showHeadImage() {
        boolean isSdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);// 判断sdcard是否存在
        if (isSdCardExist) {
            // 忘记实例化了
            String localIconNormal = "user_image.png";
            FileInputStream localStream = null;
            try {
                localStream = getActivity().openFileInput(localIconNormal);
                Bitmap bitmap = BitmapFactory.decodeStream(localStream);
                userImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                userImage.setImageResource(R.mipmap.user_image);
                //Log.e(TAG, "no file");
            }
        } else {
            Log.e("person","no SD card");
            userImage.setImageResource(R.mipmap.user_image);
        }
    }
}
