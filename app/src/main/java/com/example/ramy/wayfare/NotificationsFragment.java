package com.example.ramy.wayfare;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    JSONArray not;
    ArrayList<String> notifications;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_userslist, container, false);
        final ListView listView = (ListView) rootView.findViewById(R.id.listview);
        not=new JSONArray();
        new AsyncTask<String, Void, Intent>() {

            ServerTask s = new ServerTask(getActivity(), ((HomeActivity)getActivity()).getToken());

            @Override
            protected Intent doInBackground(String... params) {
                try {
                        not = s.getNotifications("", "GET");
                    notifications = new ArrayList<>(not.length());
                    for (int i = 0; i < not.length(); i++) {
                        notifications.add(not.getJSONObject(i).getString("message"));
                    }
                } catch (Exception e) {

                }

                return null;
            }
        }.execute();
        while(true) {
            if (not.length() !=0) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, notifications);
                listView.setAdapter(adapter);
                break;
            }
        }
        return rootView;
    }
}
