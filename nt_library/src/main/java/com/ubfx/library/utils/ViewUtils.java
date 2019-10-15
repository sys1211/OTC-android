package com.ubfx.library.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Canvas;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ListView;

/**
 * Created by chuanzheyang on 2017/8/17.
 */

public class ViewUtils {


    @Nullable
    public static Activity getActivity(@NonNull View view) {
        return getActivity(view.getContext());
    }

    @Nullable
    public static Activity getActivity(Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        }
        if (context instanceof ContextWrapper) {
            Context base = ((ContextWrapper) context).getBaseContext();
            if (base instanceof Activity) {
                return (Activity) base;
            }
        }
        return null;
    }

    /**
     * draw ForegroundColorSpan string
     *
     * @param canvas
     * @param spannableString must only contain ForegroundColorSpan
     * @param mTextPaint
     */
    public static void drawSpannableStr(Canvas canvas, SpannableStringBuilder spannableString, float x, float y, TextPaint mTextPaint) {
        // draw each span one at a time
        int next;
        float xStart = x;
        float xEnd;
        int spanIndex = 0;
        for (int i = 0; i < spannableString.length(); i = next) {

            // find the next span transition
            next = spannableString.nextSpanTransition(i, spannableString.length(), CharacterStyle.class);

            // measure the length of the span
            xEnd = xStart + mTextPaint.measureText(spannableString, i, next);

            // draw the text with an optional foreground color
            ForegroundColorSpan[] fgSpans = spannableString.getSpans(i, next, ForegroundColorSpan.class);
            if (fgSpans.length > spanIndex) {
                int saveColor = mTextPaint.getColor();
                mTextPaint.setColor(fgSpans[spanIndex].getForegroundColor());
                canvas.drawText(spannableString, i, next, xStart, y, mTextPaint);
                mTextPaint.setColor(saveColor);
                spanIndex++;
            } else {
                canvas.drawText(spannableString, i, next, xStart, y, mTextPaint);
            }

            xStart = xEnd;
        }
    }

    public static int getColorWithAlpha(float alpha, @ColorInt int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        return a + rgb;
    }

    public static int getListViewScrollY(ListView listView) {
        View c = listView.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight();
    }
}
