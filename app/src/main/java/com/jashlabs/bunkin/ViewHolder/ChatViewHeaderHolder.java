package com.jashlabs.bunkin.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jashlabs.bunkin.R;

/**
 * Created by Jaswanth on 26-02-2016.
 */
public class ChatViewHeaderHolder extends RecyclerView.ViewHolder {

    public TextView tv_chatDate;
    public LinearLayout dateHolder;

    public ChatViewHeaderHolder(View itemView) {
        super(itemView);
        tv_chatDate = (TextView) itemView.findViewById(R.id.tv_chatDate);
        dateHolder = (LinearLayout) itemView.findViewById(R.id.dateHolder);
    }
}
