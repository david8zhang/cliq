package com.cliq.cliq.controller;

import com.cliq.cliq.model.User;

/**
 * Created by david_000 on 2/27/2016.
 */
public class DataModelController {

    private static DataModelController mInstance;
    private static final Object obj = new Object();
    private static User user;

    /** the userid. */
    public static String user_id;

    /** the unique registration token for this device. */
    public static String reg_token;

    public static DataModelController getInstance() {
        synchronized(obj) {
            if(mInstance == null)
                mInstance = new DataModelController();
        }
        return mInstance;
    }

    public static void setToken(String token) {
        reg_token = token;
    }

    public static void setUser(User u) { user = u; }
    public static User getUser() { return user; }
}
