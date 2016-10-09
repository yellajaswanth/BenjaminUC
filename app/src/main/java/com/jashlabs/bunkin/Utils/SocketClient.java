package com.jashlabs.bunkin.Utils;

import android.content.Context;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Socket;


/**
 * Created by Jaswanth on 24-02-2016.
 */
public class SocketClient {
    io.socket.client.Socket socket;
    private static SocketClient mInstance = null;

    public static SocketClient getInstance() {
        if(mInstance == null)
        {
            mInstance = new SocketClient();
        }
        return mInstance;
    }

    private SocketClient() {
        try {
            socket = IO.socket(Constants.SOCKET_URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.on(io.socket.client.Socket.EVENT_CONNECT, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        System.out.println("Socket Connected");
                    }
                }
        ).on("chat message", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        System.out.println("New Display Updated" + args[0].toString());

                    }
                }
        ).on(io.socket.client.Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        System.out.println("Socket Disconnected");
                    }
                }
        );
        socket.connect();
    }

    public void createUser(){
        JSONObject obj = null;
        try {
            obj = new JSONObject();
            obj.put("username", "jash");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("newuser", obj);
    }

}

