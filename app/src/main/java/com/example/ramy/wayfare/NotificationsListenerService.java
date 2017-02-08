package com.example.ramy.wayfare;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.android.gms.gcm.GcmListenerService;
import java.util.ArrayList;


public class NotificationsListenerService extends GcmListenerService {

    protected NotificationManager mNotificationManager;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        Log.d("message","yes");
            sendNotification(data);
    }

    private void sendNotification(Bundle data) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (data.getString("type").equals("like"))
            intent.putExtra("post_id", data.getString("post_id"));
        else
            intent.putExtra("profile", data.getString("profile"));

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("WayFare")
                .setContentText("bae")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify((int)(System.currentTimeMillis() / 1000), notificationBuilder.build());
    }

    public void sendNotification(RemoteNotification remoteNotification) {
        // handle building and sending a normal notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        // perform other configuration ...
        builder.setContentTitle(remoteNotification.getActivityText())
                .setContentText(remoteNotification.getMessage())
                .setSmallIcon(R.drawable.ic_launcher);
        // set the group, this is important for later
        builder.setGroup(remoteNotification.getUserNotificationGroup());
        Notification builtNotification = builder.build();

        // deliver the standard notification
        getNotificationManagerService().notify(remoteNotification.getUserNotificationGroup(), remoteNotification.getUserNotificationId(), builtNotification);

        // pass our remote notification through to deliver a stack notification
//        sendStackNotificationIfNeeded(remoteNotification);
    }

    protected NotificationManager getNotificationManagerService() {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }

    private void sendStackNotificationIfNeeded(RemoteNotification remoteNotification) {
        Log.d("not","stack");
        if (Build.VERSION.SDK_INT >= 23) {
            ArrayList<StatusBarNotification> groupedNotifications = new ArrayList<>();
            Log.d("not",String.valueOf(getNotificationManagerService().getActiveNotifications().length));

            for (StatusBarNotification sbn : getNotificationManagerService().getActiveNotifications()) {
                if (remoteNotification.getUserNotificationGroup() != null &&
                        remoteNotification.getUserNotificationGroup().equals(sbn.getNotification().getGroup())) {
//                    &&
//                    sbn.getId() != RemoteNotification.TYPE_STACK
                    groupedNotifications.add(sbn);
                    Log.d("not",String.valueOf(groupedNotifications.size()));
                    Log.d("not",sbn.getNotification().getGroup());
                }
            }

            if (groupedNotifications.size() > 1) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                Log.d("not",String.valueOf(groupedNotifications.size()));

                builder.setContentTitle(String.format("%s", remoteNotification.getAppName()))
                        .setContentText(String.format("%d new notifications", groupedNotifications.size()))
                        .setSmallIcon(R.drawable.ic_launcher);

                // for every previously sent notification that met our above requirements,
                // add a new line containing its title to the inbox style notification extender
                NotificationCompat.InboxStyle inbox = new NotificationCompat.InboxStyle();
                {
                    for (StatusBarNotification activeSbn : groupedNotifications) {
                        String stackNotificationLine = (String)activeSbn.getNotification().extras.get(NotificationCompat.EXTRA_TITLE);
                        if (stackNotificationLine != null) {
                            inbox.addLine(stackNotificationLine);
                        }
                    }

                    // the summary text will appear at the bottom of the expanded stack notification
                    // we just display the same thing from above (don't forget to use string
                    // resource formats!)
                    inbox.setSummaryText(String.format("%d new activities", groupedNotifications.size()));
                }
                builder.setStyle(inbox);

                // make sure that our group is set the same as our most recent RemoteNotification
                // and choose to make it the group summary.
                // when this option is set to true, all previously sent/active notifications
                // in the same group will be hidden in favor of the notifcation we are creating
                builder.setGroup(remoteNotification.getUserNotificationGroup())
                        .setGroupSummary(true);

                // if the user taps the notification, it should disappear after firing its content intent
                // and we set the priority to high to avoid Doze from delaying our notifications
                builder.setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

                // create a unique PendingIntent using an integer request code.
                final int requestCode = (int)System.currentTimeMillis() / 1000;
//                builder.setContentIntent(PendingIntent.getActivity(this, requestCode, DetailActivity.createIntent(this), PendingIntent.FLAG_ONE_SHOT));

                Notification stackNotification = builder.build();
                stackNotification.defaults = Notification.DEFAULT_ALL;

                // finally, deliver the notification using the group identifier as the Tag
                // and the TYPE_STACK which will cause any previously sent stack notifications
                // for this group to be updated with the contents of this built summary notification
                getNotificationManagerService().notify(remoteNotification.getUserNotificationGroup(), RemoteNotification.TYPE_STACK, stackNotification);
            }
        }
    }
}
