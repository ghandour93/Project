package com.example.ramy.wayfare;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;;import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Feed_Fragment extends Fragment {
    JSONArray obj = new JSONArray();
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rv;
    ArrayList<JSONObject> arraylist;

    public Feed_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view);
        rv.setHasFixedSize(true);
        arraylist= refresh();
        FeedAdapter adapter = new FeedAdapter(arraylist);


        rv.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                arraylist= refresh();
                FeedAdapter adapter = new FeedAdapter(arraylist);

                swipeRefreshLayout.setRefreshing(false);
                rv.setAdapter(adapter);
            }
        });

        return rootView;
    }

    public ArrayList<JSONObject> refresh(){
        ArrayList<JSONObject> arraylist = new ArrayList<>();
        new AsyncTask<String, Void, Intent>() {

            ServerTask s = new ServerTask(getActivity(),((HomeActivity)getActivity()).getToken());
            @Override
            protected Intent doInBackground(String... params) {
                try {
                    obj=s.getFeed("", "GET", true);
                } catch (Exception e) {

                }
                return null;
            }
        }.execute();
        while(true) {
            if (obj.length()!=0) {
                for (int i = 0; i < obj.length(); i++) {
                    try {
                        arraylist.add(obj.getJSONObject(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
        return arraylist;
    }

}