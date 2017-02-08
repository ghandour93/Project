package com.example.ramy.wayfare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.Manifest;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import static android.provider.UserDictionary.Words.APP_ID;

public class HomeActivity extends AppCompatActivity implements FeedFragment.OnFeedFragmentSelected,
        ProfileFragment.OnProfileFragmentSelected, NotificationsFragment.OnNotificationsFragmentSelected{

    String auth_token;
    Bundle bndl;
    PopupWindow popupWindow;
    boolean isFABOpen;
    FloatingActionButton fab_main;
    FloatingActionButton fab_btn1;
    FloatingActionButton fab_btn2;
    FloatingActionButton fab_btn3;
    PostTextFragment dialogFragment;
    private static final int PERMISSION_ACCESS_FINE_LOCATION = 3;
    boolean gps_enabled;
    boolean mine;
    BroadcastReceiver broadcastReceiver;
    BroadcastReceiver broadcastReceiverstate;
    int numFABs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Fresco.initialize(this);
        bndl = new Bundle();
        isLocationEnabled();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        fab_main = (FloatingActionButton) findViewById(R.id.fab_main);
        fab_btn1 = (FloatingActionButton) findViewById(R.id.fab_btn1);
        fab_btn2 = (FloatingActionButton) findViewById(R.id.fab_btn2);
        fab_btn3 = (FloatingActionButton) findViewById(R.id.fab_btn3);

        new AsyncTask<String, Void, Intent>() {
            @Override
            protected Intent doInBackground(String... params) {
                try {
                    auth_token = ServerTask.getAuthToken(Login.mAccountManager, Login.accountName);
                    SharedPreferences prefs = getSharedPreferences("WayFare", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("auth_token", auth_token);
                    editor.apply();
                    Fragment fragment = null;
                    Class fragmentClass;
                    Bundle b = new Bundle();
                    if(getIntent().getStringExtra("profile") != null){
                        fragmentClass = ProfileFragment.class;
                        fragment = (Fragment) fragmentClass.newInstance();
                        b.putString("userprofile", getIntent().getStringExtra("profile"));
                        b.putBoolean("notmine", true);
                        fragment.setArguments(b);
                    }else {
                        fragmentClass = FeedFragment.class;
                        try {
                            fragment = (Fragment) fragmentClass.newInstance();
                            if (getIntent().getStringExtra("post_id") != null) {
                                Log.d("jj", "bh");
                                b.putInt("post_id", Integer.parseInt(getIntent().getStringExtra("post_id")));
                                fragment.setArguments(b);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
        while (true) {
            if (auth_token != null) {
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
                        if (isFABOpen)
                            closeFABMenu();
                        selectDrawerItem(item);
                        return true;
                    }
                });
        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    closeFABMenu();
                }
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_ACCESS_FINE_LOCATION);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                } else {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void showFABMenu(){
        isFABOpen=true;
        float dim = -getResources().getDimension(R.dimen.standard_55);
        if (numFABs == 2){
            fab_btn1.animate().translationY((float)(dim*Math.sin(Math.toRadians(15))));
            fab_btn1.animate().translationX((float)(dim*Math.cos(Math.toRadians(15))));
            fab_btn2.animate().translationY((float)(dim*Math.cos(Math.toRadians(15))));
            fab_btn2.animate().translationX((float)(dim*Math.sin(Math.toRadians(15))));
        }else if (numFABs == 3){
            fab_btn1.animate().translationX(dim);
            fab_btn2.animate().translationY((float)(dim*Math.cos(Math.toRadians(45))));
            fab_btn2.animate().translationX((float)(dim*Math.sin(Math.toRadians(45))));
            fab_btn3.animate().translationY(dim);
        }
    }

    private void closeFABMenu(){
        isFABOpen=false;
        fab_btn1.animate().translationY(0);
        fab_btn1.animate().translationX(0);
        fab_btn2.animate().translationY(0);
        fab_btn2.animate().translationX(0);
        fab_btn3.animate().translationY(0);
        fab_btn3.animate().translationX(0);
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
            fragment.setArguments(bndl);
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
    protected void onResume(){
        super.onResume();
        if (broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Log.d("broadcast","broadcast");
                }
            };
        }
        if (broadcastReceiverstate == null){
            broadcastReceiverstate = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
                        Log.d("a7a yalla","a7a");
                    }
                }
            };
        }
    }

    @Override
    public void onBackPressed() {
        if(dialogFragment!=null && dialogFragment.isVisible()){
            dialogFragment.dismiss();
            return;
        }
        if (isFABOpen){
            closeFABMenu();
        }
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

    public void isLocationEnabled(){
        LocationManager lm = (LocationManager)getSystemService(this.LOCATION_SERVICE);
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch (Exception ex){}
        if(!gps_enabled){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                    "use this app");
            dialog.setPositiveButton("Open Location Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                }
            });
            dialog.show();
        }
    }

    @Override
    public void onFeedFragmentDisplayed() {
        numFABs = 2;
        fab_main.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_launcher));
        fab_btn1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_posttext));
        fab_btn2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_postimage));
        fab_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putString("token", getToken());
                b.putBundle("bundle", bndl);
                dialogFragment = new PostTextFragment();
                dialogFragment.setArguments(b);
                dialogFragment.show(getSupportFragmentManager(), "");
                closeFABMenu();
            }
        });

        fab_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, PostImageActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onProfileFragmentDisplayed(boolean mine, int rel) {
        numFABs = 3;
        if (!mine){
            Log.d("rel", String.valueOf(rel));
        }else {
            fab_main.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_launcher));
            fab_btn3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_edit));
            fab_btn3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment = null;
                    Class fragmentClass = EditProfileFragment.class;
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
                }
            });
        }
    }

    @Override
    public void onNotificationsFragmentDisplayed() {
        numFABs = 1;
        fab_main.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_delete));
    }

    private boolean isGooglePlayServicesAvailable() {

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {

            if (googleApiAvailability.isUserResolvableError(resultCode)) {

                googleApiAvailability.getErrorDialog(this, resultCode, 10)

                        .show();

            } else {

                Toast.makeText(HomeActivity.this, "Unsupported Device", Toast.LENGTH_SHORT).show();

                finish();

            }

            return false;

        }

        return true;

    }
}
