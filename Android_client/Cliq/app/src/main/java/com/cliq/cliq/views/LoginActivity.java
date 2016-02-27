package com.cliq.cliq.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.cliq.cliq.R;
import com.cliq.cliq.api.ApiManager;


/**
 * Created by david_000 on 2/27/2016.
 */
public class LoginActivity extends AppCompatActivity {

    /** Auth info of the user. */
    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        final ApiManager apiManager = new ApiManager(this);

        /** Map the authenticate button. */
        ImageButton auth = (ImageButton)findViewById(R.id.login);

        auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText userview = (EditText) findViewById(R.id.username);
                EditText passview = (EditText) findViewById(R.id.password);

                if(userview.getText() == null || passview.getText() == null) {
                    System.out.println("No bueno");
                } else {
                    username = userview.getText().toString();
                    password = passview.getText().toString();
                    apiManager.authenticate(username, password);
                }

                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });

    }
}
