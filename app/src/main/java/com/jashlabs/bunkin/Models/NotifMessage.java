package com.jashlabs.bunkin.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jaswanth on 26-02-2016.
 */
public class NotifMessage {

    @SerializedName("username")
    String username;
    @SerializedName("message")
    String message;
    @SerializedName("channel")
    String channel;
    @SerializedName("expiry")
    String expiry;

    public NotifMessage(String username, String message, String channel, String expiry) {
        this.username = username;
        this.message = message;
        this.channel = channel;
        this.expiry = expiry;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public String getChannel(){ return channel;}

    public String getExpiry() {
        return expiry;
    }
}
