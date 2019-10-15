package com.ubfx.track;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.WebView;

import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by yangchuanzhe on 2017/11/23.
 */

public class TrackManager {


    private static volatile TrackManager instance = null;

    private Context context;

    public static TrackManager shared() {
        if (instance == null) {
            synchronized (TrackManager.class) {
                if (instance == null) {
                    instance = new TrackManager();
                }
            }
        }
        return instance;
    }

    public void initialize(Application application, String server, String channel) {
        if (context == null) {
            context = application.getApplicationContext();
        }
        SensorsDataAPI.sharedInstance(application, server);

        List<SensorsDataAPI.AutoTrackEventType> eventTypeList = new ArrayList<>();
        // $AppStart
        eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_START);
        // $AppEnd
        eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_END);
        // $AppViewScreen
        eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_VIEW_SCREEN);
        // $AppClick
        eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_CLICK);
        SensorsDataAPI.sharedInstance().enableAutoTrack(eventTypeList);
        SensorsDataAPI.sharedInstance().trackFragmentAppViewScreen();
        SensorsDataAPI.sharedInstance().trackAppCrash();
        JSONObject properties = new JSONObject();
        try {
            properties.put("DownloadChannel", channel);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SensorsDataAPI.sharedInstance().trackInstallation("AppInstall", properties);
    }

    public void setupWebView(WebView webView) {
        SensorsDataAPI.sharedInstance().showUpWebView(webView, false, true);
    }

    /**
     * 获取打点sdk生成的匿名ID
     *
     * @return
     */
    public String getAnonymousId() {
        return SensorsDataAPI.sharedInstance().getAnonymousId();
    }

    public void eventRegister(Context context) {
    }

    public void identify(@NonNull String userId, @Nullable HashMap<String, Object> userInfo) {
        if (TextUtils.isEmpty(userId) || "-1".equals(userId)) {
            return;
        }
        SensorsDataAPI.sharedInstance().login(userId);
    }

    public void trackEvent(Context context, String eventName) {
        SensorsDataAPI.sharedInstance().track(eventName);
    }

    public void trackEvent(Context context, String eventName, JSONObject properties) {
        if (properties != null) {
            SensorsDataAPI.sharedInstance().track(eventName, properties);
        }
    }

    public void trackADEvent(Context context, ADParamsBuilder params) {
        trackEvent(context, StatisticsEvents.SHENCE_EVENT_AD_CLICK, params.build());
    }

    public static HashMap<String, Object> getErrorInfo(String error) {
        HashMap<String, Object> info = new HashMap<>();
        info.put("error_msg", error);
        return info;
    }

    public static HashMap<String, Object> getProductInfo(String symbol) {
        HashMap<String, Object> info = new HashMap<>();
        info.put("symbol", symbol);
        return info;
    }

    public void reportRequest(Context context, String url, long duration) {
        HashMap<String, Object> info = new HashMap<>();
        info.put("url", url);
        info.put("duration", duration);
    }


}
