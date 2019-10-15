package com.ubfx.library.view;

import android.content.Context;
import android.util.AttributeSet;

import com.ubfx.library.view.ObservableScrollView;

/**
 * Created by chuanzheyang on 17/3/7.
 */

public class InnerScrollView extends ObservableScrollView {

    public InnerScrollView(Context paramContext) {
        super(paramContext);
    }

    public InnerScrollView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public InnerScrollView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    public boolean isBottom() {
        boolean bool1 = false;
        if (getChildAt(0) != null) {
            bool1 = getChildAt(0).getMeasuredHeight() <= getScrollY() + getHeight();
        }
        return bool1;
    }

    public boolean isTop() {
        return getScrollY() == 0;
    }
}
