package com.cliq.cliq.controller.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.cliq.cliq.R;
import com.cliq.cliq.views.HomeActivity;
import com.google.android.gms.gcm.GcmListenerService;



/**
 * Created by david_000 on 1/26/2016.
 */
public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        System.out.println("Message:" + data);
        sendNotification();
    }

    /** Show a toast, change this to a notification later. */
    private void sendNotification() {
        Intent requestIntent = new Intent(this, HomeActivity.class);
        requestIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, requestIntent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.friend1)
                .setContentTitle("cliq")
                .setContentText("New message")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
