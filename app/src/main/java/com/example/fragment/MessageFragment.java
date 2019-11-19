package com.example.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helpmequickly_my.ChatActivity;
import com.example.helpmequickly_my.MainActivity;
import com.example.helpmequickly_my.R;
import com.example.helpmequickly_my.TypeActivity;
import com.example.message.Contact;
import com.example.message.ContactAdapter;
import com.example.message.MessageAdapter;

import java.util.ArrayList;
import java.util.List;


public class MessageFragment extends Fragment {

    private List<Contact> contacts ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_message, null);

        contacts = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.contact_recycler);
        initTask();//初始化数据
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        ContactAdapter adapter = new ContactAdapter(contacts);
        recyclerView.setAdapter(adapter);

        rootView.findViewById(R.id.contact_recycler).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), ChatActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }




    private void initTask() {
        contacts = new ArrayList<>();

        Contact contact= new Contact("", "Liu", "[开心]");
        contacts.add(contact);
        contact= new Contact("", "Kitty", "快一点ok？");
        contacts.add(contact);
        contact= new Contact("", "孙笑川", "？？？？？[佛了]");
        contacts.add(contact);

    }
}

