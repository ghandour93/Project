package com.example.ramy.wayfare;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    FloatingActionButton fab_post;
    FloatingActionButton fab_text;
    FloatingActionButton fab_image;
    ArrayList<JSONObject> arraylist;
    Bundle b;
    String username;
    Boolean bool;
    private EndlessRecyclerViewScrollListener scrollListener;
    FeedAdapter adapter;
    int items;

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
        arraylist = new ArrayList<>();
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);
        adapter = new FeedAdapter(arraylist);
        rv.setAdapter(adapter);
        scrollListener = new EndlessRecyclerViewScrollListener(llm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                refresh(totalItemsCount);
            }
        };
        refresh(0);
        rv.addOnScrollListener(scrollListener);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                refresh(0);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        if (getParentFragment() == null)
            ((OnFeedFragmentSelected)getActivity()).onFeedFragmentDisplayed();
    }

    public void showLoading(){
        rootView.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.relative_lay).setVisibility(View.GONE);
    }

    public void hideLoading(){
        rootView.findViewById(R.id.progressBar).setVisibility(View.GONE);
        rootView.findViewById(R.id.relative_lay).setVisibility(View.VISIBLE);
    }

    public void refresh(int itemNumber){
        items=itemNumber;

        new ServerTask(getActivity(),((HomeActivity)getActivity()).getToken()){

            @Override
            protected void onPreExecute() {
                if (items == 0) {
                    arraylist.clear();
                    showLoading();
                    scrollListener.resetState();
                }

            }

            @Override
            protected void onPostExecute(String result) {
                if (items == 0)
                    hideLoading();
                adapter.notifyDataSetChanged();

            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    if(getArguments()!=null && getArguments().containsKey("username")){
                        obj = getFeed(getArguments().getString("username"), false, false, items);
                    }else {
                        obj = getFeed("", true, false, items);
                    }
                        for (int i = 0; i < obj.length(); i++) {
                            arraylist.add(obj.getJSONObject(i));
                        }

                } catch (Exception e) {

                }
                return null;
            }
        }.execute();
    }

    public interface OnFeedFragmentSelected{
        void onFeedFragmentDisplayed();
    }

}