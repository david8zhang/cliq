package com.cliq.cliq.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.cliq.cliq.R;

/**
 * Created by david_000 on 2/27/2016.
 */
public class SplashActivity extends AppCompatActivity{

    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String user_id = mPrefs.getString("user_id", null);
        System.out.println(user_id);
        Thread welcomeThread = new Thread() {
            @Override
            public void run() {
                try {
                    super.run();
                    sleep(3000);
                } catch (Exception e) {
                    System.out.println(e);
                } finally {
                    if(user_id == null) {
                        Intent onboard = new Intent(SplashActivity.this, OnBoardActivity.class);
                        SplashActivity.this.startActivity(onboard);
                        finish();
                    } else {
                        Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                        SplashActivity.this.startActivity(i);
                        finish();
                    }
                }
            }
        };
        welcomeThread.start();
    }

}
