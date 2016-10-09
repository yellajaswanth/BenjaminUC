package com.jashlabs.bunkin.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jashlabs.bunkin.R;
import com.jashlabs.bunkin.Utils.Constants;

/**
 * Created by Jaswanth on 26-02-2016.
 */
public class BuddyViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout buddyHolder;
    public TextView buddyName;


    public BuddyViewHolder(View itemView) {
        super(itemView);

        buddyName = (TextView) itemView.findViewById(R.id.buddyName);
        buddyHolder = (LinearLayout) itemView.findViewById(R.id.buddyHolder);
    }
}
