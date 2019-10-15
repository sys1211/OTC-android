package com.ubfx.library.utils;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by yangchuanzhe on 2018/9/4.
 */

public class DeviceUtils {

    public static void hideKeyboard(View v) {
        if (v != null) {
            IBinder token = v.getWindowToken();
            if (token != null) {
                InputMethodManager im = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (im != null) {
                    im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }

    }

    public static void hideKeyboard(Activity activity) {
        View focusView = activity.getCurrentFocus();
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(focusView == null ? null : focusView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
