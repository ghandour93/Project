package com.example.ramy.wayfare;

import android.os.Bundle;

public class RemoteNotification {

    public static final int TYPE_STACK = -1000;

    protected Bundle mExtrasBundle;
    protected int mUserNotificationId = -1;

    protected RemoteNotification() {}

    public RemoteNotification(Bundle bundle) {
        mExtrasBundle = bundle;
        mUserNotificationId = (int)(System.currentTimeMillis() / 1000);
    }

    public Bundle getBundle() {
        if (mExtrasBundle == null) {
            mExtrasBundle = new Bundle();
        }
        return mExtrasBundle;
    }

    public String getAppName() {
        return "WayFare";
    }

    public String getErrorName() {
        return getBundle().getString("error_name");
    }

    public String getActivityText() {
        return "WayFare";
    }

    public String getMessage() {
        return getBundle().getString("message");
    }

    public String getUrl() {
        return getBundle().getString("url");
    }

    public String getUserNotificationGroup() {
        return getBundle().getString("type");
    }

    public int getUserNotificationId() {
        return mUserNotificationId;
    }
}
