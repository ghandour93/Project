package com.example.ramy.wayfare;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;;import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.ramy.wayfare.R.attr.layoutManager;

public class fragmentOne extends Fragment {
    JSONArray obj = new JSONArray();
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rv;
    ArrayList<JSONObject> arraylist;

    public fragmentOne() {
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
        View rootView = inflater.inflate(R.layout.activity_fragment_one, container, false);

        rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view);
        rv.setHasFixedSize(true);
        arraylist= refresh();
        MyAdapter adapter = new MyAdapter(arraylist);


        rv.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                arraylist= refresh();
                MyAdapter adapter = new MyAdapter(arraylist);

                swipeRefreshLayout.setRefreshing(false);
                rv.setAdapter(adapter);
            }
        });

        return rootView;
    }

    public ArrayList<JSONObject> refresh(){
        ArrayList<JSONObject> arraylist = new ArrayList<>();
        new AsyncTask<String, Void, Intent>() {

            ServerTask s = new ServerTask(getActivity());
            @Override
            protected Intent doInBackground(String... params) {
                try {
                    obj=s.getFeed("qoqo");
                    Log.i("ramy","ah");
                    Log.i("ramy", obj.getJSONObject(0).toString());
                } catch (Exception e) {

                }
                return null;
            }
        }.execute();
        while(true) {
            if (obj.length()!=0) {
                try {
                    Log.i("ramy", obj.getJSONObject(0).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < obj.length(); i++) {
                    try {
                        Log.i("Print", obj.getJSONObject(i).toString());
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

//private class AsyncFetch extends AsyncTask<String, String, String> {
//
//     ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
//     HttpURLConnection conn;
//     URL url = null;
//
//     @Override
//     protected void onPreExecute() {
//        super.onPreExecute();
//
//        //this method will be running on UI thread
//        pdLoading.setMessage("\tLoading...");
//        pdLoading.setCancelable(false);
//        pdLoading.show();
//
//    }
//
//     @Override
//     protected String doInBackground(String... params) {
//        try {
//
//            // Enter URL address where your json file resides
//            // Even you can make call to php file which returns json data
//            url = new URL("http://192.168.1.7/test/example.json");
//
//        } catch (MalformedURLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return e.toString();
//        }
//        try {
//
//            // Setup HttpURLConnection class to send and receive data from php and mysql
//            conn = (HttpURLConnection) url.openConnection();
//            conn.setReadTimeout(READ_TIMEOUT);
//            conn.setConnectTimeout(CONNECTION_TIMEOUT);
//            conn.setRequestMethod("GET");
//
//            // setDoOutput to true as we recieve data from json file
//            conn.setDoOutput(true);
//
//        } catch (IOException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//            return e1.toString();
//        }
//
//        try {
//
//            int response_code = conn.getResponseCode();
//
//            // Check if successful connection made
//            if (response_code == HttpURLConnection.HTTP_OK) {
//
//                // Read data sent from server
//                InputStream input = conn.getInputStream();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
//                StringBuilder result = new StringBuilder();
//                String line;
//
//                while ((line = reader.readLine()) != null) {
//                    result.append(line);
//                }
//
//                // Pass data to onPostExecute method
//                return (result.toString());
//
//            } else {
//
//                return ("unsuccessful");
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return e.toString();
//        } finally {
//            conn.disconnect();
//        }
//
//
//    }
//
//    @Override
//    protected void onPostExecute(String result) {
//
//        //this method will be running on UI thread
//
//        pdLoading.dismiss();
//        List<Data> data=new ArrayList<>();
//
//        pdLoading.dismiss();
//        try {
//
//            JSONArray jArray = new JSONArray(result);
//
//            // Extract data from json and store into ArrayList as class objects
//            for(int i=0;i<jArray.length();i++){
//                JSONObject json_data = jArray.getJSONObject(i);
//                Data fishData = new Data();
//                fishData.image= json_data.getString("fish_img");
//                fishData.name= json_data.getString("fish_name");
//                fishData.time= json_data.getString("cat_name");
//                fishData.location= json_data.getString("cat_name");
//                data.add(fishData);
//            }
//
//            // Setup and Handover data to recyclerview
//            mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);
//            mAdapter = new AdapterFish(MainActivity.this, data);
//            mRVFishPrice.setAdapter(mAdapter);
//            mRVFishPrice.setLayoutManager(new LinearLayoutManager(MainActivity.this));
//
//        } catch (JSONException e) {
//            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
//        }
//
//    }
//
//}