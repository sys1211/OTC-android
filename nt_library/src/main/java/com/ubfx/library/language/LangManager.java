package com.ubfx.library.language;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.LocaleList;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.ubfx.log.LogUtils;;

import java.net.URLDecoder;
import java.util.Locale;

/**
 * Created by chuanzheyang on 2017/8/18.
 * <p>
 * 多语言文件 zh_CN en zh_HK zh_TW
 * 服务器接受的语言 zh-CN en zh-TW
 */

public class LangManager {

    private static final String SELECTED_LANGUAGE = "lang";
    private static final String CONFIG_LANG = "config_lang";

    public static final String LANG_LOCAL_CN = "zh_CN";
    public static final String LANG_LOCAL_TW = "zh_TW";
    public static final String LANG_LOCAL_EN = "en";

    private static final String LANG_SERVER_ZH_CN = "zh-CN";
    private static final String LANG_SERVER_ZH_TW = "zh-TW";
    private static final String LANG_SERVER_EN = "en";

    private String currentLang = LANG_LOCAL_CN;

    private static volatile LangManager instance = null;

    private ILocaleUpdateInterceptor interceptor;

    private Locale systemLocale;

    public void setInterceptor(ILocaleUpdateInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    private LangManager() {
    }

    public static LangManager get() {
        if (instance == null) {
            synchronized (LangManager.class) {
                if (instance == null) {
                    instance = new LangManager();
                }
            }
        }
        return instance;
    }

    @Nullable
    public Locale getSystemLocale() {
        return systemLocale;
    }

    public void updateLocale(Context context) {
        Resources res = context.getApplicationContext().getResources();
        Configuration configuration = res.getConfiguration();


        // get system language
        Locale locale = res.getConfiguration().locale;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = res.getConfiguration().getLocales().get(0);
        }

        if (systemLocale == null) {
            systemLocale = locale;
        }

        if (interceptor != null) {
            Locale interceptorLocale = interceptor.processLocal(locale);
            if (interceptorLocale != null) {
                locale = interceptorLocale;
            }
        }

        String langSys = locale.toString();
        LogUtils.d("system lang : " + langSys);


        SharedPreferences sp = context.getSharedPreferences(CONFIG_LANG, Context.MODE_PRIVATE);
        String langLocal = sp.getString(SELECTED_LANGUAGE, "");
        LogUtils.d("storage lang : " + langLocal);

        if (TextUtils.isEmpty(langLocal)) {
            langLocal = langSys;
        }

        // some device zh_CN recognition
        if (langLocal.equals("zh_TW")) {
            this.currentLang = LANG_LOCAL_TW;
            locale = Locale.TRADITIONAL_CHINESE;
        } else if (langLocal.contains("zh")) {
            this.currentLang = LANG_LOCAL_CN;
            locale = Locale.CHINESE;
        } else {
            this.currentLang = LANG_LOCAL_EN;
            locale = Locale.ENGLISH;
        }

        configuration.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocales(new LocaleList(locale));
        }

        res.updateConfiguration(configuration, res.getDisplayMetrics());
    }


    public Locale getCurrentLocale() {
        String lang = getCurrentLang();
        if (lang.equals("zh_TW")) {
            this.currentLang = LANG_LOCAL_TW;
            return Locale.TRADITIONAL_CHINESE;
        } else if (lang.contains("zh")) {
            this.currentLang = LANG_LOCAL_CN;
            return Locale.CHINESE;
        } else {
            this.currentLang = LANG_LOCAL_EN;
            return Locale.ENGLISH;
        }
    }

    public String getServerLang() {
        if (currentLang.equals(LANG_LOCAL_CN)) {
            return LANG_SERVER_ZH_CN;
        } else if (currentLang.equals(LANG_LOCAL_TW)) {
            return LANG_SERVER_ZH_TW;
        } else if (currentLang.equals(LANG_LOCAL_EN)) {
            return LANG_LOCAL_EN;
        }
        // default English
        return LANG_SERVER_EN;
    }

    public String getCurrentLang() {
        return this.currentLang;
    }


    public void setCurrentLang(Context context, String lang) {
        this.currentLang = lang;
        SharedPreferences sp = context.getSharedPreferences(CONFIG_LANG, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(SELECTED_LANGUAGE, lang);
        // Do not use apply.Shared preference must commit language changing immediately.
        // must have result , otherwise 'booster transform' will replace commit to apply.
        boolean result = edit.commit();
        LogUtils.i("change language save to sp result " + result);
        updateLocale(context);
    }


    public boolean currentIsChina() {
        return getCurrentLang().equals(LANG_LOCAL_CN) || getCurrentLang().equals(LANG_LOCAL_TW);
    }

    // scheme:[//[user:password@]host[:port]][/]path[?query][#fragment]
    public static String urlAppendLang(String url) {
        Uri uri = Uri.parse(url);
        if (uri == null) {
            url = URLDecoder.decode(url);
        }
        uri = Uri.parse(url);
        if (uri != null) {
            if (url.contains("#")) {
                String[] urlSplitArray = url.split("#");
                if (urlSplitArray.length > 0) {
                    String urltemp = urlSplitArray[0];
                    String urlNew = LangManager.urlAppendLang(urltemp);
                    return url.replace(urltemp, urlNew);
                }

            }
            if (!url.contains("lang=")) {
                if (url.contains("?") || url.contains("&")) {
                    url += ("&lang=" + LangManager.get().getServerLang());
                } else {
                    url += ("?lang=" + LangManager.get().getServerLang());
                }
            }
        }

        return url;
    }
}
