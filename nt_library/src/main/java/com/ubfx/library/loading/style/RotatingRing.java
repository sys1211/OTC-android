package com.ubfx.library.loading.style;

import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.animation.LinearInterpolator;

import com.github.mikephil.charting.animation.ChartLoadingAnimator;
import com.ubfx.library.loading.animation.SpriteAnimatorBuilder;
import com.ubfx.library.loading.sprite.RingSprite;
import com.ubfx.library.loading.sprite.Sprite;
import com.ubfx.library.loading.sprite.SpriteContainer;

/**
 * Created by yangchuanzhe on 28/04/2018.
 * like a ring rotating
 */

public class RotatingRing extends SpriteContainer {

    @Override
    public Sprite[] onCreateChild() {
        return new Sprite[]{new Dot()};
    }


    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
//        bounds = clipSquare(bounds);
//        getChildAt(0).setDrawBounds(bounds);
    }

    private class Dot extends RingSprite {

        @Override
        protected int getRingSweepAngle() {
            return 270;
        }


        @Override
        protected float getStrokeRadiusRatio() {
            return 0.15f;
        }

        @Override
        public ValueAnimator onCreateAnimation() {
            float fractions[] = new float[]{0f, 1f};
            return new SpriteAnimatorBuilder(this).
                    rotate(fractions, 0, 360).
                    duration(1500).
                    interpolator(new LinearInterpolator()).build();
        }
    }

    @Override
    public void setPercent(float percent) {
        super.setPercent(percent);
        setAlpha((int) (255 * percent));
        int rotation = (int) (percent * 360);
        setRotate(rotation);
    }
}
