package com.jashlabs.bunkin.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jaswanth on 26-02-2016.
 */
public class Message {

    @SerializedName("username")
    String username;
    @SerializedName("message")
    String message;
    @SerializedName("channel")
    String channel;
    @SerializedName("expiry")
    String expiry;
    @SerializedName("receivedat")
    String receivedAt;


    public Message(String username, String message, String channel, String expiry, String receivedAt) {
        this.username = username;
        this.message = message;
        this.channel = channel;
        this.expiry = expiry;
        this.receivedAt = receivedAt;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public String getChannel() {
        return channel;
    }

    public String getExpiry() {
        return expiry;
    }

    public String getReceivedAt() {
        return receivedAt;
    }
}
