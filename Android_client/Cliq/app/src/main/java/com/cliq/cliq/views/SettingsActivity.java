package com.cliq.cliq.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cliq.cliq.R;
import com.cliq.cliq.api.ApiManager;
import com.cliq.cliq.controller.DataModelController;

/**
 * Created by david_000 on 2/28/2016.
 */
public class SettingsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
}
