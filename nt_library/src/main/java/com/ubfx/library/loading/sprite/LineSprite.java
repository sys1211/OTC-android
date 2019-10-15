package com.ubfx.library.loading.sprite;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 */
public class LineSprite extends ShapeSprite {

    @Override
    public ValueAnimator onCreateAnimation() {
        return null;
    }

    @Override
    public void drawShape(Canvas canvas, Paint paint) {
        if (getDrawBounds() != null) {
            paint.setAntiAlias(true);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeWidth(6);
            canvas.drawLine(getDrawBounds().centerX(),getDrawBounds().top,getDrawBounds().centerX(),getDrawBounds().bottom,paint);
        }
    }
}
