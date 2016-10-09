package com.jashlabs.bunkin.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jashlabs.bunkin.R;
import com.jashlabs.bunkin.Utils.CircledNetworkImageView;
import com.jashlabs.bunkin.Utils.Constants;

/**
 * Created by Jaswanth on 26-02-2016.
 */
public class RecentChatViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

    public TextView buddyName, lastSeen, unreadMsgs, recentText;
    public LinearLayout recentHolder;
    public CircledNetworkImageView iv_userImg;


    public RecentChatViewHolder(View itemView) {
        super(itemView);

        recentHolder = (LinearLayout) itemView.findViewById(R.id.recentHolder);
        buddyName = (TextView) itemView.findViewById(R.id.buddyName);
        lastSeen = (TextView) itemView.findViewById(R.id.lastSeen);
        unreadMsgs = (TextView) itemView.findViewById(R.id.unreadMsgs);
        recentText = (TextView) itemView.findViewById(R.id.recentText);
        iv_userImg = (CircledNetworkImageView) itemView.findViewById(R.id.iv_userImg);
        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Choose Action");
        menu.add(0, Constants.MENU_CLEAR, getAdapterPosition(), "Clear");
        menu.add(0, Constants.MENU_DELETE, getAdapterPosition(), "Delete");
    }
}
