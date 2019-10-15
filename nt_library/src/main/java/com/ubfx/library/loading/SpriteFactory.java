package com.ubfx.library.loading;


import com.ubfx.library.loading.sprite.Sprite;
import com.ubfx.library.loading.style.FadingLine;
import com.ubfx.library.loading.style.RotatingRing;
import com.ubfx.library.loading.style.Wave;

/**
 * Created by ybq.
 */
public class SpriteFactory {

    public static Sprite create(Style style) {
        Sprite sprite = null;
        switch (style) {
            case FADING_LINE:
                sprite = new FadingLine();
                break;
            case ROTATING_RING:
                sprite = new RotatingRing();
                break;
            case WAVE:
                sprite = new Wave();
                break;
            default:
                break;
        }
        return sprite;
    }
}
