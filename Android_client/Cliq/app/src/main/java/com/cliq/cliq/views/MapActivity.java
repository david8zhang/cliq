package com.cliq.cliq.views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.cliq.cliq.R;
import com.cliq.cliq.api.ApiManager;
import com.cliq.cliq.controller.DataModelController;
import com.cliq.cliq.model.Constants;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
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

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by David on 2/27/16.
 */
public class MapActivity extends FragmentActivity
        implements OnMapReadyCallback {

    LocationManager mLocationManager;
    Marker marker;

    SinchClient sinchClient;
    MessageClient messageClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ApiManager apiManager = new ApiManager(this);

        /** Send a response to the friend. */
        //TODO: Only works for one friend
        apiManager.sendLocResponse(DataModelController.reg_token, DataModelController.friend_tokens.get(0));

        final String user_id = PreferenceManager.getDefaultSharedPreferences(this).getString("user_id", null);
        if(user_id == null) {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Error!")
                    .setConfirmText("Did you sign out?")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            Intent intent = new Intent(MapActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            MapActivity.this.startActivity(intent);
                        }
                    }).show();
        } else {
            startSinch(user_id);
        }



        /** Get the Google Map. */
        final MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        /** set the location manager. */
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                System.out.println("Latitude: " + location.getLatitude());
                System.out.println("Longitude: " + location.getLongitude());
                double lat = location.getLatitude();
                double lng = location.getLongitude();

                /** Send location to friends. */
                updateFriends(messageClient, user_id, lat, lng);

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
        mLocationManager.requestLocationUpdates(2000L, 0.5f, crit, listener, Looper.myLooper());
    }

    @Override
    public void onMapReady(GoogleMap map) {
        marker = map.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Me"));
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

        sinchClient.setSupportActiveConnectionInBackground(true);
        sinchClient.setSupportMessaging(true);

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

        /** Deal with messages. */
        messageClient = sinchClient.getMessageClient();
        MessageClientListener listener = new MessageClientListener() {

            @Override
            public void onIncomingMessage(MessageClient messageClient, Message message) {
                System.out.println(message.getTextBody());
            }

            @Override
            public void onMessageSent(MessageClient messageClient, Message message, String s) {
                System.out.println(s);
            }

            @Override
            public void onMessageFailed(MessageClient messageClient, Message message, MessageFailureInfo messageFailureInfo) {
                System.out.println(messageFailureInfo.toString());
            }

            @Override
            public void onMessageDelivered(MessageClient messageClient, MessageDeliveryInfo messageDeliveryInfo) {
                System.out.println(messageDeliveryInfo);
            }

            @Override
            public void onShouldSendPushData(MessageClient messageClient, Message message, List<PushPair> list) {
                System.out.println(message);
            }
        };
        messageClient.addMessageClientListener(listener);
    }

    /** Send Sinch Messages to all friends. */
    public void updateFriends(MessageClient messageClient, String my_id, double lat, double lng) {
        String location = my_id + "&" + lat + "&" + lng;
        for(String friend_id: DataModelController.friend_ids){
            WritableMessage message = new WritableMessage(friend_id, location);
            messageClient.send(message);
        }
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
