package com.jashlabs.bunkin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.jashlabs.bunkin.Activity.ChatActivity;
import com.jashlabs.bunkin.Models.Buddy;
import com.jashlabs.bunkin.R;
import com.jashlabs.bunkin.Utils.ChatApplication;
import com.jashlabs.bunkin.Utils.Constants;
import com.jashlabs.bunkin.ViewHolder.BuddyViewHolder;

import java.util.ArrayList;

import static com.jashlabs.bunkin.ViewHolder.BuddyViewHolder.*;

/**
 * Created by Jaswanth on 26-02-2016.
 */
public class BuddyListAdapter extends RecyclerView.Adapter<BuddyViewHolder> {

    ArrayList<String> buddyList;
    Gson gson = new Gson();
    String buddy;
    String username;
    Context context;

    public BuddyListAdapter(ArrayList<String> buddyList) {
        this.buddyList = buddyList;
    }

    @Override
    public BuddyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new BuddyViewHolder(LayoutInflater.from(context).inflate
                (R.layout.item_buddy, parent, false));
    }

    @Override
    public void onBindViewHolder(BuddyViewHolder holder, int position) {

        buddy = buddyList.get(position);
        final Buddy buddyObject = gson.fromJson(buddy, Buddy.class);

        username = buddyObject.getBuddyname();
        holder.buddyName.setText(buddyObject.getBuddyname());
        holder.buddyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(Constants.RecentChatFragment, buddyObject.getChannelName());
                ChatApplication.setRecipientChannel(buddyObject.getChannelName());
                context.startActivity(new Intent(context, ChatActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return buddyList.size();
    }


}
