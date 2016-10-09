package com.jashlabs.bunkin.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.jashlabs.bunkin.Activity.ChatActivity;
import com.jashlabs.bunkin.Fragments.RecentChatFragment;
import com.jashlabs.bunkin.Models.NotifMessage;
import com.jashlabs.bunkin.R;
import com.jashlabs.bunkin.Utils.ChatApplication;
import com.jashlabs.bunkin.Utils.Constants;
import com.jashlabs.bunkin.Utils.DatabaseHandler;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

/**
 * Created by Jash on 3/8/2016.
 */
public class PubnubService extends Service {

    Pubnub pubnub;
    SharedPreferences sharedPreferences;
    String username;
    Handler handler;
    DatabaseHandler db;
    Context ctx;

    public static String BROADCAST_ACTION = "com.jashlabs.bunkin.broadcastaction";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getApplicationContext().getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        username = sharedPreferences.getString(Constants.PREF_USER_EMAIL, null);
        pubnub = new Pubnub(Constants.PUBLISH_KEY, Constants.SUBSCRIBE_KEY);
        ctx = getApplicationContext();


        db = new DatabaseHandler(ctx);
        subscribePubnub();
        delExpMsgs();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void subscribePubnub(){
        try {
            pubnub.subscribe(username, new Callback() {
                @Override
                public void successCallback(String channel, Object message) {
                    super.successCallback(channel, message);
                    Log.d(Constants.PubnubService, "message " + message);
                    if(!ChatApplication.isActivityVisible()){
                        Gson gson = new Gson();
                        NotifMessage msgObj = gson.fromJson(message.toString(), NotifMessage.class);

                        db.addMessage(msgObj.getMessage(), msgObj.getUsername(), msgObj.getChannel(), msgObj.getExpiry());

                        Intent i = new Intent();
                        i.setAction(Constants.BROADCAST_INTENT_FILTER);
                        i.putExtra("updateList", "true");
                        sendBroadcast(i);

                        Intent intent = new Intent(ctx, ChatActivity.class);
                        intent.putExtra("channel", msgObj.getChannel());
                        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                        NotificationCompat.Builder b = new NotificationCompat.Builder(ctx);

                        b.setAutoCancel(true)
                                .setDefaults(Notification.DEFAULT_ALL)
                                .setWhen(System.currentTimeMillis())
                                .setSmallIcon(R.drawable.ic_notify)
                                .setContentTitle(msgObj.getUsername())
                                .setContentText(msgObj.getMessage())
                                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                                .setContentIntent(contentIntent);


                        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(1234, b.build());
                    }
                }

                @Override
                public void successCallback(String channel, Object message, String timetoken) {
                    super.successCallback(channel, message, timetoken);
                }

                @Override
                public void errorCallback(String channel, PubnubError error) {
                    super.errorCallback(channel, error);
                    Log.d("errorCallback", "error " + error);
                }

                @Override
                public void connectCallback(String channel, Object message) {
                    super.connectCallback(channel, message);
                    Log.d("connectCallback", "message " + message);
                }

                @Override
                public void reconnectCallback(String channel, Object message) {
                    super.reconnectCallback(channel, message);
                    Log.d("reconnectCallback", "message " + message);
                }

                @Override
                public void disconnectCallback(String channel, Object message) {
                    super.disconnectCallback(channel, message);
                    Log.d("disconnectCallback", "message " + message);
                }
            });
        } catch (PubnubException pe) {
            Log.d(Constants.PubnubService, pe.toString());
        }
    }


    private void delExpMsgs(){
        handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                db.deleteExpiredMessages();
                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(r, 1000);
    }



}
