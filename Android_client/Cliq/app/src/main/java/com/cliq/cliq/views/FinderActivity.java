package com.cliq.cliq.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.cliq.cliq.R;
import com.cliq.cliq.api.ApiManager;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by david_000 on 2/27/2016.
 */
public class FinderActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriend);

        final ApiManager apiManager = new ApiManager(this);

        ImageButton findFriend = (ImageButton)findViewById(R.id.find_friend);
        findFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText searchBar = (EditText)findViewById(R.id.search_bar);
                String friend_user_query = searchBar.getText().toString();
                apiManager.findFriend(friend_user_query, FinderActivity.this);
                //TODO: Make a list of suggested friends.
            }
        });
    }
}
