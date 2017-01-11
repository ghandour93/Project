package com.example.ramy.wayfare;

import android.*;
import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class GpsService extends IntentService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    GoogleApiClient googleApiClient;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000;
    Location location;
    Intent intentExtra;
    String text;
    boolean success;
    Intent broadcast;
    boolean gps_enabled;

    public GpsService() {
        super("GpsService");
    }

    @Override
    public void onCreate(){
        super.onCreate();
        googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        intentExtra = intent;
        googleApiClient.connect();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && isLocationEnabled()) {
            mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(UPDATE_INTERVAL)
                    .setFastestInterval(FASTEST_INTERVAL);
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, this);
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation != null) {
                location = lastLocation;
            }
            text = intentExtra.getStringExtra("text");
            new ServerTask(this,intentExtra.getStringExtra("token")){
                @Override
                protected String doInBackground(String... params) {
                    try {
                        success=postText(text, location);
                        if (success) {
                            broadcast = new Intent("text posted");
                            sendBroadcast(broadcast);
                        }

                    } catch (Exception e) {

                    }
                    return null;
                }
            }.execute();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed." + connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public void onDestroy(){
        if (googleApiClient != null && googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
        super.onDestroy();
    }

    public boolean isLocationEnabled(){
        LocationManager lm = (LocationManager)getSystemService(this.LOCATION_SERVICE);
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch (Exception ex){}
        if(!gps_enabled)
            return false;
        return true;
    }
}
