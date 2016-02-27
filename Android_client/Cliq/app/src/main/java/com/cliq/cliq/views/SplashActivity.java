package com.cliq.cliq.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by david_000 on 2/27/2016.
 */
public class SplashActivity extends AppCompatActivity{

    SharedPreferences mPrefs;

    protected void OnCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String user_id = mPrefs.getString("user_id", null);
        Thread homeThread = new Thread() {
            @Override
            public void run() {
                try {
                    super.run();
                    sleep(3000);
                } catch (Exception e) {
                    System.out.println(e);
                } finally {
                    Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        Thread onboardThread = new Thread() {
            @Override
            public void run() {
                try {
                    super.run();
                    sleep(3000);
                } catch (Exception e) {
                    System.out.println(e);
                } finally {
                    Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        if(user_id == null) {
            onboardThread.start();
        } else {
            homeThread.start();
        }
    }

}
