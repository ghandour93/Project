package com.example.ramy.wayfare;

/**
 * Created by Ramy on 12/7/2016.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CustomPagerAdapter extends FragmentPagerAdapter {

    public CustomPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new fragmentOne();
                case 1:
                    return new Trial();
                case 2:
                    return new Trial();
                default:
                    return new Profile1();
            }
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch(position)
        {
            case 0: return "Feed";
            case 1: return "Ramy";
            case 2: return "Yahia";
            default : return "Feed";
        }
    }
}

//    @Override
//    public Object instantiateItem(ViewGroup collection, int position) {
//        ModelObject modelObject = ModelObject.values()[position];
//        LayoutInflater inflater = LayoutInflater.from(mContext);
//        ViewGroup layout = (ViewGroup) inflater.inflate(modelObject.getLayoutResId(), collection, false);
//        collection.addView(layout);
//        return layout;
//    }
//
//    @Override
//    public void destroyItem(ViewGroup collection, int position, Object view) {
//        collection.removeView((View) view);
//    }
//
//    @Override
//    public int getCount() {
//        return ModelObject.values().length;
//    }
//
//    @Override
//    public boolean isViewFromObject(View view, Object object) {
//        return view == object;
//    }
//
//    @Override
//    public CharSequence getPageTitle(int position) {
//        ModelObject customPagerEnum = ModelObject.values()[position];
//        return mContext.getString(customPagerEnum.getTitleResId());
//    }
