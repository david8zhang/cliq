package com.cliq.cliq.views;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cliq.cliq.R;
import com.cliq.cliq.api.ApiManager;
import com.cliq.cliq.controller.DataModelController;
import com.cliq.cliq.model.User;

public class SettingsActivity extends AppCompatActivity {

    private TextView name;
    private TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ApiManager apiManager = new ApiManager(this);
        apiManager.getUserInfo();

        name = (TextView)findViewById(R.id.name);
        email = (TextView)findViewById(R.id.email);

        User user = DataModelController.getUser();
        if (user != null)
        {
            name.setText(user.getName());
            email.setText(user.getEmail());
        }

        ImageButton c_email = (ImageButton) findViewById(R.id.c_email);
        ImageButton c_password = (ImageButton) findViewById(R.id.c_password);

        c_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                PopupWindow pw = new PopupWindow(
                        inflater.inflate(R.layout.popup, null, false),
                        100,
                        100,
                        true);
                pw.showAtLocation(findViewById(R.id.popup_location), Gravity.CENTER, 0, 0);
            }
        });
    }
}
