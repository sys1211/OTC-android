package com.ubfx.library.loading.sprite;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;

/**
 * Created by chuanzheyang on 28/4/18.
 */
public class RingSprite extends ShapeSprite {


    private RectF rectF;

    @IntRange(from = 0, to = 360)
    protected int getRingSweepAngle() {
        return 360;
    }

    @FloatRange(from = 0, to = 1)
    protected float getStrokeRadiusRatio() {
        return 0.125f;
    }

    @FloatRange(from = 0, to = 1)
    protected float getContentRadiusRatio() {
        return 0.9f;
    }

    @Override
    public void drawShape(Canvas canvas, Paint paint) {
        if (getDrawBounds() != null) {
            paint.setStyle(Paint.Style.STROKE);
            int radius = Math.min(getDrawBounds().width(), getDrawBounds().height()) / 2;

            paint.setStrokeWidth(radius * getStrokeRadiusRatio());

            if (rectF == null) {
                rectF = new RectF();
            }

            // stoke width may occupy space
            radius = (int) (radius * getContentRadiusRatio());

            rectF.left = getDrawBounds().centerX() - radius;
            rectF.right = getDrawBounds().centerX() + radius;
            rectF.top = getDrawBounds().centerY() - radius;
            rectF.bottom = getDrawBounds().centerY() + radius;

            canvas.drawArc(rectF, 0, getRingSweepAngle(), false, paint);

        }
    }

    @Override
    public ValueAnimator onCreateAnimation() {
        return null;
    }
}
