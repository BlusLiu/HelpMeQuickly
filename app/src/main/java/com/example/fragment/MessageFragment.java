package com.example.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.helpmequickly_my.ChatActivity;
import com.example.helpmequickly_my.MainActivity;
import com.example.helpmequickly_my.R;
import com.example.helpmequickly_my.TypeActivity;
import com.example.message.Contact;
import com.example.message.ContactAdapter;
import com.example.message.MessageAdapter;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MessageFragment extends Fragment {

    private List<Contact> contacts ;
    ContactAdapter adapter;
    View rootView;
    RecyclerView recyclerView;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
                // CircleImageView portrait = rootView.findViewById((int)(adapter.getItemId(msg.what)));

            CircleImageView portrait = recyclerView.getChildAt(msg.what).findViewById(R.id.im_portrait);
//            RequestOptions requestOptions = new RequestOptions();
//            requestOptions.error(R.mipmap.no_avatar)
//                    .circleCrop()
//                    .skipMemoryCache(true)
//                    .diskCacheStrategy( DiskCacheStrategy.NONE );
            Integer y = 40 + msg.what;
            String x = y.toString();
            Glide.with(getContext()).
                    load("https://source.unsplash.com/"+x+"x"+x+"/").
                    skipMemoryCache(true).
//                    diskCacheStrategy(DiskCacheStrategy.NONE).这个应该是直接清除了
                    placeholder(R.mipmap.user_image).
                    dontAnimate().
                    into(portrait);
            new Thread(new Runnable() {
                @Override
                public void run() {
//                    Glide.get(getContext()).clearDiskCache();
//                    Glide.get(getContext()).clearMemory();
                }
            });
            onStart();


        }
    };

    @Override
    public void onStart() {
        super.onStart();
        Log.d("messageFragment", "onStart: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_message, null);

        contacts = new ArrayList<>();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.contact_recycler);
        initTask();//初始化数据
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ContactAdapter(contacts, handler);
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

