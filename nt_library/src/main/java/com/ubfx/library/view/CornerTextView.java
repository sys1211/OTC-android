package com.ubfx.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.ubfx.library.R;
import com.ubfx.theme.ThemeMgr;

/**
 * @author wangwenting
 * @date 2018/12/13
 */
public class CornerTextView extends AppCompatTextView {

    private GradientDrawable gd;

    public CornerTextView(Context context) {
        this(context, null);
    }

    public CornerTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CornerTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs, defStyle);

    }

    private void initView(Context context, AttributeSet attrs, int defStyle) {

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CornerTextView, defStyle, 0);
        if (attributes != null) {
            int defaultBorderColor = ThemeMgr.get().getThemeProvider().borderLight();
            int rtvBorderWidth = attributes.getDimensionPixelSize(R.styleable.CornerTextView_rtvBorderWidth, 0);
            int rtvBorderColor = attributes.getColor(R.styleable.CornerTextView_rtvBorderColor, defaultBorderColor);
            int rtvRadius = (int) attributes.getDimension(R.styleable.CornerTextView_rtvRadius, 0);
            int rtvBgColor = attributes.getColor(R.styleable.CornerTextView_rtvBgColor, defaultBorderColor);
            attributes.recycle();

            //创建drawable
            gd = new GradientDrawable();
            gd.setColor(rtvBgColor);
            gd.setCornerRadius(rtvRadius);
            if (rtvBorderWidth > 0) {
                gd.setStroke(rtvBorderWidth, rtvBorderColor);
            }

            this.setBackground(gd);
        }
    }

    public void setCornerBackgroundColor(@ColorInt int color) {
        gd.setColor(color);
    }

    public void setCornerSize(int cornerSize) {
        gd.setCornerRadius(cornerSize);
    }
}