package com.cliq.cliq.controller;

import java.util.ArrayList;

/**
 * Created by david_000 on 2/27/2016.
 */
public class DataModelController {

    private static DataModelController mInstance;
    private static final Object obj = new Object();

    /** the userid. */
    public static String user_id;

    /** the unique registration token for this device. */
    public static String reg_token;

    /** The tokens of friends. */
    public static String friend_token;

    /** the ids of friends. */
    //TODO: Change this to a hash map of friend_ids??
    public static String friend_id;

    public static DataModelController getInstance() {
        synchronized(obj) {
            if(mInstance == null)
                mInstance = new DataModelController();
        }
        return mInstance;
    }

}
