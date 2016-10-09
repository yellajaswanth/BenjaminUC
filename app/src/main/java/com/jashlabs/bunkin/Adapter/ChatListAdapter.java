package com.jashlabs.bunkin.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jashlabs.bunkin.Models.ChatList;
import com.jashlabs.bunkin.R;

import java.util.List;

/**
 * Created by Jaswanth on 24-02-2016.
 */
public class ChatListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<ChatList> chatItems;

    public ChatListAdapter(Activity activity, List<ChatList> chatItems) {
        this.activity = activity;
        this.chatItems = chatItems;
    }

    @Override
    public int getCount() {
        return chatItems.size();
    }

    @Override
    public Object getItem(int position) {
        return chatItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.item_chat, null);

        ImageView userImg = (ImageView) convertView.findViewById(R.id.userImg);
        TextView userName = (TextView) convertView.findViewById(R.id.userName);
        TextView userMsg  = (TextView) convertView.findViewById(R.id.userMsg);
        TextView userLastMsgTime = (TextView) convertView.findViewById(R.id.userLastMsgTime);

        ChatList c = chatItems.get(position);

        userName.setText(c.getUserName());
        userMsg.setText(c.getUserChatLastMsg());
        userLastMsgTime.setText(c.getUserLastMsgDateTime());

        return null;
    }
}
