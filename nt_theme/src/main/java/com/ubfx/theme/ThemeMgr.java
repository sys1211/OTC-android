package com.ubfx.theme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by yangchuanzhe on 2019/8/19.
 */
public class ThemeMgr {
    public static final String ACTION_THEME_CHANGED = "ACTION_UB_THEME_CHANGED";
    private static final ThemeMgr ourInstance = new ThemeMgr();

    public static ThemeMgr get() {
        return ourInstance;
    }

    private ThemeProvider themeProvider;

    private ThemeMgr() {
    }


    public void setThemeProvider(ThemeProvider themeProvider) {
        this.themeProvider = themeProvider;
    }

    public ThemeProvider getThemeProvider() {
        return themeProvider;
    }

    public void registerReceiver(Context context, BroadcastReceiver receiver) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_THEME_CHANGED);
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, intentFilter);
    }

    public void unregisterReceiver(Context context, BroadcastReceiver receiver) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
    }


    void sendBroadcast(Context context) {
        Intent intent = new Intent();
        intent.setAction(ACTION_THEME_CHANGED);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
