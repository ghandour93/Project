package com.example.ramy.wayfare;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

public class RegistrationService extends IntentService {
    public RegistrationService() {
        super("RegistrationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        InstanceID myID = InstanceID.getInstance(this);
        try {
            String registrationToken = myID.getToken(
                    getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE,
                    null
            );
            SharedPreferences prefs = getSharedPreferences("WayFare", MODE_PRIVATE);
            String reg_id = prefs.getString("reg_id", null);
            if((reg_id != null && !reg_id.equals(registrationToken)) || reg_id == null) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("reg_id", registrationToken);
                editor.apply();
                Log.d("Added to prefs", "YES");
                editor.clear();
                editor.apply();
                ServerTask s = new ServerTask(this, intent.getStringExtra("auth_token"));
                s.addRegId(registrationToken);
            }
            Log.d("Registration Token", registrationToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
