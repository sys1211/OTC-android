package com.ubfx.library.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import com.ubfx.library.R;
import com.ubfx.library.UBFXLibraryConfig;
import com.ubfx.log.LogUtils;

/**
 * Created by yangchuanzhe on 27/04/2018.
 */

public class StyleButton extends AppCompatButton {

    // state normal
    @ColorInt
    private int normalColor;
    @ColorInt
    private int disableColor;

    // corner radius
    private float currCorner;

    private float strokeWidth;

    @ColorInt
    private int normalStrokeColor;
    @ColorInt
    private int disableStrokeColor;


    private GradientDrawable normalDrawable;

    private GradientDrawable disableDrawable;
    private GradientDrawable pressedDrawable;


    public StyleButton(Context context) {
        this(context, null);
    }

    public StyleButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StyleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        init();
    }


    private void init() {

        normalDrawable = new GradientDrawable();
        normalDrawable.setColor(normalColor);
        normalDrawable.setStroke((int) strokeWidth, normalStrokeColor);
        normalDrawable.setCornerRadius(currCorner);

        disableDrawable = new GradientDrawable();
        disableDrawable.setColor(disableColor);// use normal color default
        disableDrawable.setStroke((int) strokeWidth, disableStrokeColor);
        disableDrawable.setCornerRadius(currCorner);

        pressedDrawable = new GradientDrawable();
        pressedDrawable.setColor(getPressedColor(this.normalColor));// use normal color with alpha default
        pressedDrawable.setStroke((int) strokeWidth, getPressedColor(this.normalStrokeColor));
        pressedDrawable.setCornerRadius(currCorner);

        // TODO different state
        int statePressed = android.R.attr.state_pressed;
//        int stateChecked = android.R.attr.state_checked;
//        int stateFocused = android.R.attr.state_focused;
        int stateEnable = android.R.attr.state_enabled;

        StateListDrawable stateList = new StateListDrawable();
        // The order of adding state is very important!!!
        stateList.addState(new int[]{-stateEnable}, disableDrawable);
        stateList.addState(new int[]{statePressed}, pressedDrawable);
        stateList.addState(new int[]{}, normalDrawable);

        setBackgroundDrawable(stateList);
    }

    private int getPressedColor(int normalColor) {
        // 20% alpha black
        return ColorUtils.compositeColors(Color.argb(51, 0, 0, 0), normalColor);
    }

    private void initAttrs(AttributeSet attrs) {
        if (attrs != null) {
            @SuppressLint("CustomViewStyleable")
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ButtonStyle);
            normalColor = typedArray.getColor(R.styleable.ButtonStyle_normal_color, UBFXLibraryConfig.getMainColor());
            disableColor = typedArray.getColor(R.styleable.ButtonStyle_disable_color, normalColor);
            strokeWidth = typedArray.getDimension(R.styleable.ButtonStyle_stroke_width, Color.TRANSPARENT);
            normalStrokeColor = typedArray.getColor(R.styleable.ButtonStyle_stroke_color, Color.TRANSPARENT);
            disableStrokeColor = typedArray.getColor(R.styleable.ButtonStyle_stroke_color, Color.TRANSPARENT);
            currCorner = typedArray.getDimension(R.styleable.ButtonStyle_corner, getResources().getDimension(R.dimen.lib_default_btn_corner_radius));
            typedArray.recycle();
        }
    }


    public void setNormalColor(@ColorRes int normalColor) {
        this.normalColor = getResources().getColor(normalColor);
        if (normalDrawable != null)
            normalDrawable.setColor(this.normalColor);

        if (pressedDrawable != null) {
            pressedDrawable.setColor(getPressedColor(this.normalColor));
        }
    }

    public void setDisableColor(@ColorRes int disableColor) {
        this.disableColor = getResources().getColor(disableColor);
        if (disableDrawable != null)
            disableDrawable.setColor(this.disableColor);
    }

    public void setDisableColorInt(@ColorInt int disableColor) {
        this.disableColor = disableColor;
        if (disableDrawable != null)
            disableDrawable.setColor(this.disableColor);
    }

    public void setNormalColor(String normalColor) {
        this.normalColor = Color.parseColor(normalColor);
        if (normalDrawable != null)
            normalDrawable.setColor(this.normalColor);

        if (pressedDrawable != null) {
            pressedDrawable.setColor(getPressedColor(this.normalColor));
        }
    }

    public void setNormalColorInt(@ColorInt int normalColor) {
        this.normalColor = normalColor;
        if (normalDrawable != null)
            normalDrawable.setColor(this.normalColor);
        if (pressedDrawable != null) {
            pressedDrawable.setColor(getPressedColor(this.normalColor));
        }
    }


    public void setCurrCorner(float currCorner) {
        this.currCorner = currCorner;
        if (normalDrawable != null)
            normalDrawable.setCornerRadius(currCorner);
        if (disableDrawable != null)
            disableDrawable.setCornerRadius(currCorner);
        if (pressedDrawable != null)
            pressedDrawable.setCornerRadius(currCorner);
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        if (normalDrawable != null)
            normalDrawable.setStroke((int) strokeWidth, this.normalStrokeColor);
        if (disableDrawable != null)
            disableDrawable.setStroke((int) strokeWidth, this.disableStrokeColor);
        if (pressedDrawable != null)
            pressedDrawable.setStroke((int) strokeWidth, getPressedColor(this.normalStrokeColor));
    }

    public void setNormalStrokeColor(@ColorRes int strokeColor) {
        this.normalStrokeColor = getResources().getColor(strokeColor);
        if (normalDrawable != null)
            normalDrawable.setStroke((int) strokeWidth, this.normalStrokeColor);
        if (pressedDrawable != null)
            pressedDrawable.setStroke((int) strokeWidth, getPressedColor(this.normalStrokeColor));
    }

    public void setDisableStrokeColor(@ColorRes int strokeColor) {
        this.disableStrokeColor = getResources().getColor(strokeColor);
        if (disableDrawable != null)
            disableDrawable.setStroke((int) strokeWidth, this.disableStrokeColor);
    }

}
