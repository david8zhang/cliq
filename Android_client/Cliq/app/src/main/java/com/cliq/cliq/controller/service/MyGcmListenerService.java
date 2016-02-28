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
import com.cliq.cliq.views.ResponseActivity;
import com.google.android.gms.gcm.GcmListenerService;



/**
 * Created by david_000 on 1/26/2016.
 */
public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        System.out.println("Message:" + data);
        String text = "Swipe to decline, tap to accept";
        String user_id = data.getString("username");
        sendNotification(text, user_id);
    }

    /** Show a toast, change this to a notification later. */
    private void sendNotification(String text, String username) {
        Intent requestIntent = new Intent(this, ResponseActivity.class);
        requestIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, requestIntent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.friend2)
                .setContentTitle("Location Request!")
                .setContentText(text)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
