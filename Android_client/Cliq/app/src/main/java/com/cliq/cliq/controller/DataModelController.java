package com.cliq.cliq.controller;

/**
 * Created by david_000 on 2/27/2016.
 */
public class DataModelController {

    private static DataModelController mInstance;
    private static final Object obj = new Object();

    /** the userid. */
    public static String user_id;

    public static DataModelController getInstance() {
        synchronized(obj) {
            if(mInstance == null)
                mInstance = new DataModelController();
        }
        return mInstance;
    }

}
