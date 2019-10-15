package com.github.mikephil.charting.animation;

import android.graphics.drawable.Drawable;

/**
 * Created by yangchuanzhe on 09/05/2018.
 */

public interface ChartLoadingAnimator {

    public void startLoading();
    public void stopLoading();
    public Drawable getLoadingDrawable();
}
