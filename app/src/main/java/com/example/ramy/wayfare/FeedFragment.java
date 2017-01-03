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

import org.json.JSONObject;

import java.util.ArrayList;

public class FeedFragment extends Fragment {
    JSONArray obj = new JSONArray();
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rv;
    View rootView;
    ArrayList<JSONObject> arraylist;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        refresh();
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                refresh();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return rootView;
    }

    public void showLoading(){
        rootView.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.relative_lay).setVisibility(View.GONE);
    }

    public void hideLoading(){
        rootView.findViewById(R.id.progressBar).setVisibility(View.GONE);
        rootView.findViewById(R.id.relative_lay).setVisibility(View.VISIBLE);
    }

    public void refresh(){
        arraylist = new ArrayList<>();
        new ServerTask(getActivity(),((HomeActivity)getActivity()).getToken()){

            @Override
            protected void onPreExecute() {
                showLoading();
            }

            @Override
            protected void onPostExecute(String result) {
                hideLoading();
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    obj=getFeed("", "GET", true);
                        for (int i = 0; i < obj.length(); i++) {
                            arraylist.add(obj.getJSONObject(i));
                        }
                    publishProgress();

                } catch (Exception e) {

                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view);
                rv.setHasFixedSize(true);
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                rv.setLayoutManager(llm);
                FeedAdapter adapter = new FeedAdapter(arraylist);
                rv.setAdapter(adapter);
                super.onProgressUpdate(values);
            }
        }.execute();
    }

}