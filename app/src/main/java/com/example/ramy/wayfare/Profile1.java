package com.example.ramy.wayfare;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;;import static com.example.ramy.wayfare.R.attr.layoutManager;

public class Profile1 extends Fragment {

    public Profile1() {
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
        View rootView = inflater.inflate(R.layout.activity_profile1, container, false);

//        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view);
//
//        rv.setHasFixedSize(true);
//        MyAdapter adapter = new MyAdapter(new String[]{"text", "image", "test three", "test four", "test five" , "test six" , "test seven"});
//
//
//        rv.setAdapter(adapter);
//        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
//        rv.setLayoutManager(llm);

        return rootView;
    }

}
