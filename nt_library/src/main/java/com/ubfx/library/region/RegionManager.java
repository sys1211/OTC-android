package com.ubfx.library.region;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.ubfx.library.language.LangManager;
import com.ubfx.library.model.RegionModel;
import com.ubfx.log.LogUtils;
import com.ubfx.network.request.UBSuccessListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by chuanzheyang on 2017/8/15.
 */

public class RegionManager {

    private static final String SELECTED_COUNTRY = "country";
    private static final String CONFIG_COUNTRY = "config_country";

    private static volatile RegionManager instance = null;

    private RegionDataProvider provider;

    private RegionManager() {
        regions = new ArrayList<>();
    }

    public static RegionManager get() {
        if (instance == null) {
            synchronized (RegionManager.class) {
                if (instance == null) {
                    instance = new RegionManager();
                }
            }
        }
        return instance;
    }

    private List<RegionModel> regions;
    private List<String> blackList;

    public void configProvider(RegionDataProvider provider) {
        this.provider = provider;
    }


    public void getAllRegions(@Nullable RegionsCallback callback) {
        getAllRegions(false, callback);
    }

    private void removeBlackAndCallback(RegionsCallback callback) {
        List<RegionModel> resultRegions = new ArrayList<>(regions);

        Iterator<RegionModel> it = resultRegions.iterator();
        while (it.hasNext()) {
            RegionModel model = it.next();
            if (blackList.contains(model.nameShort)) {
                it.remove();
            }
        }
        callback.callback(resultRegions);
    }

    public void getAllRegions(boolean removeBlack, @Nullable RegionsCallback callback) {
        if (provider != null) {
            if (regions.isEmpty()) {
                regions = provider.loadRegions();
            }
            if (!removeBlack) {
                if (callback != null) {
                    callback.callback(regions);
                }
            } else {
                if (callback != null) {
                    if (blackList != null) {
                        removeBlackAndCallback(callback);
                    } else {
                        provider.loadBlackList(new UBSuccessListener<List<String>>() {
                            @Override
                            public void onResponse(List<String> blackList) {
                                RegionManager.this.blackList = blackList;
                                removeBlackAndCallback(callback);
                            }
                        });
                    }

                }
            }
        } else {
            if (callback != null) {
                callback.callback(new ArrayList<>());
            }
        }
    }

    public void getRegion(String shortName, RegionCallback callback) {
        getRegion(false, shortName, callback);

    }

    public void getRegion(boolean removeBlack, String shortName, RegionCallback callback) {
        if (TextUtils.isEmpty(shortName)) {
            if (callback != null) {
                callback.callback(null);
            }
            return;
        }
        getAllRegions(removeBlack, new RegionsCallback() {
            @Override
            public void callback(List<RegionModel> regionModels) {
                RegionModel regionModel = null;
                for (RegionModel region :
                        regionModels) {
                    if (shortName.equals(region.nameShort)) {
                        regionModel = region;
                        break;
                    }
                }
                if (callback != null) {
                    callback.callback(regionModel);
                }
            }
        });
    }

    public static String getChinaShort() {
        return "CN";
    }


    public static String getDefaultRegion(Context context) {
        String regionCode = RegionManager.get().getCurrentRegion(context);
        if (!TextUtils.isEmpty(regionCode)) {
            return regionCode;
        }

        String serLang = LangManager.get().getCurrentLang();
        if (serLang.equals(LangManager.LANG_LOCAL_CN)) {
            return getChinaShort();
        } else if (serLang.equals(LangManager.LANG_LOCAL_EN)) {
            // return New Zealand in English Language
            return "NZ";
        } else if (serLang.equals(LangManager.LANG_LOCAL_TW)) {
            return "TW";
        }
        return getChinaShort();
    }

    /**
     * @param context
     * @return region code
     */
    @Nullable
    public String getCurrentRegion(Context context) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG_COUNTRY, Context.MODE_PRIVATE);
        return sp.getString(SELECTED_COUNTRY, null);
    }


    public void setCurrentRegion(Context context, String region) {
        if (TextUtils.isEmpty(region)) {
            return;
        }
        if (region.equals(getCurrentRegion(context))) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(CONFIG_COUNTRY, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(SELECTED_COUNTRY, region);
        // Do not use apply.Shared preference must commit language changing immediately.
        // must have result , otherwise 'booster transform' will replace commit to apply.
        boolean result = edit.commit();
        LogUtils.i("change country save to sp result " + result);
        if (result) {
            EventBus.getDefault().post(new EventRegionChanged());
        }
    }

    public void reload() {
        regions.clear();
        getAllRegions(null);
    }

}
