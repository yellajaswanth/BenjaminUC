package com.jashlabs.bunkin.Fragments;

/**
 * Created by Jaswanth on 24-02-2016.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jashlabs.bunkin.R;
import com.jashlabs.bunkin.Utils.ChatApplication;
import com.jashlabs.bunkin.Utils.Constants;
import com.jashlabs.bunkin.Utils.SocketClient;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecentFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private Socket mSocket;
    private TextView testClick;

    public RecentFragment() {
    }

    public static RecentFragment newInstance() {
        RecentFragment fragment = new RecentFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chat_fragment, container, false);

        return rootView;
    }
}
