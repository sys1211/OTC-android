package com.ubfx.library.utils;

import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.support.annotation.ColorInt;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;

import com.ubfx.theme.ThemeMgr;

import java.util.Set;

/**
 * Created by chuanzheyang on 2017/8/14.
 */

public class DrawableUtils {

    public static Drawable tint(Drawable d, ColorStateList colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(d);
        DrawableCompat.setTintList(wrappedDrawable, colors);

        return wrappedDrawable;
    }

    public static Drawable tint(Drawable d, @ColorInt int color) {
        return tint(d, ColorStateList.valueOf(color));
    }

    public static void setBackgroundDrawable(View[] views){
        for (View view : views){
            setBackgroundDrawable(view);
        }
    }

    public static void setBackgroundDrawable(View view){
        Drawable unPressedDrawable = DrawableUtils.createDrawable(0, ThemeMgr.get().getThemeProvider().backgroundMain());
        Drawable pressedDrawable = DrawableUtils.createDrawable(0, ThemeMgr.get().getThemeProvider().backgroundLight());
        StateListDrawable stateListDrawable = new StateListDrawable();
        int statePressed = android.R.attr.state_pressed;
        stateListDrawable.addState(new int[]{statePressed}, pressedDrawable);
        stateListDrawable.addState(new int[]{}, unPressedDrawable);
        view.setBackground(stateListDrawable);
    }

    public static Drawable createDrawable(float corner, @ColorInt int solidColor) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(solidColor);
        drawable.setCornerRadius(corner);
        return drawable;
    }


    public static Drawable createDrawable(float corner, @ColorInt int solidColor, @ColorInt int strokeColor) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(solidColor);
        drawable.setCornerRadius(corner);
        drawable.setStroke(1, strokeColor);
        return drawable;
    }

    public static Drawable createDashDrawable(float corner, @ColorInt int solidColor, @ColorInt int strokeColor, float dashWidth, float dashGap) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(solidColor);
        drawable.setCornerRadius(corner);
        drawable.setStroke(1, strokeColor, dashWidth, dashGap);
        return drawable;
    }

    public static Drawable createDrawable(float topLeftCorner, float topRightCorner, float bottomLeftCorner, float bottomRightCorner, @ColorInt int solidColor, @ColorInt int strokeColor) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(solidColor);
        float[] outR = new float[] {topLeftCorner, topLeftCorner, topRightCorner, topRightCorner, bottomRightCorner, bottomRightCorner, bottomLeftCorner, bottomLeftCorner};
        drawable.setCornerRadii(outR);
        drawable.setStroke(1, strokeColor);
        return drawable;
    }

//    public static Drawable createShapeDrawable(float corner, @ColorInt int solidColor, @ColorInt int strokeColor) {
//        ShapeDrawable drawable = new ShapeDrawable();
//        float[] outR = new float[] { corner, corner, corner, corner, corner, corner, corner, corner };
//
//        RoundRectShape shape = new RoundRectShape(outR,null,null);
//        drawable.setShape(shape);
//        drawable.getPaint().setColor(solidColor);
//        drawable.getPaint().setStyle(Paint.Style.FILL_AND_STROKE);
//        drawable.getPaint().setStrokeWidth(1);
//        return drawable;
//    }


}
