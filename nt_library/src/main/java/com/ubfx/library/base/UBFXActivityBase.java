package com.ubfx.library.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.LocaleList;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.ubfx.library.agent.ImagePickerAgent;
import com.ubfx.library.agent.TakeCaptureAgent;
import com.ubfx.library.agent.ToastAgent;
import com.ubfx.library.language.LangManager;
import com.ubfx.library.utils.DeviceUtils;
import com.ubfx.library.loading.utils.LoadingUtils;
import com.ubfx.log.LogUtils;;
import com.ubfx.library.utils.ScreenUtils;
import com.ubfx.library.loading.SpinKitView;

import java.util.Locale;

/**
 * Created by yangchuanzhe on 2019/2/21.
 */
public abstract class UBFXActivityBase extends AppCompatActivity {

    protected ToastAgent mToastAgent;

    protected ImagePickerAgent imagePickerAgent;
    protected TakeCaptureAgent takeCaptureAgent;

    private View loadingDialog;

    private boolean loadingVisible;

    public boolean isloadingVisible() {
        return loadingVisible;
    }


    public void setToastAgent(ToastAgent toastAgent) {
        this.mToastAgent = toastAgent;
    }


    public void toast(@NonNull String msg) {
        if (mToastAgent != null) {
            mToastAgent.toast(this, msg);
        } else {
            LogUtils.e("Toast agent not be set , there is not toast !!!");
        }
    }

    public void toastSuccess(@NonNull String msg) {
        if (mToastAgent != null) {
            mToastAgent.toastSuccess(this, msg);
        } else {
            LogUtils.e("Toast agent not be set , there is not toast !!!");
        }
    }

    public void showLoading() {
        if (isFinishing()) {
            return;
        }
        DeviceUtils.hideKeyboard(getWindow().getDecorView().getRootView());
        loadingVisible = true;

        if (loadingDialog == null) {

            loadingDialog = LoadingUtils.createLoadingView(this);

            FrameLayout rootLayout = findViewById(android.R.id.content);
            int width = ScreenUtils.dp2px(this, 100);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, width);
            params.gravity = Gravity.CENTER;
            rootLayout.addView(loadingDialog, params);
        }
        loadingDialog.bringToFront();
        loadingDialog.setVisibility(View.VISIBLE);
        if (loadingDialog instanceof CardView) {
            ((SpinKitView) ((CardView) loadingDialog).getChildAt(0)).getIndeterminateDrawable().start();
        }
    }

    public void finishLoading() {
        if (loadingDialog != null) {
            loadingVisible = false;

            loadingDialog.setVisibility(View.GONE);
            if (loadingDialog instanceof CardView) {
                ((SpinKitView) ((CardView) loadingDialog).getChildAt(0)).getIndeterminateDrawable().stop();
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            Resources res = newBase.getResources();
            Configuration configuration = res.getConfiguration();
            Locale locale = LangManager.get().getCurrentLocale();
            configuration.setLocale(locale);
            if (configuration.fontScale != 1) {
                configuration.fontScale = 1;
            }
            LocaleList localeList = new LocaleList(locale);
            LocaleList.setDefault(localeList);
            configuration.setLocales(localeList);
            Context newContext = newBase.createConfigurationContext(configuration);
            super.attachBaseContext(newContext);
        } else {
            Resources res = newBase.getResources();
            Configuration configuration = res.getConfiguration();
            if (configuration.fontScale != 1) {
                DisplayMetrics displayMetrics = res.getDisplayMetrics();
                configuration.fontScale = 1;
                displayMetrics.scaledDensity = displayMetrics.density * configuration.fontScale;
                Context newContext = newBase.createConfigurationContext(configuration);
                super.attachBaseContext(newContext);
            } else {
                super.attachBaseContext(newBase);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (imagePickerAgent != null && imagePickerAgent.handleActivityResult(this, requestCode, resultCode, data)) {
            imagePickerAgent = null;
        }

        if (takeCaptureAgent != null && takeCaptureAgent.handleActivityResult(requestCode, resultCode, data)) {
            takeCaptureAgent = null;
        }
    }

    public boolean showImagePicker(ImagePickerAgent imagePickerAgent) {
        this.imagePickerAgent = imagePickerAgent;
        return imagePickerAgent.show();
    }


    public boolean showCapture(Uri uri, TakeCaptureAgent takeCaptureAgent) {
        this.takeCaptureAgent = takeCaptureAgent;
        return takeCaptureAgent.show(uri);
    }

}
