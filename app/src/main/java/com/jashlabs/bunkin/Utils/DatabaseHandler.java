package com.jashlabs.bunkin.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.jashlabs.bunkin.Models.Message;
import com.jashlabs.bunkin.Models.Recent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jaswanth on 16-02-2016.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    Gson gson = new Gson();
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "bunkin";

    // Users table name
    private static final String TABLE_USERS = "users";
    private static final String TABLE_RECENT_CHATS = "recent_chat";


    //RecentChat Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_RECIPIENT_ID = "recipient_id";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_MESSAGE_TYPE = "message_type";
    private static final String KEY_RECEIVED_AT= "received_at";
    private static final String KEY_READ_STATUS= "read_status";
    private static final String KEY_WILL_EXPIRE = "willexpire";
    private static final String KEY_MESSAGE_EXPIRY = "expire_at";

    //User Table Column Names
    private static final String KEY_CHANNEL = "channel";
    private static final String KEY_LAST_CHAT_TIME = "last_chat_time";
    private static final String KEY_LAST_RECEIVED_MESSAGE = "last_received_message";
    private static final String KEY_LAST_RECEIVED_MESSAGE_TYPE = "last_received_message_type";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RECENT_CHAT_TABLE = "CREATE TABLE " + TABLE_RECENT_CHATS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_RECIPIENT_ID + " TEXT,"
                + KEY_CHANNEL + " TEXT,"
                + KEY_MESSAGE + " TEXT,"
                + KEY_MESSAGE_TYPE + " TEXT,"
                + KEY_READ_STATUS +" INTEGER,"
                + KEY_WILL_EXPIRE +" INTEGER,"
                + KEY_MESSAGE_EXPIRY +" DATETIME,"
                + KEY_RECEIVED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"+ ")";
        db.execSQL(CREATE_RECENT_CHAT_TABLE);

        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_RECIPIENT_ID + " TEXT,"
                + KEY_CHANNEL + " TEXT,"
                + KEY_LAST_RECEIVED_MESSAGE + " TEXT,"
                + KEY_LAST_RECEIVED_MESSAGE_TYPE + " TEXT,"
                + KEY_LAST_CHAT_TIME + " DATETIME" +
                ")";
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECENT_CHATS);
        // Create tables again
        onCreate(db);
    }


    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void addUser(String username, String channel){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = null;
        try{
            cursor =  db.rawQuery("SELECT recipient_id FROM users WHERE recipient_id=?", new String[] {String.valueOf(username)});
            if (cursor.getCount() <= 0){
                ContentValues values = new ContentValues();
                values.put(KEY_RECIPIENT_ID, username);
                values.put(KEY_CHANNEL, channel);
                db.insert(TABLE_USERS, null, values);
                System.out.println(username + " inserted succefully");
            }
        } finally {
            db.close(); // Closing database
        }

    }

    public void addMessage(String message, String username, String channel, String expiry){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RECIPIENT_ID, username);
        values.put(KEY_MESSAGE, message );
        values.put(KEY_CHANNEL, channel);
        values.put(KEY_READ_STATUS, 0);
        if(!expiry.isEmpty()) {
            values.put(KEY_MESSAGE_EXPIRY, formatDate(expiry).toString());
            values.put(KEY_WILL_EXPIRE, "1");
        } else{
            values.put(KEY_MESSAGE_EXPIRY, "");
            values.put(KEY_MESSAGE_EXPIRY, "0");
        }

        db.insert(TABLE_RECENT_CHATS, null, values);

        ContentValues values2 = new ContentValues();

        values2.put(KEY_LAST_CHAT_TIME, new Date().toString());
        values2.put(KEY_LAST_RECEIVED_MESSAGE, message);
        values2.put(KEY_LAST_RECEIVED_MESSAGE_TYPE, "text");
        db.update(TABLE_USERS, values2, KEY_CHANNEL + " = ?",
                new String[]{String.valueOf(channel)});

        db.close(); // Closing database conn

        System.out.println(message + " from " + username + " inserted succefully");

        getAllRecentChats();
    }


    public int readAllMessages(String channel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_READ_STATUS, 1);
        return db.update(TABLE_RECENT_CHATS, values, KEY_CHANNEL + " = ?",
                new String[] { String.valueOf(channel) });
    }

    public ArrayList<String> getAllMessages(String channelName) {
        ArrayList<String> chatMessageList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        gson = new Gson();
        try {

            cursor = db.rawQuery("SELECT * FROM recent_chat WHERE channel=?", new String[] {String.valueOf(channelName)});
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Log.d(Constants.ChatActivity, cursor.getString(0) + "-" + cursor.getString(1) + "-" + cursor.getString(3));

                    String msg = gson.toJson(new Message(String.valueOf(cursor.getString(1)), String.valueOf(cursor.getString(3)), String.valueOf(cursor.getString(2)), String.valueOf(cursor.getString(6)), String.valueOf(cursor.getString(8))));
                    chatMessageList.add(msg);
                } while (cursor.moveToNext());
            }
        } catch(Exception ex){
            Log.d(Constants.ChatActivity, ex.toString());
        }
        return chatMessageList;
    }

    public ArrayList<String> getAllRecentChats() {
        ArrayList<String> chatMessageList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;

        try {

            cursor = db.rawQuery("SELECT * FROM users order by datetime(last_chat_time) DESC",null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    int unreadCount = getUnreadMsgCount(cursor.getString(2));
                    if(!(String.valueOf(cursor.getString(3)).isEmpty())){
                        String msg = gson.toJson(new Recent(cursor.getString(1), cursor.getString(2), String.valueOf(cursor.getString(5)), String.valueOf(unreadCount), cursor.getString(3)));
                        chatMessageList.add(msg);

                        System.out.println("Added to recent" + msg);
                    }

                } while (cursor.moveToNext());
            }
        } catch(Exception ex){
            Log.d(Constants.ChatActivity, ex.toString());
        }
        Collections.reverse(chatMessageList);
        return chatMessageList;
    }

    private int getUnreadMsgCount(String channelName){
        int count = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {

            cursor = db.rawQuery("SELECT * FROM recent_chat WHERE read_status=? AND channel=?", new String[] {String.valueOf(0),String.valueOf(channelName)});
            count = cursor.getCount();
        } catch(Exception ex){
            Log.d(Constants.ChatActivity, ex.toString());
        }
        return count;
    }

    public void deleteExpiredMessages(){
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = KEY_MESSAGE_EXPIRY + "<=? AND " + KEY_WILL_EXPIRE + "=1 AND " + KEY_READ_STATUS + "=1";
        String[] whereArgs = new String[] { String.valueOf(new Date()) };
        db.delete(TABLE_RECENT_CHATS, whereClause, whereArgs);
    }

    public void deleteChannelChat(String channelName){
        System.out.println(channelName);
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = KEY_CHANNEL + "=?";
        String[] whereArgs = new String[] { String.valueOf(channelName) };
        db.delete(TABLE_RECENT_CHATS, whereClause, whereArgs);

        ContentValues values2 = new ContentValues();

        values2.put(KEY_LAST_RECEIVED_MESSAGE, "");
        db.update(TABLE_USERS, values2, KEY_CHANNEL + " = ?",
                new String[]{String.valueOf(channelName)});

        db.close(); // Closing database conn
    }

    private Date formatDate(String seconds){
        long time = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(Integer.parseInt(seconds));
        Date expiredDate = new Date(time);
        return expiredDate;
    }
}

