package com.ubfx.library.view.recycler;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.ubfx.library.R;
import com.ubfx.library.UBFXLibraryConfig;
import com.ubfx.library.loading.SpinKitView;
import com.ubfx.library.loading.SpriteFactory;
import com.ubfx.library.loading.sprite.Sprite;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * Created by yangchuanzhe on 2019/1/22.
 */
public class UBFXPtrHeader extends ConstraintLayout implements PtrUIHandler {

    protected SpinKitView loadingView;

    public UBFXPtrHeader(Context context) {
        super(context);
        initView(context);
    }

    public UBFXPtrHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public UBFXPtrHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void setAnimationColor(@ColorInt int color) {
        if (loadingView != null) {
            loadingView.getIndeterminateDrawable().setColor(color);
        }
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.lib_layout_ptr_header, this);
        loadingView = findViewById(R.id.loading_view);
        Sprite sprite = SpriteFactory.create(UBFXLibraryConfig.getLoadingStyle());
        sprite.setColor(UBFXLibraryConfig.getMainColor());
        loadingView.setIndeterminateDrawable(sprite);
    }


    @Override
    public void onUIReset(PtrFrameLayout frame) {
        loadingView.getIndeterminateDrawable().stop();
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        loadingView.getIndeterminateDrawable().stop();
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        loadingView.getIndeterminateDrawable().setAlpha(255);
        loadingView.getIndeterminateDrawable().start();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        loadingView.getIndeterminateDrawable().stop();
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        float percent = Math.min(1f, ptrIndicator.getCurrentPercent());

        if (status == PtrFrameLayout.PTR_STATUS_PREPARE) {
            loadingView.getIndeterminateDrawable().setPercent(percent);
            loadingView.invalidate();
        }
    }
}
