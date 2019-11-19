package com.example.task;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 9:05 2019/10/23
 */
public class ImLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //Glide 加载图片简单用法
        Glide.with(context).load(path).centerCrop().into(imageView);
    }

}
