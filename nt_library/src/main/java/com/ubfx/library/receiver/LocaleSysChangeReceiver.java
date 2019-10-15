package com.ubfx.library.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ubfx.library.language.LangManager;
import com.ubfx.log.LogUtils;;

/**
 * Created by yangchuanzhe on 2017/9/14.
 */

public class LocaleSysChangeReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_LOCALE_CHANGED)) {
            LogUtils.d("system locale changed");
            LangManager.get().updateLocale(context);
        }
    }
}
