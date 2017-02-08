package com.example.ramy.wayfare;

/**
 * Created by ysalem1 on 12/8/2016.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.MyViewHolder> {
    private ArrayList<JSONObject> mDataset;
    boolean success;
    String userName;
    int postId;
    JSONObject likes = new JSONObject();
    MyViewHolder mvholder;
    Context ctx;
    String auth_token;
    Bundle b;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView mTextView, username, location, tv_like, tv_dislike;
        public Button btn_like, btn_dislike;
        public RelativeLayout rl;
        public ImageView iv;

        public MyViewHolder(View v) {
            super(v);
            mCardView = (CardView) v.findViewById(R.id.card_view);
            mTextView = (TextView) v.findViewById(R.id.textView3);
            iv = (ImageView) v.findViewById(R.id.pic2);
            username=(TextView) v.findViewById(R.id.tv_username);
            location = (TextView)v.findViewById(R.id.tv_location);
            tv_like = (TextView) v.findViewById(R.id.tv_like);
            tv_dislike = (TextView) v.findViewById(R.id.tv_dislike);
            btn_like = (Button) v.findViewById(R.id.btn_like);
            btn_dislike = (Button) v.findViewById(R.id.btn_dislike);
            rl = (RelativeLayout) v.findViewById(R.id.relative_lay);
            mTextView.setVisibility(View.INVISIBLE);
            iv.setVisibility(View.INVISIBLE);
            b = new Bundle();
            btn_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        userName = mDataset.get(getAdapterPosition()).getString("profile");
                        postId = mDataset.get(getAdapterPosition()).getInt("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new ServerTask(ctx,auth_token){
                        @Override
                        protected String doInBackground(String... params) {
                            try {
                                success=editLike(userName, postId);
                                if (success) {
                                    likes = getLikes(userName, postId);
                                    publishProgress();
                                }

                            } catch (Exception e) {

                            }
                            return null;
                        }

                        @Override
                        protected void onProgressUpdate(Void... values) {
                            try {
                                mDataset.get(getAdapterPosition()).put("likes_count", likes.getInt("likes_count"));
                                mDataset.get(getAdapterPosition()).put("likers", likes.getJSONArray("likers"));
                                notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            super.onProgressUpdate(values);
                        }
                    }.execute();
                }
            });

            tv_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        userName = mDataset.get(getAdapterPosition()).getString("profile");
                        postId = mDataset.get(getAdapterPosition()).getInt("id");
                        Fragment fragment1 = null;
                        Class fragmentClass = UsersListFragment.class;
                        try {
                            fragment1 = (Fragment) fragmentClass.newInstance();
                            b.putInt("id", 3);
                            b.putString("username", userName);
                            b.putInt("post_id", postId);
                            fragment1.setArguments(b);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        FragmentManager fragmentManager = ((HomeActivity)ctx).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.relative_lay, fragment1).addToBackStack(null);
                        fragmentTransaction.commit();
                        fragmentTransaction.addToBackStack(null);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public FeedAdapter(ArrayList<JSONObject> myDataset, Context context) {
        mDataset = myDataset;
        ctx = context;
    }

    @Override
    public FeedAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        mvholder = holder;
        SharedPreferences prefs = ctx.getSharedPreferences("WayFare", MODE_PRIVATE);
        auth_token = prefs.getString("auth_token", null);
        JSONObject obj;
        obj=mDataset.get(position);
        try {
            if (obj.getString("type").equals("text")) {
                holder.mTextView.setVisibility(View.VISIBLE);
                holder.mTextView.setText(obj.getString("uploaded_text"));
            }
            else if (obj.getString("type").equals("image")) {
                holder.iv.setVisibility(View.VISIBLE);
                String image = obj.getString("encoded_file");
                byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.iv.setImageBitmap(decodedByte);
            }
            holder.username.setText(obj.getString("profile"));
            holder.location.setText(obj.getString("location"));
            holder.tv_like.setText(obj.getInt("likes_count") + " Votes");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
