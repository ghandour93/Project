package com.example.ramy.wayfare;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class UsersAdapter extends ArrayAdapter<JSONObject>{
    List<JSONObject> objs;
    boolean success;
    String username;
    View v;
    Context ctx;

    public UsersAdapter(Context context, int resource, List<JSONObject> objects) {
        super(context, resource, objects);
        objs=objects;
        ctx = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.user_item, null);
        }
        final TextView tv_username = (TextView) v.findViewById(R.id.tv_username);
        final Button btn_follow = (Button) v.findViewById(R.id.btn_follow);
        SimpleDraweeView avatar = (SimpleDraweeView) v.findViewById(R.id.avatar);

        if(objs.isEmpty() || position == objs.size()-1){
            v.findViewById(R.id.relative_lay).setVisibility(View.GONE);
            return v;
        }

        JSONObject obj = getItem(position);

        if (obj != null ) {
                try {
                    v.findViewById(R.id.relative_lay).setVisibility(View.VISIBLE);
                    tv_username.setText(obj.getString("user"));
                    int num = Integer.parseInt(obj.getString("type_rel"));
                    switch (num){
                        case 0:
                            btn_follow.setVisibility(View.GONE);
                            break;
                        case 1:
                            btn_follow.setText("Following");
                            break;
                        default:
                            btn_follow.setVisibility(View.VISIBLE);
                            btn_follow.setText("Follow");
                            break;
                    }
                    ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithResourceId(R.drawable.ic_launcher).build();
                    avatar.setImageURI(imageRequest.getSourceUri());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }

        btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username=tv_username.getText().toString();
                try {
                    new ServerTask(ctx,objs.get(objs.size()-1).getString("token")){
                        @Override
                        protected String doInBackground(String... params) {
                            try {

                                success=editFollow(username);
                                if (success)
                                    publishProgress();

                            } catch (Exception e) {

                            }
                            return null;
                        }

                        @Override
                        protected void onProgressUpdate(Void... values) {
                            if (v!=null)
                            if (btn_follow.getText().toString().equals("Follow")){
                                btn_follow.setText("Following");
                            }else if (btn_follow.getText().toString().equals("Following")){
                                btn_follow.setText("Follow");
                            }
                            super.onProgressUpdate(values);
                        }
                    }.execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return v;
    }
    }

