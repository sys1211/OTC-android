package com.ubfx.library.loading.utils;

import android.support.annotation.ColorInt;

/**
 * Created by yangchuanzhe on 2019/3/7.
 */
public class LoadingHolder {
    @ColorInt
    private int colorBg;

    private int corner;

    private int cardElevation;

    public LoadingHolder(int colorBg, int corner, int cardElevation) {
        this.colorBg = colorBg;
        this.corner = corner;
        this.cardElevation = cardElevation;
    }

    public int getColorBg() {
        return colorBg;
    }

    public void setColorBg(int colorBg) {
        this.colorBg = colorBg;
    }

    public int getCorner() {
        return corner;
    }

    public void setCorner(int corner) {
        this.corner = corner;
    }

    public int getCardElevation() {
        return cardElevation;
    }

    public void setCardElevation(int cardElevation) {
        this.cardElevation = cardElevation;
    }
}
