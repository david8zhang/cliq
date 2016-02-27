package com.cliq.cliq.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.cliq.cliq.R;
import com.cliq.cliq.api.ApiManager;

/**
 * Created by david_000 on 2/27/2016.
 */
public class RegisterActivity extends AppCompatActivity {
    /** Auth info of the user. */
    String username;
    String password;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final ApiManager apiManager = new ApiManager(this);

        /** Map the authenticate button. */
        ImageButton reg = (ImageButton)findViewById(R.id.register);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText userview = (EditText) findViewById(R.id.username);
                EditText passview = (EditText) findViewById(R.id.password);
                EditText emailview = (EditText) findViewById(R.id.email);
                if (userview.getText() == null || passview.getText() == null || emailview == null) {
                    System.out.println("No bueno");
                } else {
                    username = userview.getText().toString();
                    password = passview.getText().toString();
                    email = emailview.getText().toString();
                    apiManager.register(email, username, password);
                }

                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                RegisterActivity.this.startActivity(intent);
            }
        });

    }
}
