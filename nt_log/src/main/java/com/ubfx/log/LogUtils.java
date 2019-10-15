package com.ubfx.log;

import android.util.Log;


public class LogUtils {

    private static final String TAG = BuildConfig.APPLICATION_ID;

    private LogUtils() {
    }

    public static void d(String message) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, buildMessage(message));
        }
    }

    public static void d(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, buildMessage(message));
        }
    }

    public static void d(String tag, String message, Object... args) {
        if (BuildConfig.DEBUG) {
            if (args.length > 0) {
                message = String.format(message, args);
            }
            Log.d(tag, buildMessage(message));
        }
    }

    public static void v(String message) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, buildMessage(message));
        }
    }

    public static void v(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, buildMessage(message));
        }
    }

    public static void v(String tag, String message, Object... args) {
        if (BuildConfig.DEBUG) {
            if (args.length > 0) {
                message = String.format(message, args);
            }
            Log.v(tag, buildMessage(message));
        }
    }

    public static void i(String message) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, buildMessage(message));
        }
    }

    public static void i(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, buildMessage(message));
        }
    }

    public static void i(String tag, String message, Object... args) {
        if (BuildConfig.DEBUG) {
            if (args.length > 0) {
                message = String.format(message, args);
            }
            Log.i(tag, buildMessage(message));
        }
    }

    public static void e(String message) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, buildMessage(message));
        }
    }

    public static void e(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, buildMessage(message));
        }
    }


    private static String buildMessage(String rawMessage) {
        StackTraceElement caller = new Throwable().getStackTrace()[2];
        String fullClassName = caller.getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        return className + "." + caller.getMethodName() + "(): " + rawMessage;
    }
}