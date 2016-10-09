package com.jashlabs.bunkin.Utils;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.jashlabs.bunkin.Adapter.RecentChatListAdapter;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;


/**
 * Created by Jaswanth on 24-02-2016.
 */
public class ChatApplication extends Application {

    private static boolean activityVisible;

    private static RecentChatListAdapter recentChatListAdapter;
    private static ArrayList<String> recentList;

    public static final String TAG = ChatApplication.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private static String recipientChannel;

    private static ChatApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized ChatApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }


    public static String getRecipientChannel() {
        return recipientChannel;
    }

    public static void setRecipientChannel(String recipientChannel) {
        ChatApplication.recipientChannel = recipientChannel;
    }

    public static RecentChatListAdapter getRecentChatListAdapter() {
        return recentChatListAdapter;
    }

    public static void setRecentChatListAdapter(RecentChatListAdapter recentChatListAdapter) {
        ChatApplication.recentChatListAdapter = recentChatListAdapter;
    }

    public static ArrayList<String> getRecentList() {
        return recentList;
    }

    public static void setRecentList(ArrayList<String> recentList) {
        ChatApplication.recentList = recentList;
    }
}
