package com.jashlabs.bunkin.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.jashlabs.bunkin.R;
import com.jashlabs.bunkin.Utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    EditText et_email, et_chatName, et_fullName;
    SharedPreferences sharedPreferences;
    private Firebase mFirebaseRef;
    private String SHAHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_login);

        et_email = (EditText) findViewById(R.id.emailId);
        et_chatName = (EditText) findViewById(R.id.chatName);
        et_fullName = (EditText) findViewById(R.id.fullName);

        loginButton = (Button) findViewById(R.id.login);

        sharedPreferences = getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString().trim();
                    if (email.length() != 0) {
                        sharedPreferences.edit().putString(Constants.PREF_USER_EMAIL, email).apply();
                        sharedPreferences.edit().putString(Constants.PREF_USER_FULL_NAME, et_fullName.getText().toString().trim()).apply();
                        sharedPreferences.edit().putString(Constants.PREF_USER_NICK, et_chatName.getText().toString().trim()).apply();
                        mFirebaseRef = new Firebase(Constants.FIREBASE_URL).child(Constants.FIREBASE_PARENT_NAME);
                        Map<String, Object> detailsObject = new HashMap<String, Object>();
                        detailsObject.put("CreatedAt", new Date().getTime());
                        detailsObject.put("FullName", et_fullName.getText().toString().trim());
                        detailsObject.put("ChatName", et_chatName.getText().toString().trim());
                        detailsObject.put("Channel", email);

//                        Map<String, Object> userObject = new HashMap<String, Object>();
//                        userObject.put(email, detailsObject);
//
                        System.out.println(detailsObject);
                        mFirebaseRef.child(email).updateChildren(detailsObject);

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Enter username", Toast.LENGTH_SHORT).show();
                    }
            }
        });

    }

    public String computeSHAHash(String text)
    {
        MessageDigest mdSha1 = null;
        try
        {
            mdSha1 = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e1) {
            Log.e("myapp", "Error initializing SHA1 message digest");
        }
        try {
            mdSha1.update(text.getBytes("ASCII"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] data = mdSha1.digest();
        try {
            SHAHash=convertToHex(data);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return SHAHash;
    }

    private static String convertToHex(byte[] data) throws java.io.IOException
    {


        StringBuffer sb = new StringBuffer();
        String hex=null;

        hex= Base64.encodeToString(data, 0, data.length, 0);

        sb.append(hex);

        return sb.toString();
    }
}
