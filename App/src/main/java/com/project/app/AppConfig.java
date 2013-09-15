package com.project.app;

import android.util.Log;

public class AppConfig {

    public static String DATABASE_NAME = "Atreides";
    public static int DATABASE_VERSION = 1;

    public static String LOG_TAG = "Atreides";

    // 0 pro, 1 dev
    public static int DEBUG_MODE = 1;

    public static void log(String content){
        log(LOG_TAG, content);
    }

    public static void log(String tag, String content){
        if(DEBUG_MODE == 1)
            Log.i(tag, content);
    }

}
