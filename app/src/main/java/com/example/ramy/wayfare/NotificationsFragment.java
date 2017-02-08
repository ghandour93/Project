package com.example.ramy.wayfare;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    JSONArray obj = new JSONArray();
    ArrayList<JSONObject> arrayList = new ArrayList<>();
    View rootView;
    ListView listView;

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
        listView = (ListView) rootView.findViewById(R.id.listview);
        final NotificationsAdapter adapter = new NotificationsAdapter(getActivity(), R.layout.notification_item, arrayList);
        listView.setAdapter(adapter);

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
                    obj = getNotifications("", "GET");
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
                adapter.notifyDataSetChanged();
                super.onProgressUpdate(values);
            }
        }.execute();

        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        ((OnNotificationsFragmentSelected)getActivity()).onNotificationsFragmentDisplayed();
        arrayList.clear();
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
