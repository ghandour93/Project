package com.example.ramy.wayfare;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    JSONArray not;
    ArrayList<String> notifications;
    View rootView;

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
        rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
        final ListView listView = (ListView) rootView.findViewById(R.id.listview);
        not=new JSONArray();

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
                    not = getNotifications("", "GET");
                    notifications = new ArrayList<>(not.length());
                    for (int i = 0; i < not.length(); i++) {
                        notifications.add(not.getJSONObject(i).getString("message"));
                    }
                    publishProgress();
                } catch (Exception e) {

                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, notifications);
                listView.setAdapter(adapter);
                super.onProgressUpdate(values);
            }
        }.execute();

        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        ((OnNotificationsFragmentSelected)getActivity()).onNotificationsFragmentDisplayed();
    }

    public void showLoading(){
        rootView.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.relative_lay).setVisibility(View.GONE);
    }

    public void hideLoading(){
        rootView.findViewById(R.id.progressBar).setVisibility(View.GONE);
        rootView.findViewById(R.id.relative_lay).setVisibility(View.VISIBLE);
    }

    public interface OnNotificationsFragmentSelected{
        void onNotificationsFragmentDisplayed();
    }
}
