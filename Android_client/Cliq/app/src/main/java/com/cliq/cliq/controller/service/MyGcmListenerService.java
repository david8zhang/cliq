package com.cliq.cliq.controller.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.cliq.cliq.R;
import com.cliq.cliq.controller.DataModelController;
import com.cliq.cliq.views.ResponseActivity;
import com.google.android.gms.gcm.GcmListenerService;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by david_000 on 1/26/2016.
 */
public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        System.out.println("Message:" + data);
        String text = "Tap to accept, Swipe to Decline";
        String title = null;
        String type = data.getString("type");
        String user_id = data.getString("username");
        String friend_token = data.getString("text");
        /** Add the friend_id and friend_token to the current list. */
        DataModelController.friend_tokens.add(friend_token);
        DataModelController.friend_ids.add(user_id);

        if(type.equals("request")) {
            title = "Location Request!";
        } else if (type.equals("response")) {
            title = "Location Response!";
        }
        String my_id = PreferenceManager.getDefaultSharedPreferences(this).getString("user_id", null);
        if(my_id != null) {
            sendNotification(title, text, user_id);
        }
    }

    /** Show a toast, change this to a notification later. */
    private void sendNotification(String title, String text, String username) {
        Intent requestIntent = new Intent(this, ResponseActivity.class);
        requestIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, requestIntent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.friend2)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
