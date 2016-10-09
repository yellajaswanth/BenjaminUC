package com.jashlabs.bunkin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import com.jashlabs.bunkin.Activity.ChatActivity;
import com.jashlabs.bunkin.Models.Buddy;
import com.jashlabs.bunkin.Models.Recent;
import com.jashlabs.bunkin.R;
import com.jashlabs.bunkin.Utils.ChatApplication;
import com.jashlabs.bunkin.Utils.Constants;
import com.jashlabs.bunkin.ViewHolder.BuddyViewHolder;
import com.jashlabs.bunkin.ViewHolder.RecentChatViewHolder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Jaswanth on 26-02-2016.
 */
public class RecentChatListAdapter extends RecyclerView.Adapter<RecentChatViewHolder> implements Filterable {

    ArrayList<String> buddyList;
    ArrayList<String> filterList;
    Gson gson = new Gson();
    String buddy;
    String username;
    Context context;
    ValueFilter valueFilter;

    ImageLoader imageLoader = ChatApplication.getInstance().getImageLoader();

    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public RecentChatListAdapter(ArrayList<String> buddyList) {
        this.buddyList = buddyList;
        this.filterList = buddyList;
    }

    @Override
    public RecentChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new RecentChatViewHolder(LayoutInflater.from(context).inflate
                (R.layout.item_recent_chat, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecentChatViewHolder holder, int position) {

        buddy = buddyList.get(position);
        final Recent recentObject = gson.fromJson(buddy, Recent.class);

        username = recentObject.getBuddyname();
        holder.buddyName.setText(recentObject.getBuddyname());
        holder.recentHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(Constants.RecentChatFragment, recentObject.getChannelName());
                ChatApplication.setRecipientChannel(recentObject.getChannelName());
                context.startActivity(new Intent(context, ChatActivity.class));
            }
        });


        try {

            holder.lastSeen.setText(formatToYesterdayOrToday(recentObject.getLastSeen()));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if(!recentObject.getUnreadMsgs().equals("0")){
            if(recentObject.getUnreadMsgs().equals("1")){
                holder.unreadMsgs.setText(recentObject.getUnreadMsgs() + " unread message");
                holder.unreadMsgs.setVisibility(View.VISIBLE);
            } else {
                holder.unreadMsgs.setText(recentObject.getUnreadMsgs() + " unread messages");
                holder.unreadMsgs.setVisibility(View.VISIBLE);
            }

        }


        holder.recentText.setText(recentObject.getLastMsg());
        if (imageLoader == null)
            imageLoader = ChatApplication.getInstance().getImageLoader();
        holder.iv_userImg.setImageUrl("https://s3.amazonaws.com/uifaces/faces/twitter/adhamdannaway/128.jpg", imageLoader);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getPosition());
                ChatApplication.setRecipientChannel(recentObject.getChannelName());
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return buddyList.size();
    }

    public void refresh(ArrayList<String> buddyList){
        this.buddyList = buddyList;
        notifyDataSetChanged();
    }

    @Override
    public void onViewRecycled(RecentChatViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    public static String formatToYesterdayOrToday(String date) throws ParseException {
        Date dateTime = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy").parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        Calendar today = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        DateFormat timeFormatter = new SimpleDateFormat("hh:mma");

        if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            return timeFormatter.format(dateTime);
        } else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
            return "Yesterday";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy",
                    Locale.ENGLISH);
            Date parsedDate = sdf.parse(date);
            SimpleDateFormat print = new SimpleDateFormat("dd/MM/yyyy");
            return print.format(parsedDate).toString();
        }
    }


    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            System.out.println(constraint);
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<String> mfilterList = new ArrayList<>();
                for (int i=0; i < filterList.size(); i++){
                    String fBuddy = filterList.get(i);
                    Recent recentObject = gson.fromJson(fBuddy, Recent.class);
                    if ( (recentObject.getBuddyname().toUpperCase() )
                            .contains(constraint.toString().toUpperCase())) {
                        fBuddy = gson.toJson(new Recent(recentObject.getBuddyname(),recentObject.getChannelName(), recentObject.getLastSeen(), recentObject.getUnreadMsgs(), recentObject.getLastMsg()));
                        mfilterList.add(fBuddy);
                    }
                }
                results.count = mfilterList.size();
                results.values = mfilterList;
            } else {
                results.count = filterList.size();
                results.values = filterList;
            }


            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            buddyList = (ArrayList<String>) results.values;
            notifyDataSetChanged();
        }
    }
}
