package com.jashlabs.bunkin.Models;

/**
 * Created by Jaswanth on 24-02-2016.
 */
public class ChatList {
    private String userName;
    private String userChatLastMsg;
    private String userLastMsgDateTime;
    private String userImg;

    public ChatList(String userName, String userChatLastMsg, String userLastMsgDateTime, String userImg) {
        this.userName = userName;
        this.userChatLastMsg = userChatLastMsg;
        this.userLastMsgDateTime = userLastMsgDateTime;
        this.userImg = userImg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserChatLastMsg() {
        return userChatLastMsg;
    }

    public void setUserChatLastMsg(String userChatLastMsg) {
        this.userChatLastMsg = userChatLastMsg;
    }

    public String getUserLastMsgDateTime() {
        return userLastMsgDateTime;
    }

    public void setUserLastMsgDateTime(String userLastMsgDateTime) {
        this.userLastMsgDateTime = userLastMsgDateTime;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }
}
