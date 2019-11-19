package com.example.entity;
import android.provider.SyncStateContract;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

public class UserInfo extends SyncStateContract.Constants {
    @JSONField(ordinal = 1)
    private int id;
    @JSONField(ordinal = 2)
    private String time;
    @JSONField(ordinal = 3)
    private String token;


    public UserInfo(int id, String time, String token){
        this.id    = id;
        this.time  = time;
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String toString(){
        return "{\"id\":"+id+",\"time\":"+time+",\"token\":"+token+"}";
    }

    public static String USER_NAME = "user_name";
    public static String USER_HEAD_IMAGE = "user_head_image";
}
