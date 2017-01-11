package com.example.ramy.wayfare;

/**
 * Created by ysalem1 on 12/8/2016.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.MyViewHolder> {
    private ArrayList<JSONObject> mDataset;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView mTextView, username, location;
        public ImageView iv;

        public MyViewHolder(View v) {
            super(v);
            mCardView = (CardView) v.findViewById(R.id.card_view);
            mTextView = (TextView) v.findViewById(R.id.textView3);
            iv = (ImageView) v.findViewById(R.id.pic2);
            username=(TextView) v.findViewById(R.id.tv_username);
            location = (TextView)v.findViewById(R.id.tv_location);
            mTextView.setVisibility(View.GONE);
            iv.setVisibility(View.GONE);
        }
    }

    public FeedAdapter(ArrayList<JSONObject> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public FeedAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
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
