package com.example.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alibaba.fastjson.JSONObject;
import com.example.entity.UserInfo;

public class User_Database extends SQLiteOpenHelper {

    public User_Database(Context context){
        //创建数据库
        super(context,"user_db",null,1);
    }
    //数据库第一次创建时调用该方法
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        //数据库语句,创建token表,存储token
        String sql = "create table user( id integer primary key autoincrement,time varchar(256),token varchar(256))";
        sqLiteDatabase.execSQL(sql);
    }
    //数据库版本更新的时候调用
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase,int i,int i1){
    }

    //添加数据
    public void adddata(SQLiteDatabase sqLiteDatabase,String Time,String Token){
        ContentValues values = new ContentValues();
        values.put("time",Time);
        values.put("token",Token);
        sqLiteDatabase.insert("user",null,values);
        sqLiteDatabase.close();
    }
    //删除数据
    public void delete(SQLiteDatabase sqLiteDatabase,int id){
        sqLiteDatabase.delete("user","id=?",new String[]{id+""});
        sqLiteDatabase.close();
    }
    //查询数据,查找最后一个数据
    public JSONObject querydata(SQLiteDatabase sqLiteDatabase){
        Cursor cursor = sqLiteDatabase.query("user",null,null,null,null,null,"id ASC");
        cursor.moveToLast();
        int id = cursor.getInt(cursor.getColumnIndex("id"));
        String time = cursor.getString(1);
        String token = cursor.getString(2);
        UserInfo userinfo = new UserInfo(id,time,token);
        //返回json
        JSONObject json = JSONObject.parseObject(userinfo.toString());
        cursor.close();
        sqLiteDatabase.close();
        return json;
    }
}
