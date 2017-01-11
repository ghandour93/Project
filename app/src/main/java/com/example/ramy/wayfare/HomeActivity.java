package com.example.ramy.wayfare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import static android.provider.UserDictionary.Words.APP_ID;
import static com.example.ramy.wayfare.R.id.textView;

public class HomeActivity extends AppCompatActivity{

    String auth_token;
    Bundle bndl;
    PopupWindow popupWindow;
    boolean isFABOpen;
    FloatingActionButton fab_post;
    FloatingActionButton fab_text;
    FloatingActionButton fab_image;
    PostTextFragment dialogFragment;
    private static final int PERMISSION_ACCESS_FINE_LOCATION = 3;
    boolean gps_enabled;
    boolean network_enabled;
    BroadcastReceiver broadcastReceiver;
    BroadcastReceiver broadcastReceiverstate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Fresco.initialize(this);
        bndl = new Bundle();
        isLocationEnabled();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        fab_post = (FloatingActionButton) findViewById(R.id.fab_post);
        fab_text = (FloatingActionButton) findViewById(R.id.fab_text);
        fab_image = (FloatingActionButton) findViewById(R.id.fab_image);

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
        fab_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    closeFABMenu();
                }
            }
        });
        fab_text.setOnClickListener(new View.OnClickListener() {
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

        fab_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, PostImageActivity.class);
                startActivity(intent);
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
        fab_text.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fab_image.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
    }

    private void closeFABMenu(){
        isFABOpen=false;
        fab_text.animate().translationY(0);
        fab_image.animate().translationY(0);
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
//        Intent intent = new Intent(HomeActivity.this, GpsService.class);
//        startService(intent);
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

//        registerReceiver(broadcastReceiver, new IntentFilter("location update"));
//        registerReceiver(broadcastReceiverstate, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
    }

//    @Override
////    protected void onPause(){
////        super.onPause();
////        Intent intent = new Intent(HomeActivity.this, GpsService.class);
////        stopService(intent);
////        unregisterReceiver(broadcastReceiver);
////        unregisterReceiver(broadcastReceiverstate);
//    }

    @Override
    public void onBackPressed() {
        if(dialogFragment!=null && dialogFragment.isVisible()){
            dialogFragment.dismiss();
            return;
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
}
