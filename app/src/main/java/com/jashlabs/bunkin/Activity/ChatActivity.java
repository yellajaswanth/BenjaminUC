package com.jashlabs.bunkin.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jashlabs.bunkin.Adapter.ChatAdapter;
import com.jashlabs.bunkin.CustomCallback.CustomCallback;
import com.jashlabs.bunkin.Models.Message;
import com.jashlabs.bunkin.R;
import com.jashlabs.bunkin.Services.PubnubService;
import com.jashlabs.bunkin.Utils.ChatApplication;
import com.jashlabs.bunkin.Utils.Constants;
import com.jashlabs.bunkin.Utils.DatabaseHandler;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    String TAG = "ChatActivity";
    SharedPreferences sharedPreferences;
    CustomCallback callback;
    Context context;
    Pubnub pubnub;
    EditText chatMessage;
    Button send;
    RecyclerView chatList;
    ArrayList<String> chatMessageList;
    ChatAdapter chatAdapter;
    Gson gson;
    JSONObject messageObject;
    String username;
    String channel;

    private String SHAHash;
    public static int NO_OPTIONS=0;
    DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Bundle extras = getIntent().getExtras();
        String userName;

        if (extras != null) {
            ChatApplication.setRecipientChannel(extras.getString("channel"));
        }


        getSupportActionBar().setTitle(ChatApplication.getRecipientChannel());

        context = ChatActivity.this;
        sharedPreferences = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();

        username = sharedPreferences.getString(Constants.PREF_USER_EMAIL, null);
        this.channel = sharedPreferences.getString(Constants.PREF_NAME, Constants.PREF_USER_CHANNEL);

        if (sharedPreferences.getString(Constants.PREF_USER_EMAIL, null) == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else{
            startService(new Intent(ChatActivity.this, PubnubService.class));
        }

        db = new DatabaseHandler(this);


        chatMessageList = new ArrayList<>();
        if(!db.getAllMessages(ChatApplication.getRecipientChannel()).isEmpty()) {
            chatMessageList = db.getAllMessages(ChatApplication.getRecipientChannel());
        }
        chatAdapter = new ChatAdapter(chatMessageList, username);

        chatList = (RecyclerView) findViewById(R.id.chatlist);
        chatList.setLayoutManager(new LinearLayoutManager(context));
        chatList.setAdapter(chatAdapter);

        chatMessage = (EditText) findViewById(R.id.message);
        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String m = chatMessage.getText().toString().trim().toLowerCase();
                if(m.contains("*book cab*")) {
                    openApp(ChatActivity.this,"com.ubercab");
                } else if(m.contains("*help*")) {
                    String help_items = "Help: \n 1.*book cab* \n 2.*Movie:<Movie Name>*" ;
                    String m2 = gson.toJson(new Message(username, help_items, username, "", new Date().toString()));
                    db.addMessage(username, help_items, username, "");
                    chatMessageList.add(m2);
                    scrollListDown();

                }else if(m.contains("*movie:")) {

                    String movieName = "{\n" +
                            "  \"Title\": \"The Dark Knight\",\n" +
                            "  \"Year\": \"2008\",\n" +
                            "  \"Rated\": \"PG-13\",\n" +
                            "  \"Released\": \"18 Jul 2008\",\n" +
                            "  \"Runtime\": \"152 min\",\n" +
                            "  \"Genre\": \"Action, Crime, Drama\",\n" +
                            "  \"Director\": \"Christopher Nolan\",\n" +
                            "  \"Writer\": \"Jonathan Nolan (screenplay), Christopher Nolan (screenplay), Christopher Nolan (story), David S. Goyer (story), Bob Kane (characters)\",\n" +
                            "  \"Actors\": \"Christian Bale, Heath Ledger, Aaron Eckhart, Michael Caine\",\n" +
                            "  \"Plot\": \"When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, the caped crusader must come to terms with one of the greatest psychological tests of his ability to fight injustice.\",\n" +
                            "  \"Language\": \"English, Mandarin\",\n" +
                            "  \"Country\": \"USA, UK\",\n" +
                            "  \"Awards\": \"Won 2 Oscars. Another 146 wins & 142 nominations.\",\n" +
                            "  \"Poster\": \"https:\\/\\/images-na.ssl-images-amazon.com\\/images\\/M\\/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1_SX300.jpg\",\n" +
                            "  \"Metascore\": \"82\",\n" +
                            "  \"imdbRating\": \"9.0\",\n" +
                            "  \"imdbVotes\": \"1,699,835\",\n" +
                            "  \"imdbID\": \"tt0468569\",\n" +
                            "  \"Type\": \"movie\",\n" +
                            "  \"Response\": \"True\"\n" +
                            "}";
                    try{
                        JSONObject obj = new JSONObject(movieName);
                        String mName = obj.getString("Title");
                        String mRating = obj.getString("imdbRating");
                        String mDirector = obj.getString("Director");
                        String mPlot = obj.getString("Plot");
                        String mItems = "Title:" + mName+"\n" + "IMDB Rating:" +mRating+"\n"+ "Director:"+mDirector+"\n" +"Plot:" + mPlot;
                        sendMessage(mItems, "");
                    } catch (Exception ex){}
                } else if(m.contains("*nearby*")) {
                    String nRestos = "1. Corinthian Restaurant & Lounge \n 2. New Amber Restaurant \n 3. Cactus Pear \n 4. Chick-fil-A";
                    sendMessage(nRestos, "");
                } else{
                    sendMessage(chatMessage.getText().toString().trim(), "");
                }
            }
        });
        send.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                sendMessage(chatMessage.getText().toString().trim(), "10");
                return true;
            }
        });
        scrollListDown();

        pubnub = new Pubnub(Constants.PUBLISH_KEY, Constants.SUBSCRIBE_KEY);
        history();
        try {
            pubnub.subscribe(username, new Callback() {
                @Override
                public void successCallback(String channel, Object message) {
                    super.successCallback(channel, message);
                    chatMessageList.add(message.toString());
                    Message msg = gson.fromJson(message.toString(), Message.class);
                    db.addMessage(msg.getMessage(), msg.getUsername(), ChatApplication.getRecipientChannel(), msg.getExpiry());
                    scrollListDown();
                    Log.d("successCallback", "message " + message);
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
            Log.d(TAG, pe.toString());
        }

    }

    private void scrollListDown() {
        ChatActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chatAdapter.notifyItemInserted(chatMessageList.size() - 1);
                chatList.scrollToPosition(chatMessageList.size() - 1);
                db.readAllMessages(ChatApplication.getRecipientChannel());
            }
        });
    }

    public static boolean openApp(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        Intent i = manager.getLaunchIntentForPackage(packageName);
        if (i == null) {
            return false;
        }
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        context.startActivity(i);
        return true;
    }


    public void sendMessage(String msg, final String expiry){
            String message = msg;
            final String pubMsg = message;
            String channelname = username;

            if (message.length() != 0) {
                message = gson.toJson(new Message(username, message, channelname, expiry, new Date().toString()));
                final String gsonMsg = message;
                Log.d("successCallback", "message " + gsonMsg);

                try {
                    messageObject = new JSONObject(message);
                } catch (JSONException je) {
                    Log.d(TAG, je.toString());
                }
                chatMessage.setText("");
                pubnub.publish(ChatApplication.getRecipientChannel(), messageObject, new Callback() {
                    @Override
                    public void successCallback(String channel, Object message) {
                        super.successCallback(channel, message);
                        Log.d("successCallback", "Message " + message);

                        db.addMessage(pubMsg, username, channel, expiry);

                        chatMessageList.add(gsonMsg);
                        scrollListDown();


                    }
                    @Override
                    public void errorCallback(String channel, PubnubError error) {
                        super.errorCallback(channel, error);
                        Log.d("errorCallback", "error " + error);
                    }
                });
            } else {
                Toast.makeText(context, "Please enter message", Toast.LENGTH_SHORT).show();
            }

    }

    public void history(){
        this.pubnub.history(username,100,false,new Callback() {
            @Override
            public void successCallback(String channel, final Object message) {
                try {
                    JSONArray json = (JSONArray) message;
                    Log.d("History", json.toString());
                    final JSONArray messages = json.getJSONArray(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void errorCallback(String channel, PubnubError error) {
                Log.d("History", error.toString());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ChatApplication.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ChatApplication.activityPaused();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChatApplication.activityPaused();
    }
}
