package com.example.ramy.wayfare;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

//        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
//        PagerAdapter pagerAdapter =
//                new PagerAdapter(getSupportFragmentManager(), MainActivity.this);
//        viewPager.setAdapter(pagerAdapter);
        Fragment fragment1 = null;
        Class fragmentClass = fragmentOne.class;
        try {
             fragment1 = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame, fragment1).commit();

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        selectDrawerItem(item);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.action_favorites:

                fragmentClass = fragmentOne.class;
                break;
            case R.id.action_schedules:
                fragmentClass = Profile1.class;
                break;
            case R.id.action_music:
                fragmentClass = fragmentOne.class;
                break;
            default:
                fragmentClass = fragmentOne.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
    }

//    class PagerAdapter extends FragmentPagerAdapter {
//
////        String tabTitles[] = new String[]{"Tab One", "Tab Two", "Tab Three"};
//        Context context;
//
//        public PagerAdapter(FragmentManager fm, Context context) {
//            super(fm);
//            this.context = context;
//        }
//
//        @Override
//        public int getCount() {
////            return tabTitles.length;
//            return 3;
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//
//            switch (position) {
//                case 0:
//                    return new fragmentOne();
//                case 1:
//                    return new fragmentOne();
//                case 2:
//                    return new fragmentOne();
//            }
//
//            return null;        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            // Generate title based on item position
////            return tabTitles[position];
//            return null;
//        }
//
//    }
}
