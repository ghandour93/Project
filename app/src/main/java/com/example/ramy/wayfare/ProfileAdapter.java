package com.example.ramy.wayfare;

/**
 * Created by Ramy on 12/7/2016.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ProfileAdapter extends FragmentPagerAdapter {

    String username;
    Bundle b;

    public ProfileAdapter(FragmentManager fm, String user) {
        super(fm);
        username = user;
        b=new Bundle();
        b.putString("username", username);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
            switch (position) {
                case 0:
                    fragment = new FeedFragment();
                    fragment.setArguments(b);
                    return fragment;
                case 1:
                    fragment = new FeedFragment();
                    fragment.setArguments(b);
                    return fragment;
                case 2:
                    fragment = new FeedFragment();
                    fragment.setArguments(b);
                    return fragment;
                default:
                    return new ProfileFragment();
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
