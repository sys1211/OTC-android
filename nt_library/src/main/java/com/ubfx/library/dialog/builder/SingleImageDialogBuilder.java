package com.ubfx.library.dialog.builder;

import android.view.View;
import android.view.animation.Animation;

import com.ubfx.library.dialog.DialogModelDataSource;

/**
 * Created by yangchuanzhe on 2019/2/26.
 */
public class SingleImageDialogBuilder {

    private boolean isCancelable;
    private View.OnClickListener mImageClickListener;
    private View.OnClickListener mCancelClickListener;
    private DialogModelDataSource dataSource;

    private Animation outAnimation;

    public SingleImageDialogBuilder(DialogModelDataSource dataSource) {
        this.isCancelable = false;
        this.dataSource = dataSource;
    }

    public void setOutAnimation(Animation outAnimation) {
        this.outAnimation = outAnimation;
    }

    public Animation getOutAnimation() {
        return outAnimation;
    }

    public DialogModelDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DialogModelDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean isCancelable() {
        return isCancelable;
    }

    public void setCancelable(boolean cancelable) {
        isCancelable = cancelable;
    }

    public View.OnClickListener getImageClickListener() {
        return mImageClickListener;
    }

    public void setImageClickListener(View.OnClickListener imageClickListener) {
        this.mImageClickListener = imageClickListener;
    }

    public View.OnClickListener getCancelClickListener() {
        return mCancelClickListener;
    }

    public void setCancelClickListener(View.OnClickListener cancelClickListener) {
        this.mCancelClickListener = cancelClickListener;
    }
}
