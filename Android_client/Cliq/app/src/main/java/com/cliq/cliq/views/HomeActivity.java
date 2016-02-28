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
public class HomeActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageButton find_button = (ImageButton)findViewById(R.id.findbutton);
        find_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent finder = new Intent(HomeActivity.this, FinderActivity.class);
                HomeActivity.this.startActivity(finder);
            }
        });
    }
}
