package com.example.ramy.wayfare;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;

public class EditProfileFragment extends Fragment {

    View v;
    boolean success;
    EditText et_name;
    EditText et_username;
    EditText et_bio;
    String name;
    String username;
    String bio;

    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        Button btn = (Button) v.findViewById(R.id.button2);
        et_name = (EditText) v.findViewById(R.id.et_name);
        et_username = (EditText) v.findViewById(R.id.et_username);
        et_bio = (EditText) v.findViewById(R.id.et_bio);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ServerTask(getActivity(),((HomeActivity)getActivity()).getToken()){

                    @Override
                    protected void onPreExecute(){
                        name = et_name.getText().toString();
                        username = et_username.getText().toString();
                        bio = et_bio.getText().toString();
                    }

                    @Override
                    protected String doInBackground(String... params) {
                        try {
                            success = editProfile(name, username, bio);
                            publishProgress();

                        } catch (Exception e) {

                        }
                        return null;
                    }

                    @Override
                    protected void onProgressUpdate(Void... values) {
                        if (success)
                        super.onProgressUpdate(values);
                    }
                }.execute();

            }
        });
        return v;
    }
}
