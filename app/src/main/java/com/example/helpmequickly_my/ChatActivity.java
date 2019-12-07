package com.example.helpmequickly_my;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.message.Message;
import com.example.message.MessageAdapter;
import com.example.message.TCPClient;
import com.example.message.bean.ServerInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends Activity {
    private static final int SEND = 10;
    private static final int WRITE = 20;
    private List<Message> messages;
    MessageAdapter adapter;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull android.os.Message msg) {
            if (msg.what == SEND){

                messages.add(new Message(false, msg.obj.toString()));
                adapter.notifyDataSetChanged();
            }else if (msg.what == WRITE){

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final TextView edit_content = findViewById(R.id.edit_content);
        View btn_submit = findViewById(R.id.btn_submit);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        initTask();//初始化数据
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MessageAdapter(messages);
        recyclerView.setAdapter(adapter);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_content != null)
                {
                    send(edit_content.getText().toString());
                }
            }
        });


        final ServerInfo info = new ServerInfo();
        if (info != null) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            TCPClient.linkWith(info,handler);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

        }
    }

    private void send(String content) {
        TCPClient.send(content);
        messages.add(new Message(true, content));
        adapter.notifyDataSetChanged();
    }

    private void initTask() {
        messages = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            Boolean isRight = (int)(Math.random()*10)%2 == 0;
            Message message= new Message(isRight, "?");
            messages.add(message);
        }

    }

}
