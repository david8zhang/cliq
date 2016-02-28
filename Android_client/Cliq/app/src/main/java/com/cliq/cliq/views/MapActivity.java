package com.cliq.cliq.views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.cliq.cliq.R;
import com.cliq.cliq.api.ApiManager;
import com.cliq.cliq.controller.DataModelController;
import com.cliq.cliq.model.Constants;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.messaging.Message;
import com.sinch.android.rtc.messaging.MessageClient;
import com.sinch.android.rtc.messaging.MessageClientListener;
import com.sinch.android.rtc.messaging.MessageDeliveryInfo;
import com.sinch.android.rtc.messaging.MessageFailureInfo;
import com.sinch.android.rtc.messaging.WritableMessage;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by David on 2/27/16.
 */
public class MapActivity extends FragmentActivity
        implements OnMapReadyCallback {

    LocationManager mLocationManager;
    Marker marker;

    Marker friend;

    SinchClient sinchClient;
    MessageClient messageClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ApiManager apiManager = new ApiManager(this);

        /** Send a response to the friend. */
        //TODO: Only works for one friend
        apiManager.sendLocResponse(DataModelController.reg_token, DataModelController.friend_token);

        final String user_id = PreferenceManager.getDefaultSharedPreferences(this).getString("user_id", null);
        startSinch(user_id);


        /** Get the Google Map. */
        final MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        /** set the location manager. */
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double lat = location.getLatitude();
                double lng = location.getLongitude();

                /** Send location to friends. */
                updateFriends(messageClient, lat, lng);

                /** Update my position. */
                marker.setPosition(new LatLng(lat, lng));
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                System.out.println("Status has changed");
            }

            @Override
            public void onProviderEnabled(String s) {
                System.out.println("Provider has been enabled");
            }

            @Override
            public void onProviderDisabled(String s) {
                System.out.println("Provider has been disabled");
            }
        };

        Criteria crit = new Criteria();
        crit.setAccuracy(Criteria.ACCURACY_FINE);
        crit.setPowerRequirement(Criteria.POWER_MEDIUM);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(3000L, 0.5f, crit, listener, Looper.myLooper());
    }

    @Override
    public void onMapReady(GoogleMap map) {
        marker = map.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Me")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        friend = map.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Friend")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
    }

    /** Deal with Sinch messaging API.*/
    public void startSinch(String user_id) {
        // Instantiate a SinchClient using the SinchClientBuilder.
        Context context = this.getApplicationContext();
        sinchClient = Sinch.getSinchClientBuilder().context(context)
                .applicationKey(Constants.SINCH_API_KEY)
                .applicationSecret(Constants.SINCH_API_SECRET)
                .environmentHost("sandbox.sinch.com")
                .userId(user_id)
                .build();

        sinchClient.setSupportMessaging(true);
        sinchClient.setSupportActiveConnectionInBackground(true);
        sinchClient.startListeningOnActiveConnection();

        sinchClient.addSinchClientListener(new SinchClientListener() {
            @Override
            public void onClientStarted(SinchClient sinchClient) {
                System.out.println("SINCH: sinch client started");
            }

            @Override
            public void onClientStopped(SinchClient sinchClient) {
                System.out.println("SINCH: sinch client stopped");

            }

            @Override
            public void onClientFailed(SinchClient sinchClient, SinchError sinchError) {
                System.out.println("SINCH: sinch client failed");
            }

            @Override
            public void onRegistrationCredentialsRequired(SinchClient sinchClient, ClientRegistration clientRegistration) {
                System.out.println("SINCH: registration required");

            }

            @Override
            public void onLogMessage(int i, String s, String s1) {
                System.out.println(s1);
            }
        });
        sinchClient.start();
        /** Deal with messages. */
        messageClient = sinchClient.getMessageClient();
        MessageClientListener sinch_listener = new MessageClientListener() {

            @Override
            public void onIncomingMessage(MessageClient messageClient, Message message) {
                String msg = message.getTextBody();
                double lat = Double.parseDouble(msg.substring(0, msg.indexOf('&')));
                double lng = Double.parseDouble(msg.substring(msg.indexOf('&') + 1));
                System.out.println("lat: " + lat);
                System.out.println("lng: " + lng);
                rerenderFriends(lat, lng);
            }

            @Override
            public void onMessageSent(MessageClient messageClient, Message message, String s) {
                System.out.println("Sent!");
            }

            @Override
            public void onMessageFailed(MessageClient messageClient, Message message, MessageFailureInfo messageFailureInfo) {
                System.out.println("Failed");
            }

            @Override
            public void onMessageDelivered(MessageClient messageClient, MessageDeliveryInfo messageDeliveryInfo) {
                System.out.println("Delivered!");
            }

            @Override
            public void onShouldSendPushData(MessageClient messageClient, Message message, List<PushPair> list) {
                System.out.println("Pushed!");
            }
        };
        messageClient.addMessageClientListener(sinch_listener);
    }

    /** Send Sinch Messages to all friends. */
    public void updateFriends(MessageClient messageClient, double lat, double lng) {
        if(messageClient != null) {
            String location = lat + "&" + lng;
            System.out.println(location);
            WritableMessage message = new WritableMessage(DataModelController.friend_id, location);
            messageClient.send(message);
        }
    }

    /** Rerender the locations of all friends. */
    public void rerenderFriends(double lat, double lng) {
        friend.setPosition(new LatLng(lat, lng));
    }

    /** Terminate the Sinch Client if press back. */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            sinchClient.stopListeningOnActiveConnection();
            sinchClient.terminate();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
