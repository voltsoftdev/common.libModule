package com.dev.voltsoft.lib.utility;

import android.util.Log;

public class EasyLog {

    private static final String LOG_TAG = "com.dev.voltsoft.lib";

    public static void LogMessage(Object o, String... message)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        stringBuilder.append(o.getClass().getSimpleName());
        stringBuilder.append("]");

        for (String s : message)
        {
            stringBuilder.append(s);
        }

        Log.d(LOG_TAG , stringBuilder.toString());
    }

    public static void LogMessage(String... message) {

        StringBuilder stringBuilder = new StringBuilder();

        for (String s : message)
        {
            stringBuilder.append(s);
        }

        Log.d(LOG_TAG , stringBuilder.toString());
    }

    public static void ErrorMessage(Exception e , String... messages) {

        StringBuilder errorMessage = new StringBuilder();

        errorMessage.append(e.getMessage());

        for (String s : messages)
        {
            errorMessage.append(s);
        }

        Log.e(LOG_TAG , errorMessage.toString());
    }
}
