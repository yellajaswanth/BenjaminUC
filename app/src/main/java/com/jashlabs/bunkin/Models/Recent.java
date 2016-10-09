package com.jashlabs.bunkin.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jaswanth on 02-03-2016.
 */
public class Recent {

    @SerializedName("buddyname")
    private String buddyname;
    @SerializedName("channelname")
    private String channelName;
    @SerializedName("lastSeen")
    private String lastSeen;
    @SerializedName("unreadMsgs")
    private String unreadMsgs;
    @SerializedName("lastMsg")
    private String lastMsg;


    public Recent(String buddyname, String channelName, String lastSeen, String unreadMsgs, String lastMsg) {
        this.buddyname = buddyname;
        this.channelName = channelName;
        this.lastSeen = lastSeen;
        this.unreadMsgs = unreadMsgs;
        this.lastMsg = lastMsg;
    }

    public String getBuddyname() {
        return buddyname;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public String getUnreadMsgs() {
        return unreadMsgs;
    }


    public String getLastMsg() {
        return lastMsg;
    }
}
