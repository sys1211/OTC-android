package com.ubfx.library.common;

import android.content.Context;
import android.text.TextUtils;

import com.meituan.android.walle.ChannelInfo;
import com.meituan.android.walle.WalleChannelReader;

/**
 * Created by yangchuanzhe on 2018/11/13.
 *
 * config file in ${root}/jiagu/forex(forex_waihui)/config.json
 */
public class PackageConfig {

    private static ChannelInfo channelInfo = null;

    public static void initConfig(Context context) {
        channelInfo = WalleChannelReader.getChannelInfo(context);
    }

    public static String getChannelID() {
        if (channelInfo == null) {
            return defaultChannelID();
        }
        String channelID = channelInfo.getExtraInfo().get("CHANNEL_ID");
        return TextUtils.isEmpty(channelID) ? "13" : channelID;
    }

    public static String getChannel() {
        if (channelInfo == null) {
            return defaultChannel();
        }
        return channelInfo.getChannel();
    }

    private static String defaultChannelID() {
        return "13";
    }

    private static String defaultChannel() {
        return "official";
    }
}
