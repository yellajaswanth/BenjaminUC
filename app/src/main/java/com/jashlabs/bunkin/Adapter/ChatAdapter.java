package com.jashlabs.bunkin.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.jashlabs.bunkin.Models.Message;
import com.jashlabs.bunkin.R;
import com.jashlabs.bunkin.ViewHolder.ChatViewItemHolder;

import java.util.ArrayList;

/**
 * Created by Jaswanth on 26-02-2016.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatViewItemHolder> {

    ArrayList<String> chatMessageList;
    Gson gson = new Gson();
    String message;
    String username;
    String myUsername;
    Context context;

    public ChatAdapter(ArrayList<String> chatMessageList, String myUsername) {
        this.chatMessageList = chatMessageList;
        this.myUsername = myUsername;
    }

    @Override
    public ChatViewItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ChatViewItemHolder(LayoutInflater.from(context).inflate
                (R.layout.chat_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final ChatViewItemHolder holder, int position) {

        message = chatMessageList.get(position);
        final Message messageObject = gson.fromJson(message, Message.class);

        username = messageObject.getUsername();
        holder.username.setText(messageObject.getUsername());

        if ((username.equals(myUsername))) {
            holder.chatHolder.setBackgroundResource(R.drawable.me_chat_bubble);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
            holder.chatHolder.setLayoutParams(params);
            holder.message.setTextColor(Color.parseColor("#ffffff"));
            holder.username.setTextColor(Color.parseColor("#ffffff"));
        } else{
            holder.chatHolder.setBackgroundResource(R.drawable.left_chat_bubble);
            holder.message.setTextColor(Color.parseColor("#000000"));
            holder.username.setTextColor(Color.parseColor("#000000"));
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 1);
            holder.chatHolder.setLayoutParams(params);
        }
        holder.message.setText(messageObject.getMessage());


        if(!(messageObject.getExpiry().isEmpty() || messageObject.getExpiry().equals("null"))){
            holder.divider.setVisibility(View.VISIBLE);
            holder.expiry.setVisibility(View.VISIBLE);
            holder.expiry.setText("Expires in " + messageObject.getExpiry() + " seconds");
            new CountDownTimer(Integer.parseInt(messageObject.getExpiry()) * 1000, 1000){

                @Override
                public void onTick(long millisUntilFinished) {
                    holder.expiry.setText("Expires in " + millisUntilFinished/1000 + " seconds");
                }

                @Override
                public void onFinish() {
                    holder.chatHolder.setVisibility(View.GONE);
                }
            }.start();
        }
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }
}
