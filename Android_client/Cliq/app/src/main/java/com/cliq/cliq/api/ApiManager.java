package com.cliq.cliq.api;

import android.content.Context;
import android.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cliq.cliq.controller.AppController;
import com.cliq.cliq.model.Constants;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by david_000 on 2/27/2016.
 */
public class ApiManager {

    private Context context;

    public ApiManager(Context context) {

        this.context = context;
    }

    /** Authenticate the user. */
    public void authenticate(final String username, final String password) {
        final StringRequest request = new StringRequest(Request.Method.POST, Constants.USER_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("user_id", s).commit();
                        PreferenceManager.getDefaultSharedPreferences(context).getString("user_id", null);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println(volleyError);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(request);

    }

    /** Register a new user. */
    public void register(final String email, final String username, final String password) {
        final StringRequest request = new StringRequest(Request.Method.POST, Constants.USER_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("user_id", s).commit();
                        PreferenceManager.getDefaultSharedPreferences(context).getString("user_id", null);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println(volleyError);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("username", username);
                params.put("password", password);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(request);
    }
}

