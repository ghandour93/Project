package com.example.ramy.wayfare;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.facebook.drawee.backends.pipeline.Fresco;

public class HomeActivity extends AppCompatActivity {

    String auth_token;
    Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Fresco.initialize(this);
        b=new Bundle();
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        new AsyncTask<String, Void, Intent>() {
            @Override
            protected Intent doInBackground(String... params) {
                try {
                    auth_token = ServerTask.getAuthToken(Login.mAccountManager, Login.accountName);
                    Fragment fragment = null;
                    Class fragmentClass = FeedFragment.class;
                    try {
                        fragment = (Fragment) fragmentClass.newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.relative_lay, fragment).addToBackStack(null);
                    fragmentTransaction.commit();
                    fragmentTransaction.addToBackStack(null);
                } catch (Exception e) {
                }
                return null;
            }
        }.execute();
        while(true){
            if (auth_token != null){
                Intent i = new Intent(this, RegistrationService.class);
                i.putExtra("auth_token", auth_token);
                startService(i);
                break;
            }
        }

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
        Fragment fragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.action_favorites:

                fragmentClass = FeedFragment.class;
                break;
            case R.id.action_schedules:
                fragmentClass = ProfileFragment.class;
                break;
            case R.id.action_music:
                fragmentClass = NotificationsFragment.class;
                break;
            default:
                fragmentClass = FeedFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
            fragment.setArguments(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.relative_lay, fragment).addToBackStack(null);
        fragmentTransaction.commit();
        fragmentTransaction.addToBackStack(null);
        menuItem.setChecked(true);
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() == 1) {
            moveTaskToBack(false);
        }
        else {
            super.onBackPressed();
        }
    }

    public String getToken(){
        return auth_token;
    }

}
