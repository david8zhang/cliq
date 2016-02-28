package com.cliq.cliq.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.cliq.cliq.R;
import com.cliq.cliq.api.ApiManager;
import com.cliq.cliq.controller.DataModelController;
import com.cliq.cliq.controller.service.QuickstartPreferences;
import com.cliq.cliq.controller.service.RegistrationIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Created by david_000 on 2/27/2016.
 */
public class HomeActivity extends AppCompatActivity{

    private BroadcastReceiver broadcastReceiver;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ApiManager apiManager = new ApiManager(this);

        ImageButton find_button = (ImageButton)findViewById(R.id.findbutton);
        find_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent finder = new Intent(HomeActivity.this, FinderActivity.class);
                HomeActivity.this.startActivity(finder);
            }
        });

        Button map_button = (Button)findViewById(R.id.mapButton);
        map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent map = new Intent(HomeActivity.this, MapActivity.class);
                HomeActivity.this.startActivity(map);
            }
        });


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences.getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if(sentToken) {
                    System.out.println("Sent token");
                } else {
                    System.out.println("error");
                }
            }
        };

        if(checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

        if(DataModelController.reg_token != null) {
            apiManager.setRegToken(DataModelController.reg_token);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
