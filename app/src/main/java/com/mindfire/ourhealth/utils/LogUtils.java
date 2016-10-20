package com.mindfire.ourhealth.utils;

import android.util.Log;

import com.mindfire.ourhealth.BuildConfig;

/**
 * This is a utility class that helps in displaying toast messages and in writing
 * the log entries (if application is in DEBUG mode).
 */
public class LogUtils {
    /**
     * Makes a log entry under verbose column if app is in DEBUG mode.
     */
    public static void verbose(String tag, String message) {
        if (BuildConfig.DEBUG)
            Log.v(tag, message);
    }

    /**
     * Makes a log entry under debug column if app is in DEBUG mode.
     */
    public static void debug(String tag, String message) {
        if (BuildConfig.DEBUG)
            Log.d(tag, message);
    }

    /**
     * Makes a log entry under information column if app is in DEBUG mode.
     */
    public static void info(String tag, String message) {
        if (BuildConfig.DEBUG)
            Log.i(tag, message);
    }

    /**
     * Makes a log entry under warning column if app is in DEBUG mode.
     */
    public static void warn(String tag, String message) {
        if (BuildConfig.DEBUG)
            Log.w(tag, message);
    }

    /**
     * Makes a log entry under error column if app is in DEBUG mode.
     */
    public static void error(String tag, String message) {
        if (BuildConfig.DEBUG)
            Log.e(tag, message);
    }

    /**
     * Print the stack trace of exception if app is in DEBUG mode.
     */
    public static void printStack(Exception exception) {
        if (BuildConfig.DEBUG)
            exception.printStackTrace();
    }
}
