package com.ubfx.library.base;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ubfx.library.utils.DeviceUtils;
import com.ubfx.library.loading.utils.LoadingUtils;
import com.ubfx.library.utils.ScreenUtils;
import com.ubfx.library.loading.SpinKitView;

/**
 * Created by yangchuanzhe on 2019/2/21.
 */
public abstract class UBFXFragmentBase extends BaseVisibilityFragment {

    protected View pageView;

    private View progressBar;

    private boolean loadingVisible;

    public boolean isLoadingVisible() {
        return loadingVisible;
    }

    abstract public int getLayoutId();

    abstract public void onInitializeView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getLayoutId() == 0) {
            throw new UnknownError("Fragment layout view id is not initialized");
        }

        if (pageView == null) {
            View rootView = inflater.inflate(getLayoutId(), container, false);

            FrameLayout rootWrapper = new FrameLayout(getContext());
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            rootWrapper.addView(rootView, params);

            pageView = rootWrapper;
            // notify to initialize view
            onInitializeView(inflater, (ViewGroup) rootView, savedInstanceState);

        }
        return pageView;
    }


    public void showLoading() {
        if(getActivity() != null){
            DeviceUtils.hideKeyboard(getActivity().getWindow().getDecorView().getRootView());
        }

        if (progressBar == null && pageView instanceof FrameLayout) {

            if (getContext() == null) {
                return;
            }

            progressBar = LoadingUtils.createLoadingView(getContext());
            FrameLayout rootLayout = (FrameLayout) pageView;
            int width = ScreenUtils.dp2px(getContext(), 100);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, width);
            params.gravity = Gravity.CENTER;

            rootLayout.addView(progressBar, params);
        }
        if (progressBar != null) {
            progressBar.bringToFront();
            progressBar.setVisibility(View.VISIBLE);
            if (progressBar instanceof CardView) {
                ((SpinKitView) ((CardView) progressBar).getChildAt(0)).getIndeterminateDrawable().start();
            }

            loadingVisible = true;
        }
    }

    public void finishLoading() {
        if (progressBar != null) {
            loadingVisible = false;

            progressBar.setVisibility(View.GONE);
            if (progressBar instanceof CardView) {
                ((SpinKitView) ((CardView) progressBar).getChildAt(0)).getIndeterminateDrawable().stop();
            }
        }
    }


    public void toast(String msg) {
        if (isFragmentVisible() && getActivity() != null) {
            ((UBFXActivityBase) getActivity()).toast(msg);
        }
    }
    public void toastSuccess(String msg) {
        if (isFragmentVisible() && getActivity() != null) {
            ((UBFXActivityBase) getActivity()).toastSuccess(msg);
        }
    }
}
