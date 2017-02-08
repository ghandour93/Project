package com.example.ramy.wayfare;

import android.content.Context;
import android.content.SharedPreferences;
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

import static android.content.Context.MODE_PRIVATE;

public class UsersAdapter extends ArrayAdapter<JSONObject>{
    TextView tv_username;
    Button btn_follow;
    SimpleDraweeView avatar;

    List<JSONObject> objs;
    JSONObject obj;
    String rel;
    String username;
    String auth_token;
    View v;
    Context ctx;

    public UsersAdapter(Context context, int resource, List<JSONObject> objects) {
        super(context, resource, objects);
        objs=objects;
        ctx = context;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.user_item, null);
        }
        tv_username = (TextView) v.findViewById(R.id.tv_username);
        btn_follow = (Button) v.findViewById(R.id.btn_follow);
        avatar = (SimpleDraweeView) v.findViewById(R.id.avatar);

        if(objs.isEmpty() || position == objs.size()-1){
            v.findViewById(R.id.relative_lay).setVisibility(View.GONE);
            return v;
        }

        obj = getItem(position);

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
                try {
                    SharedPreferences prefs = ctx.getSharedPreferences("WayFare", MODE_PRIVATE);
                    auth_token = prefs.getString("auth_token", null);
                    username=getItem(position).getString("user");
                    new ServerTask(ctx,auth_token){
                        @Override
                        protected String doInBackground(String... params) {
                            try {

                                rel=editFollow(username);
                                publishProgress();

                            } catch (Exception e) {

                            }
                            return null;
                        }

                        @Override
                        protected void onProgressUpdate(Void... values) {
                            try {
                                getItem(position).put("type_rel", rel);
                                notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
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

    @Override
    public int getViewTypeCount() {

        return 1;
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    }

