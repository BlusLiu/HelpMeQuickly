package com.example.message;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fragment.MessageFragment;
import com.example.helpmequickly_my.ChatActivity;
import com.example.helpmequickly_my.R;
import com.example.utils.MyApplication;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 14:25 2019/11/19
 */
public  class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> implements MyViewHolerClicks{
    private List<Contact> contacts;
    public MyViewHolerClicks myViewHolerClicks;
    private Context myContext;
    private static String TAG = "ContactAdapter";

    public void setMyViewHolerClicks(MyViewHolerClicks myViewHolerClicks) {
        this.myViewHolerClicks = myViewHolerClicks;
    }

    public ContactAdapter(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        myContext = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_contact, parent, false).getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        try {
            URL url = new URL("https://source.unsplash.com/40x40/");
            Log.d(TAG, "onBindViewHolder: "+url.getHost());
            myContext = MyApplication.getContext();
            Glide.with(myContext).load("https://source.unsplash.com/40x40/").placeholder(R.mipmap.user_image).into(holder.portrait);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "onBindViewHolder: test!!!");
        holder.desc.setText(contact.desc);
        holder.name.setText(contact.name);


    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    @Override
    public void onItemClick(int position) {

    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView desc;
        ImageView portrait;


        public ViewHolder(final View view) {
            super(view);
            portrait = view.findViewById(R.id.im_portrait);

            name = view.findViewById(R.id.txt_name);
            desc = view.findViewById(R.id.txt_desc);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(view.getContext(), ChatActivity.class);
                    view.getContext().startActivity(intent);
                }
            });


        }
    }
}
