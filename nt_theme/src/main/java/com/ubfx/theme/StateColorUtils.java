package com.ubfx.theme;

import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;

/**
 * Created by yangchuanzhe on 2019/8/21.
 */
public class StateColorUtils {

    public static ColorStateList getStateColorList(@ColorInt int normal, @ColorInt int checked, @ColorInt int selected) {
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_checked},
                new int[]{android.R.attr.state_selected},
                new int[]{}
        };

        int[] colors = new int[]{
                checked,
                selected,
                normal
        };

        return new ColorStateList(states, colors);
    }
}
