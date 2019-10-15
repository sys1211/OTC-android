package com.ubfx.theme;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.util.TypedValue;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by yangchuanzhe on 2019/8/21.
 */
public class UBResourceHelper {


    private static Context appContext;

    /**
     * 设置上下文环境
     *
     * @param context 上下文环境
     */
    public static void setContext(Context context) {
        appContext = context.getApplicationContext();
    }

    /**
     * 获取上下文环境
     *
     * @return Context
     */
    private static Context getContext() {
        return appContext;
    }

    /**
     * 检查上下文环境
     */
    private static boolean isCurrentContextValid() {
        if (getContext() == null) throw new NullPointerException(" the context could't be null. ");
        return getContext() != null;
    }

    /**
     * 获得当前上下文环境下对应的主题Theme
     *
     * @return Theme
     */
    public static Resources.Theme getCurrentContextTheme() {
        if (!isCurrentContextValid()) return null;
        return getContext().getTheme();
    }

    /**
     * # 根据attr属性id获得typedValue对象
     *
     * @param attrId 资源属性对应的id
     * @return TypedValue
     */
    @Nullable
    private static TypedValue obtainTypedValue(Context context, int attrId) {
        Resources.Theme theme = context.getTheme();
        if (theme == null) return null;
        TypedValue typedValue = new TypedValue();
        try {
            theme.resolveAttribute(attrId, typedValue, true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return typedValue;
        }
    }

    /**
     * 根据所给定的arrtId获得Identifier资源id
     *
     * @param attrId
     */
    public static int getResIDByAttrID(Context context, int attrId) {
        TypedValue typedValue = obtainTypedValue(context, attrId);
        return typedValue == null ? 0 : typedValue.resourceId;
    }

    public static boolean isNight(Context context) {
        SharedPreferences sp = context.getSharedPreferences("theme_config", MODE_PRIVATE);
        return sp.getBoolean("config_day_night", false);
    }

    public static boolean setNight(Context context, boolean isNight) {
        SharedPreferences sp = context.getSharedPreferences("theme_config", MODE_PRIVATE);
        boolean result = sp.edit().putBoolean("config_day_night", isNight).commit();
        if (result) {
            ThemeMgr.get().sendBroadcast(context);
        }
        return result;
    }


}
