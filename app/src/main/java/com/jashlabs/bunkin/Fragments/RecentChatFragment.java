package com.jashlabs.bunkin.Fragments;

/**
 * Created by Jaswanth on 24-02-2016.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jashlabs.bunkin.Adapter.RecentChatListAdapter;
import com.jashlabs.bunkin.R;
import com.jashlabs.bunkin.Utils.ChatApplication;
import com.jashlabs.bunkin.Utils.Constants;
import com.jashlabs.bunkin.Utils.DatabaseHandler;

import java.util.ArrayList;

public class RecentChatFragment extends Fragment {

    RecyclerView recentChatListView;
    public ArrayList<String> recentList;
    public RecentChatListAdapter recentChatListAdapter;
    Gson gson;
    SharedPreferences sharedPreferences;
    String username;

    DatabaseHandler db;

    View v;

    Handler handler;

    public RecentChatFragment() {
    }



    public static RecentChatFragment newInstance() {
        RecentChatFragment fragment = new RecentChatFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_recent_chat, container, false);
        gson = new Gson();

        recentList = new ArrayList<>();
        db = new DatabaseHandler(getActivity());
        sharedPreferences = getActivity().getSharedPreferences(Constants.PREF_NAME, getActivity().MODE_PRIVATE);
        this.username = sharedPreferences.getString(Constants.PREF_USER_EMAIL, null);

        v = view;
        getActivity().registerReceiver(mReceiver, new IntentFilter(Constants.BROADCAST_INTENT_FILTER));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


        recentChatListAdapter = new RecentChatListAdapter(recentList);

        recentChatListView = (RecyclerView) v.findViewById(R.id.recentChatList);
        registerForContextMenu(recentChatListView);


        recentChatListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recentChatListView.setAdapter(recentChatListAdapter);

        recentList.clear();
        recentList.addAll(db.getAllRecentChats());
        recentChatListAdapter.refresh(recentList);

    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();
            if(b.getString("updateList").equals("true")){
                System.out.println("List Update called");
                recentList.clear();
                recentList.addAll(db.getAllRecentChats());
                recentChatListAdapter.refresh(recentList);
            }
        }
    };



    @Override
    public void onPause() {
        super.onPause();
        try {
            getActivity().unregisterReceiver(mReceiver);
        }catch (Exception ex){}
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println(newText);
                recentChatListAdapter.getFilter().filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_search){

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getItemId() == 1){
            db.deleteChannelChat(ChatApplication.getRecipientChannel());
            recentList.clear();
            recentList.addAll(db.getAllRecentChats());
            recentChatListAdapter.refresh(recentList);
        } else if(item.getItemId() == 2){
            Log.d(Constants.RecentChatFragment, String.valueOf(item.getItemId()));
            Toast.makeText(getActivity(), "Delete", Toast.LENGTH_SHORT).show();
        }

        // do something!
        return super.onContextItemSelected(item);
    }
}
