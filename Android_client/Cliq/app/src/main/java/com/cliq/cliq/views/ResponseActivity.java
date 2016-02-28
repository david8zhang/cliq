package com.cliq.cliq.views;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.cliq.cliq.R;
import com.google.android.gms.appdatasearch.GetRecentContextCall;

/**
 * Created by david_000 on 2/28/2016.
 */
public class ResponseActivity extends AppCompatActivity {

    LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        ImageButton accept = (ImageButton)findViewById(R.id.acceptbutton);
        ImageButton decline = (ImageButton)findViewById(R.id.declinebutton);

        //TODO: Add a message telling users to turn on location services on their device.

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(ResponseActivity.this, MapActivity.class);
                ResponseActivity.this.startActivity(mapIntent);
            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(ResponseActivity.this, HomeActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ResponseActivity.this.startActivity(mainIntent);
            }
        });

    }
}
