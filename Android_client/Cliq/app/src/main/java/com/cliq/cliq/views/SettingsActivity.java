package com.cliq.cliq.views;

import android.app.ActionBar;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cliq.cliq.R;
import com.cliq.cliq.api.ApiManager;
import com.cliq.cliq.controller.DataModelController;
import com.cliq.cliq.model.User;

import org.w3c.dom.Text;

public class SettingsActivity extends AppCompatActivity {

    private TextView name;
    private TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final ApiManager apiManager = new ApiManager(this);
        apiManager.getUserInfo();

        name = (TextView)findViewById(R.id.name);
        email = (TextView)findViewById(R.id.email);

        User user = DataModelController.getUser();
        if (user != null)
        {
            name.setText(user.getName());
            email.setText(user.getEmail());
        }

        // popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popup = inflater.inflate(R.layout.popup, null);
        final PopupWindow pw = new PopupWindow(
                popup,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);
        final TextView cText = (TextView) popup.findViewById(R.id.changeUserInfo);
        final Button confirm = (Button) popup.findViewById(R.id.confirm);
        Button cancel = (Button) popup.findViewById(R.id.cancel);

        ImageButton c_email = (ImageButton) findViewById(R.id.c_email);
        ImageButton c_password = (ImageButton) findViewById(R.id.c_password);

        c_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.showAtLocation(findViewById(R.id.popup_location), Gravity.CENTER, 0, 0);

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        email.setText(cText.getText());
                        pw.dismiss();
                    }
                });
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });
    }
}
