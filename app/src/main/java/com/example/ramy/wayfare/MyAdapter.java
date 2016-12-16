package com.example.ramy.wayfare;

/**
 * Created by ysalem1 on 12/8/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<JSONObject> mDataset;
    private Context context;
    private int lastPosition;
    private View view;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
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
//            v.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    view.getContext().startActivity(new Intent(view.getContext(),TripInfo.class));
//                }
//            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<JSONObject> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v);
        view = v;
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

            JSONObject obj = new JSONObject();
        obj=mDataset.get(position);
        try {

            if (obj.getString("type").equals("text")) {
                holder.mTextView.setVisibility(View.VISIBLE);
            }
            else if (obj.getString("type").equals("image")) {
                Log.i("feed","image");
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

//        holder.iv.setBackgroundResource(R.mipmap.ic_launcher);

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
