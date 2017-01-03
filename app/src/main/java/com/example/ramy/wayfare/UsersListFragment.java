package com.example.ramy.wayfare;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class UsersListFragment extends Fragment {
    JSONObject item = new JSONObject();
    Bundle b;
    JSONArray obj = new JSONArray();
    ListView listView;
    View rootView;
    ArrayList<JSONObject> arrayList = new ArrayList<>();

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

        rootView = inflater.inflate(R.layout.fragment_userslist, container, false);
        listView = (ListView) rootView.findViewById(R.id.listview);
        arrayList.clear();
        b=new Bundle();
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

                    obj=getProfiles(getArguments().getInt("id"), getArguments().getString("username"), "POST");
                    for (int i = 0; i < obj.length(); i++) {
                        arrayList.add(obj.getJSONObject(i));
                    }
                    publishProgress();

                } catch (Exception e) {

                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                try {
                    String json = "{'token': '" + ((HomeActivity)getActivity()).getToken() + "'}";
                    JSONObject tkn = new JSONObject(json);
                    arrayList.add(tkn);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                UsersAdapter adapter = new UsersAdapter(getActivity(), R.layout.user_item, arrayList);
                listView.setAdapter(adapter);
                super.onProgressUpdate(values);
            }
        }.execute();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = null;
                Class fragmentClass = ProfileFragment.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    item =(JSONObject) listView.getItemAtPosition(position);
                    b.putString("userprofile", item.getString("user"));
                    b.putBoolean("notmine", true);
                    fragment.setArguments(b);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.relative_lay, fragment).addToBackStack(null);
                fragmentTransaction.commit();
                fragmentTransaction.addToBackStack(null);
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

}
