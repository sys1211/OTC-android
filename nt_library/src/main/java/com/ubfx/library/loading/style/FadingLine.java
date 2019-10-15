package com.ubfx.library.loading.style;

import android.animation.ValueAnimator;
import android.os.Build;

import com.ubfx.library.loading.animation.SpriteAnimatorBuilder;
import com.ubfx.library.loading.sprite.CircleLineLayoutContainer;
import com.ubfx.library.loading.sprite.LineSprite;
import com.ubfx.library.loading.sprite.Sprite;


/**
 * Like chrysanthemum
 */
public class FadingLine extends CircleLineLayoutContainer {

    @Override
    public Sprite[] onCreateChild() {
        Dot[] dots = new Dot[10];
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new Dot();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                dots[i].setAnimationDelay(1200 / 10 * i);
            } else {
                dots[i].setAnimationDelay(1200 / 10 * i + -1200);
            }
        }
        return dots;
    }

    private class Dot extends LineSprite {

        Dot() {
            setAlpha(0);
        }

        @Override
        public ValueAnimator onCreateAnimation() {
            float fractions[] = new float[]{0f, 0.39f, 0.4f, 1f};
            return new SpriteAnimatorBuilder(this).
                    alpha(fractions, 0, 0, 255, 0).
                    duration(1200).
                    easeInOut(fractions).build();
        }
    }
}
