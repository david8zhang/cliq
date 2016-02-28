package com.cliq.cliq.api;

import android.content.Context;
import android.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cliq.cliq.controller.AppController;
import com.cliq.cliq.controller.DataModelController;
import com.cliq.cliq.model.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


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
                        String user_id = PreferenceManager.getDefaultSharedPreferences(context).getString("user_id", null);

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

    /** Send a request for the friend's location. */
    public void findFriend(final String friend_username, Context contex) {
        String user_id = PreferenceManager.getDefaultSharedPreferences(context).getString("userid", null);
        if(user_id == null) {
            System.out.println("No bueno");
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("friend_username", friend_username);
            params.put("user_id", user_id);
            final GetRequest request = new GetRequest(null, params, Constants.FIND_FRIEND, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    System.out.println(jsonObject.toString());
                    try {
                        final JSONArray feedArray = jsonObject.getJSONArray("Items");
                        final JSONObject feedObj = (JSONObject)feedArray.get(0);
                        if(feedArray.length() <= 0 || feedObj.getString("reg_token") == null) {
                            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setContentText("Could not find friend!")
                                    .show();
                        } else {
                            new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Friend found!")
                                    .setContentText("Send a request?")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            try {
                                                String friend_token = feedObj.getString("reg_token");
                                                sendLocRequest(friend_token);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    })
                                    .show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    System.out.println(volleyError);
                }
            });
            AppController.getInstance().addToRequestQueue(request);
        }

    }

    /** Set the registration token. */
    public void setRegToken(final String reg_token) {
        final String user_id = PreferenceManager.getDefaultSharedPreferences(context).getString("user_id", null);
        if(user_id == null) {
            System.out.println("user id cannot be null!");
        }
        if(reg_token == null) {
            System.out.println("reg_token cannot be null!");
        }
        HashMap<String, String> params = new HashMap<>();
        StringRequest request = new StringRequest(Request.Method.POST, Constants.SEND_REG, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                System.out.println(s);
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
                params.put("user_id", user_id);
                params.put("reg_token", reg_token);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request);
    }

    /** Send a location/friend request. */
    public void sendLocRequest(String friend_token) {
        String text = "New friend request!";

        //TODO: Put in actual username here
        String username = "username";

        HashMap<String, String> params = new HashMap<>();
        params.put("text", text);
        params.put("username", username);
        params.put("reg_token", friend_token);

        StringRequest request = new StringRequest(Request.Method.POST, Constants.SEND_GCM, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                System.out.println(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println(volleyError);
            }
        });

        AppController.getInstance().addToRequestQueue(request);
    }
}

