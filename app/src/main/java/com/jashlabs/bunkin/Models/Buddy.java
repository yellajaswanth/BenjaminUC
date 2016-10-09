package com.jashlabs.bunkin.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jaswanth on 02-03-2016.
 */
public class Buddy {

    @SerializedName("buddyname")
    private String buddyname;
    @SerializedName("channelname")
    private String channelName;

    public Buddy( String buddyname, String channelName) {
        this.channelName = channelName;
        this.buddyname = buddyname;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getBuddyname() {
        return buddyname;
    }
}
