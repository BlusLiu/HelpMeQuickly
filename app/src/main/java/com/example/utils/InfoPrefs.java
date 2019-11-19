package com.example.utils;

import android.content.SharedPreferences;


public class InfoPrefs {

    private static SPUtil sp;

    public static void init(String configName){
        if(sp == null||!sp.getConfigName().equals(configName)){
            sp = new SPUtil(configName);
        }
    }

    public static void setData(String key,String value){
        sp.setString(key,value);
    }
    public static void setIntData(String key,int value) {sp.setInt(key,value);}
    public static String getData(String key){
        return sp.getString(key,"");
    }
    public static int getIntData(String key){ return sp.getInt(key,0); }
}
