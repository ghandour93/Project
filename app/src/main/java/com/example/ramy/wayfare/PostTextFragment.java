package com.example.ramy.wayfare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class PostTextFragment extends DialogFragment{

    EditText et_post;
    Button btn_post;
    String token;
    String text;
    Boolean success;
    Location location;
    BroadcastReceiver broadcastReceiver;

    public PostTextFragment() {
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
        View view = inflater.inflate(R.layout.fragment_post_text, container, false);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.my_textfield);
        getDialog().getWindow().setWindowAnimations(R.style.DialogAnimation);

        btn_post = (Button) view.findViewById(R.id.btn_post);
        et_post = (EditText) view.findViewById(R.id.et_post);
        token = getArguments().getString("token");

        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = et_post.getText().toString();
                Intent intent = new Intent(getActivity(), GpsService.class);
                intent.putExtra("text", text);
                intent.putExtra("token", ((HomeActivity)getActivity()).getToken());
                getActivity().startService(intent);

//                new ServerTask(getActivity(),token){
//                    @Override
//                    protected String doInBackground(String... params) {
//                        try {
//                            if (getArguments().getBundle("bundle").containsKey("updated location"))
//                                location = getArguments().getBundle("bundle").getParcelable("updated location");
//                            else if (getArguments().getBundle("bundle").containsKey("last location"))
//                                location = getArguments().getBundle("bundle").getParcelable("last location");
//                            success=postText(text, location);
//                            if (success)
//                                publishProgress();
//
//                        } catch (Exception e) {
//
//                        }
//                        return null;
//                    }
//
//                    @Override
//                    protected void onProgressUpdate(Void... values) {
//                        getDialog().dismiss();
//                        Toast.makeText(getActivity(), "Text successfully posted", Toast.LENGTH_LONG).show();
//                        super.onProgressUpdate(values);
//                    }
//                }.execute();
            }
        });

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                        getDialog().dismiss();
                        Toast.makeText(getActivity(), "Text successfully posted", Toast.LENGTH_LONG).show();

            }
        };
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("text posted"));
        return view;
    }

    @Override
    public void onPause(){
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);
    }
}
