package com.ubfx.library.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.ubfx.theme.ThemeMgr;

import java.io.File;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by chuanzheyang on 17/2/7.
 */

public class ImageLoadUtils {


    private static ColorDrawable mDefaultDrawable;
    private static Drawable mDefaultAvatarD;
    private static ThemeReceiver themeReceiver;

    public static ComponentCallbacks getComponentCallbacks(Context context) {
        return Glide.get(context.getApplicationContext());
    }

    public static void onTrimMemory(Context context, int level) {
        Glide.get(context.getApplicationContext()).onTrimMemory(level);
    }

    public static void onLowMemory(Context context) {
        Glide.get(context.getApplicationContext()).onLowMemory();
    }

    static void onThemeChanged() {
        mDefaultDrawable = null;
        if (mDefaultAvatarD instanceof ColorDrawable) {
            mDefaultAvatarD = null;
        }
    }

    private static void checkDefault(Context context) {
        if (themeReceiver == null) {
            themeReceiver = new ThemeReceiver();
            ThemeMgr.get().registerReceiver(context.getApplicationContext(), themeReceiver);
        }
        if (mDefaultDrawable == null) {
            mDefaultDrawable = new ColorDrawable(ThemeMgr.get().getThemeProvider().background());
        }
        if (mDefaultAvatarD == null) {
            mDefaultAvatarD = new ColorDrawable(ThemeMgr.get().getThemeProvider().background());
        }
    }

    public static void initDefaultDrawable(Drawable defaultAvatar) {
        mDefaultAvatarD = defaultAvatar;
    }

    public static void loadImage(@Nullable String url, @NonNull ImageView imageView) {
        if (TextUtils.isEmpty(url)) {
            url = null;
        }
        checkDefault(imageView.getContext());
        if (isValidImageViewForGlide(imageView)) {
            RequestOptions defaultOptions = new RequestOptions()
                    .placeholder(mDefaultDrawable)
                    .error(mDefaultDrawable)
                    .priority(Priority.HIGH);

            Glide.with(imageView.getContext())
                    .load(url).apply(defaultOptions).transition(withCrossFade()).into(imageView);
        }
    }

    public static void loadImageNoPlaceholder(@Nullable String url, @NonNull ImageView imageView) {
        if (TextUtils.isEmpty(url)) {
            url = null;
        }
        checkDefault(imageView.getContext());
        if (isValidImageViewForGlide(imageView)) {
            RequestOptions defaultOptions = new RequestOptions()
                    .priority(Priority.HIGH);

            Glide.with(imageView.getContext())
                    .load(url).apply(defaultOptions).transition(withCrossFade()).into(imageView);
        }

    }

    public static void loadImage(@Nullable String url, @NonNull ImageView imageView, @DrawableRes int errorRes) {
        if (TextUtils.isEmpty(url)) {
            url = null;
        }
        checkDefault(imageView.getContext());
        if (isValidImageViewForGlide(imageView)) {
            RequestOptions defaultOptions = new RequestOptions()
                    .placeholder(mDefaultDrawable)
                    .error(errorRes)
                    .priority(Priority.HIGH);

            Glide.with(imageView.getContext())
                    .load(url).apply(defaultOptions).transition(withCrossFade()).into(imageView);
        }

    }

    public static void loadImageWithOptions(@Nullable String url, @NonNull ImageView imageView, RequestOptions options) {
        if (TextUtils.isEmpty(url)) {
            url = null;
        }
        checkDefault(imageView.getContext());
        if (isValidImageViewForGlide(imageView)) {
            Glide.with(imageView.getContext())
                    .load(url)
                    .apply(options)
                    .into(imageView);
        }
    }


    public static void loadAvatar(@Nullable String url, @NonNull ImageView imageView) {
        if (TextUtils.isEmpty(url)) {
            url = null;
        }
        checkDefault(imageView.getContext());
        if (isValidImageViewForGlide(imageView)) {
            RequestOptions defaultOptions = new RequestOptions()
                    .placeholder(mDefaultAvatarD)
                    .error(mDefaultAvatarD)
                    .priority(Priority.IMMEDIATE);

            Glide.with(imageView.getContext())
                    .load(url).apply(defaultOptions).into(imageView);
        }
    }

    public static void loadImage(@NonNull File file, @NonNull ImageView imageView) {
        if (isValidImageViewForGlide(imageView)) {
            Glide.with(imageView.getContext())
                    .load(file).into(imageView);
        }
    }


    public static boolean isValidImageViewForGlide(final ImageView imageView) {
        if (imageView == null) {
            return false;
        }
        Activity activity = ViewUtils.getActivity(imageView.getContext());
        if (activity != null && (activity.isDestroyed() || activity.isFinishing())) {
            return false;
        }

        return true;
    }


    static class ThemeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ImageLoadUtils.onThemeChanged();
        }
    }
}
