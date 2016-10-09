package com.jashlabs.bunkin.Fragments;

/**
 * Created by Jaswanth on 24-02-2016.
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.google.gson.Gson;
import com.jashlabs.bunkin.Activity.MainActivity;
import com.jashlabs.bunkin.Adapter.BuddyListAdapter;
import com.jashlabs.bunkin.Models.Buddy;
import com.jashlabs.bunkin.R;
import com.jashlabs.bunkin.Utils.Constants;
import com.jashlabs.bunkin.Utils.DatabaseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BuddyListFragment extends Fragment {

    RecyclerView buddyListView;
    ArrayList<String> buddyList;
    BuddyListAdapter buddyListAdapter;
    Gson gson;
    SharedPreferences sharedPreferences;
    String username;

    public BuddyListFragment() {
    }
    public static BuddyListFragment newInstance() {
        BuddyListFragment fragment = new BuddyListFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buddy_list, container, false);
        gson = new Gson();

        Firebase.setAndroidContext(getActivity());

        buddyList = new ArrayList<>();
        buddyListAdapter = new BuddyListAdapter(buddyList);

        buddyListView = (RecyclerView) view.findViewById(R.id.buddyList);
        buddyListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        buddyListView.setAdapter(buddyListAdapter);

        sharedPreferences = getActivity().getSharedPreferences(Constants.PREF_NAME, getActivity().MODE_PRIVATE);
        this.username = sharedPreferences.getString(Constants.PREF_USER_EMAIL, null);

        addUsersToList();
        return view;
    }

    private void addUsersToList() {

        Firebase ref = new Firebase(Constants.FIREBASE_URL);
        Query queryRef = ref.child(Constants.FIREBASE_PARENT_NAME).orderByChild("FullName");
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        try{
            if(!dataSnapshot.child("Channel").getValue(String.class).equals(username)){
                String buddyName = dataSnapshot.child("FullName").getValue(String.class);
                String buddyChannel = dataSnapshot.child("Channel").getValue(String.class);
                String buddy = gson.toJson(new Buddy(buddyName, buddyChannel));
                JSONObject obj = new JSONObject(buddy);
                buddyList.add(obj.toString());
                buddyListAdapter.notifyDataSetChanged();

                DatabaseHandler db = new DatabaseHandler(getActivity());
                db.addUser(buddyName, buddyChannel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }
}
