package com.example.tool;

import java.util.Map;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 封装OKhttp
 * Created by PC on 2017/8/8.
 */
public class MyHttpClient {

    public static void sendAnysGet(OkHttpClient client,String url,Callback callback){
        Request request = new Request.Builder().url(url).get().build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendAnysPost(OkHttpClient client,String url,Callback callback,Map<String,String> map){
        FormBody.Builder builder = new  FormBody.Builder();
        if (map!=null)
        {
            for (Map.Entry<String,String> entry:map.entrySet())
            {
                builder.add(entry.getKey(),entry.getValue());
            }
        }
        FormBody body = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
