package com.ubfx.library.view.recycler;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;


import com.ubfx.library.R;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;

/**
 * Created by yangchuanzhe on 2018/11/5.
 */
public class UBFXPtrFrameLayout extends PtrClassicFrameLayout {


    public UBFXPtrFrameLayout(Context context) {
        super(context);
        init();
    }

    public UBFXPtrFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UBFXPtrFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setAnimationColor(@ColorInt int color) {
        if (getHeaderView() instanceof UBFXPtrHeader) {
            ((UBFXPtrHeader) getHeaderView()).setAnimationColor(color);
        }
    }

    public void setNewHeader(View view) {
        View headerView = getHeaderView();
        if (headerView != null) {
            setHeaderView(view);
        }
        if (view instanceof PtrUIHandler) {
            addPtrUIHandler((PtrUIHandler) view);
            if (headerView instanceof PtrUIHandler) {
                removePtrUIHandler((PtrUIHandler) headerView);
            }
        }
    }

    private void init() {
        setDurationToCloseHeader(200);
        //setBackgroundColor(getResources().getColor(R.color.lib_color_f7));
        setRatioOfHeaderHeightToRefresh(1.0f);
        UBFXPtrHeader header = new UBFXPtrHeader(getContext());
        setHeaderView(header);
        addPtrUIHandler(header);
    }
}
