package com.ubfx.library.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.ubfx.library.view.ObservableScrollView;

/**
 * Created by limeng on 2017/9/16.
 */

public class LockableScrollView extends ObservableScrollView {

    private OnChildScrollAttachTopGetter attachTopGetter;


    public void setAttachTopGetter(OnChildScrollAttachTopGetter attachTopGetter) {
        this.attachTopGetter = attachTopGetter;
    }

    private boolean canScroll = true;

    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }

    private int scrollHeight;

    public void setScrollHeight(int scrollHeight) {
        this.scrollHeight = scrollHeight;
    }

    public LockableScrollView(Context context) {
        super(context);
    }

    public LockableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LockableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // Negative to check scrolling up, positive to check scrolling down.
    @Override
    public boolean canScrollVertically(int direction) {
        if (attachTopGetter != null && direction < 0) {
            return !attachTopGetter.attachTop();
        }
        return super.canScrollVertically(direction);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (getScrollY() >= scrollHeight) {
            super.onNestedPreScroll(target, dx, dy, consumed);
        } else {
            scrollBy(dx, dy);
            consumed[0] = 0;
            consumed[1] = dy;
        }
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (getScrollY() >= scrollHeight) {
            return super.onNestedPreFling(target, velocityX, velocityY);
        }else{
            fling((int) velocityY);
            return false;
        }

    }


    public interface OnChildScrollAttachTopGetter {
        boolean attachTop();
    }

}
