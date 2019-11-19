package com.example.helpmequickly_my;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.message.Message;
import com.example.message.MessageAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends Activity {
    private List<Message> messages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        initTask();//初始化数据
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        MessageAdapter adapter = new MessageAdapter(messages);
        recyclerView.setAdapter(adapter);
    }

    private void initTask() {
        messages = new ArrayList<>();
        for(int i = 0; i < 7; i++) {
            Boolean isRight = (int)(Math.random()*10)%2 == 0;
            Message message= new Message(isRight, "?");
            messages.add(message);
        }

    }

}
