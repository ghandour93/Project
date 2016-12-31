package com.example.ramy.wayfare;

/**
 * Created by Ramy on 12/7/2016.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ProfileAdapter extends FragmentPagerAdapter {

    public ProfileAdapter(FragmentManager fm) {
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
                    return new Feed_Fragment();
                case 1:
                    return new Feed_Fragment();
                case 2:
                    return new Feed_Fragment();
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
