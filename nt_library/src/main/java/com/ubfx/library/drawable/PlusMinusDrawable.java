package com.ubfx.library.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by yangchuanzhe on 2018/12/14.
 */
public class PlusMinusDrawable extends Drawable {
    private Paint mPaint;


    private boolean plus = true;

    public PlusMinusDrawable(boolean plus) {
        this.plus = plus;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(4);
    }

    public void setStrokeWidth(float width) {
        mPaint.setStrokeWidth(width);
    }

    @Override
    public int getIntrinsicWidth() {
        return 36;
    }

    @Override
    public int getIntrinsicHeight() {
        return 36;
    }


    @Override
    public void draw(@NonNull Canvas canvas) {
        float y = getIntrinsicHeight() / 2f;
        canvas.drawLine(0f, y, getIntrinsicWidth(), y, mPaint);

        if (plus) {
            float x = getIntrinsicWidth() / 2f;
            canvas.drawLine(x, 0, x, getIntrinsicHeight(), mPaint);
        }

    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }
}
