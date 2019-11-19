package com.example.message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helpmequickly_my.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 14:25 2019/11/19
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Message> mMessages;

    public MessageAdapter(List<Message> mMessages) {
        this.mMessages = mMessages;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == R.layout.list_message_r){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_message_r , parent, false);
        }else view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_message_l , parent, false);

        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Message message = mMessages.get(position);
        holder.content.setText(message.getContent());
        // holder.portrait.setImageURI();
    }


    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mMessages.get(position).getRight() ? R.layout.list_message_r : R.layout.list_message_l;


    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        CircleImageView portrait;

        public ViewHolder(View view) {
            super(view);
            content = view.findViewById(R.id.txt_content);
            portrait = view.findViewById(R.id.portrait);


        }
    }
}