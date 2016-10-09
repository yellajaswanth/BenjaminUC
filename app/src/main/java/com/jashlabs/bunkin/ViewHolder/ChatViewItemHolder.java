package com.jashlabs.bunkin.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jashlabs.bunkin.R;

/**
 * Created by Jaswanth on 26-02-2016.
 */
public class ChatViewItemHolder extends RecyclerView.ViewHolder {

    public LinearLayout chatHolder, dateHolder;
    public TextView username;
    public TextView message;
    public TextView divider, expiry;
    public TextView tv_chatDate;



    public ChatViewItemHolder(View itemView) {
        super(itemView);

        username = (TextView) itemView.findViewById(R.id.username);
        message = (TextView) itemView.findViewById(R.id.message);
        chatHolder = (LinearLayout) itemView.findViewById(R.id.chatholder);
        dateHolder = (LinearLayout) itemView.findViewById(R.id.dateHolder);
        divider = (TextView) itemView.findViewById(R.id.divider);
        expiry = (TextView) itemView.findViewById(R.id.expiry);
        tv_chatDate = (TextView) itemView.findViewById(R.id.tv_chatDate);
    }
}
