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
            String reg_id_new = myID.getToken(
                    getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE,
                    null
            );
            SharedPreferences prefs = getSharedPreferences("WayFare", MODE_PRIVATE);
            String reg_id_old = prefs.getString("reg_id", null);
            if((reg_id_old != null && !reg_id_old.equals(reg_id_new)) || reg_id_old == null) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("reg_id", reg_id_new);
                editor.apply();
                Log.d("Added to prefs", "YES");
                ServerTask s = new ServerTask(this, intent.getStringExtra("auth_token"));
                s.addRegId(reg_id_new, reg_id_old);
            }
            Log.d("Registration Token", reg_id_new);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
