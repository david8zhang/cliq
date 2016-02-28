package com.cliq.cliq.views;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cliq.cliq.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by David on 2/27/16.
 */
public class MapActivity extends FragmentActivity
        implements OnMapReadyCallback {

    LocationManager mLocationManager;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

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
        mLocationManager.requestLocationUpdates(5000L, 0.01f, crit, listener, Looper.myLooper());
    }

    @Override
    public void onMapReady(GoogleMap map) {
       marker = map.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Me"));
    }
}
