package com.cliq.cliq.api;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.cliq.cliq.controller.AppController;
import com.cliq.cliq.controller.DataModelController;
import com.cliq.cliq.model.Constants;
import com.cliq.cliq.views.HomeActivity;
import com.sinch.android.rtc.SinchClient;

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
                        setRegToken(DataModelController.reg_token);
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
                        setRegToken(DataModelController.reg_token);
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
    public void findFriend(final String friend_username) {
        final String user_id = PreferenceManager.getDefaultSharedPreferences(context).getString("user_id", null);
        if(user_id == null) {
            System.out.println("No bueno");
        } else {
            HashMap<String, String> params = new HashMap<>();

            //Debugging
            System.out.println("Friend username: " + friend_username);
            System.out.println("user_id " + user_id);
            String url = Constants.FIND_FRIEND + "?user_id=" + user_id + "&friend_username=" + friend_username;
            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    System.out.println(jsonObject.toString());
                    try {
                        final JSONArray feedArray = jsonObject.getJSONArray("Items");

                        if(feedArray.length() <= 0) {
                            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setContentText("Could not find friend!")
                                    .show();
                        } else {
                            final JSONObject feedObj = (JSONObject)feedArray.get(0);
                            if(feedObj.getString("reg_token") == null) {
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
                                                    final String friend_token = feedObj.getString("reg_token");
                                                    DataModelController.friend_token = friend_token;
                                                    sendLocRequest(DataModelController.reg_token, friend_token);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        })
                                        .show();
                            }
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
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("friend_username", friend_username);
                    params.put("user_id", user_id);
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

    /** Set the registration token. */
    public void setRegToken(final String reg_token) {
        final String user_id = PreferenceManager.getDefaultSharedPreferences(context).getString("user_id", null);
        if(user_id == null) {
            System.out.println("user id cannot be null!");
        }
        if(reg_token == null) {
            System.out.println("reg_token cannot be null!");
        }
        StringRequest request = new StringRequest(Request.Method.POST, Constants.SEND_REG, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                System.out.println(s);
                PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("registered", true).commit();

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
    public void sendLocRequest(final String my_token, final String friend_token) {
        if(my_token == null) {
            System.out.println("no Bueno my token");
            return;
        }
        if(friend_token == null) {
            System.out.println("no Bueno friend_token");
            return;
        }
        System.out.println("Sent a location request!");
        final String type = "request";
        final String username = PreferenceManager.getDefaultSharedPreferences(context).getString("user_id", null);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.SEND_GCM, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                System.out.println(s);
                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Success!")
                        .setContentText("location request sent!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                Intent intent = new Intent(context, HomeActivity.class);
                                context.startActivity(intent);
                            }
                        }).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println(volleyError);
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!")
                        .setContentText("location request Failed!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                Intent intent = new Intent(context, HomeActivity.class);
                                context.startActivity(intent);
                            }
                        }).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("text", my_token);
                params.put("username", username);
                params.put("type", type);
                params.put("reg_token", friend_token);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    /** Send a handshake response to the location request. */
    public void sendLocResponse(final String my_token, final String friend_token) {
        System.out.println("Sent a location response!");
        final String type = "response";
        final String username = PreferenceManager.getDefaultSharedPreferences(context).getString("user_id", null);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.SEND_GCM, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Success!")
                        .setContentText("location response sent!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                            }
                        }).show();
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
                params.put("text", my_token);
                params.put("username", username);
                params.put("type", type);
                params.put("reg_token", friend_token);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request);
    }
}

