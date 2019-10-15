package com.ubfx.library;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;

import com.ubfx.library.loading.Style;
import com.ubfx.log.LogUtils;
import com.ubfx.theme.ThemeMgr;;

/**
 * Created by yangchuanzhe on 2019/2/21.
 */
public class UBFXLibraryConfig {

    @DrawableRes
    private static int titleBackIcon = R.drawable.lib_icon_back;

    private static Style loadingStyle = Style.ROTATING_RING;

    @ColorInt
    public static int getMainColor() {
        int mainColor = ThemeMgr.get().getThemeProvider().main();
        if (mainColor == 0) {
            LogUtils.e("App Main Color Not Config , Library will use Color.GRAY as main color !!!");
            mainColor = Color.GRAY;
        }
        return mainColor;
    }

    @ColorInt
    public static int getMainTextColor() {
        int mainTextColor = ThemeMgr.get().getThemeProvider().fontMain();
        if (mainTextColor == 0) {
            LogUtils.e("App Main Text Color Not Config , Library will use Color.BLACK as main Text color !!!");
            mainTextColor = Color.BLACK;
        }
        return mainTextColor;
    }

    @ColorInt
    public static int getMainTextHighlightColor() {
        int mainTextHighlightColor = ThemeMgr.get().getThemeProvider().mainHighlight();
        if (mainTextHighlightColor == 0) {
            LogUtils.e("App Main Text Highlight Color Not Config , Library will use Color.GRAY as main Text Highlight color !!!");
            mainTextHighlightColor = Color.GRAY;
        }
        return mainTextHighlightColor;
    }


    public static void init(Context context) {
    }

    @DrawableRes
    public static int getTitleBackIcon() {
        return UBFXLibraryConfig.titleBackIcon;
    }

    public static void setTitleBackRes(@DrawableRes int backIcon) {
        UBFXLibraryConfig.titleBackIcon = backIcon;
    }

    /**
     * title bar tint color
     * @return
     */
    @ColorInt
    public static int getTitleBackColor() {
        return ThemeMgr.get().getThemeProvider().fontMain();
    }

    public static Style getLoadingStyle() {
        return loadingStyle;
    }

    public static void setLoadingStyle(Style loadingStyle) {
        UBFXLibraryConfig.loadingStyle = loadingStyle;
    }
}
