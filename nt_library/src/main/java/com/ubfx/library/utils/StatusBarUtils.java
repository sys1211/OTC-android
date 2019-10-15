package com.ubfx.library.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.ubfx.library.R;


/**
 * Created by chuanzheyang on 2017/8/31.
 */

public class StatusBarUtils {
    public static void setWindowStatusBarColorInt(Activity activity, @ColorInt int colorResId) {
        // build version lager than 23 active
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity == null) {
                return;
            }

            Window window = activity.getWindow();
            if (window == null) {
                return;
            }

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(colorResId);
        }
    }

    public static void setWindowStatusBarColor(Activity activity, @ColorRes int colorResId) {
        setWindowStatusBarColorInt(activity, activity.getResources().getColor(colorResId));
    }

    @SuppressLint("InlinedApi")
    public static void transparencyBar(Activity activity) {
        transparencyBar(activity, true);
    }

    public static void transparencyBar(Activity activity, boolean statusBarLight) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            int statusBarFlag = statusBarLight ? View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN : View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            window.getDecorView().setSystemUiVisibility(statusBarFlag);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public static int getStateBar(Context mContext) {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void setRectangleForWindow(Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            View statusBarView = new View(activity);
            statusBarView.setId(R.id.statusbarutil_view);
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
            statusBarView.setBackgroundColor(Color.WHITE);
            statusBarView.setLayoutParams(params);
            decorView.addView(statusBarView);
            setRootView(activity);
        }
    }

    /**
     * 设置根布局参数
     */
    private static void setRootView(Activity activity) {
        ViewGroup parent = activity.findViewById(android.R.id.content);
        for (int i = 0, count = parent.getChildCount(); i < count; i++) {
            View childView = parent.getChildAt(i);
            if (childView instanceof ViewGroup) {
                childView.setFitsSystemWindows(true);
                ((ViewGroup) childView).setClipToPadding(true);
            }
        }
    }

    private static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    public static void setBackgroundColors(Activity activity, @ColorInt int[] colors) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View statusBarView = activity.findViewById(R.id.statusbarutil_view);
            if (statusBarView != null) {
                if (colors.length == 1) {
                    statusBarView.setBackgroundColor(colors[0]);
                } else if (colors.length == 2) {
                    GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
                    statusBarView.setBackground(drawable);
                } else {
                    statusBarView.setBackgroundColor(Color.WHITE);
                }
            } else {
                throw new RuntimeException("setRectangleForWindow Must Be Called In Advance");
            }
        }
    }
}