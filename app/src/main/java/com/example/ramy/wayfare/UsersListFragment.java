package com.example.ramy.wayfare;

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

import java.util.ArrayList;


public class UsersListFragment extends Fragment {
    String item;
    Bundle b;

    public UsersListFragment() {
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
        ArrayList<String> following = getArguments().getStringArrayList("following");
        ArrayList<String> followers = getArguments().getStringArrayList("followers");
        b=new Bundle();

        ArrayAdapter<String> adapter;
        if (getArguments().getInt("id")==1) {
            adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, following);
        }else {
            adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, followers);
        }

        // Assign adapter to List
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment1 = null;
                Class fragmentClass = ProfileFragment.class;
                try {
                    fragment1 = (Fragment) fragmentClass.newInstance();
                    item=(String) listView.getItemAtPosition(position);
                    b.putString("userprofile", item);
                    b.putBoolean("notmine", true);
                    fragment1.setArguments(b);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment1).addToBackStack(null);
                fragmentTransaction.commit();
                fragmentTransaction.addToBackStack(null);
            }
        });

        return rootView;
    }

}
