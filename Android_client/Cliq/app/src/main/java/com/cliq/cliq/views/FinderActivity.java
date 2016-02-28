package com.cliq.cliq.views;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.cliq.cliq.R;
import com.cliq.cliq.api.ApiManager;
import com.cliq.cliq.controller.DataModelController;

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
        //apiManager.getUserInfo();

        ImageButton findFriend = (ImageButton)findViewById(R.id.find_friend);
        findFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText searchBar = (EditText)findViewById(R.id.search_bar);
                String friend_user_query = searchBar.getText().toString();
                System.out.println(friend_user_query);
                System.out.println(PreferenceManager.getDefaultSharedPreferences(FinderActivity.this).getString("user_id", null));
                apiManager.findFriend(friend_user_query);
                //TODO: Make a list of suggested friends.
            }
        });
        if(!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("registered", false)) {
            apiManager.setRegToken(DataModelController.reg_token);
        }
    }
}
