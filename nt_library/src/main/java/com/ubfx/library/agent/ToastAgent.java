package com.ubfx.library.agent;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ubfx.log.LogUtils;;


/**
 * Created by yangchuanzhe on 2019/2/22.
 */
public class ToastAgent {
    @LayoutRes
    private int messageLayout;
    @LayoutRes
    private int successLayout;

    private Toast mToast;
    private Toast mSuccessToast;

    /**
     *
     * @param messageLayout common message toast , set 0 if ignore
     * @param successLayout success toast , set 0 if ignore
     */
    public ToastAgent(@LayoutRes int messageLayout, @LayoutRes int successLayout) {
        this.messageLayout = messageLayout;
        this.successLayout = successLayout;
    }


    public void toast(Context context, @NonNull String msg) {
        if (messageLayout == 0) {
            LogUtils.e("Toast common Layout not be set !!!");
            return;
        }
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = new Toast(context);
        LayoutInflater inflate = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflate == null) {
            return;
        }
        View v = inflate.inflate(messageLayout, null);
        if (v instanceof ViewGroup) {
            int childCount = ((ViewGroup) v).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = ((ViewGroup) v).getChildAt(i);
                if (childView instanceof TextView) {
                    ((TextView) childView).setText(msg);
                    break;
                }
            }
        }

        mToast.setView(v);
        mToast.setDuration(msg.length() < 10 ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    public void toastSuccess(Context context, @NonNull String msg) {
        if (successLayout == 0) {
            LogUtils.e("Toast Success Layout not be set !!!");
            return;
        }
        if (TextUtils.isEmpty(msg)) {
            return;
        }

        if (mSuccessToast != null) {
            mSuccessToast.cancel();
        }

        mSuccessToast = new Toast(context);
        LayoutInflater inflate = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflate == null) {
            return;
        }

        View v = inflate.inflate(successLayout, null);
        if (v instanceof ViewGroup) {
            TextView textView = getTv((ViewGroup) v);
            if (textView != null) {
                textView.setText(msg);
            } else {
                LogUtils.e("There is not TextView in success toast layout !!!");
            }
        }

        mSuccessToast.setView(v);
        mSuccessToast.setDuration(Toast.LENGTH_SHORT);
        mSuccessToast.setGravity(Gravity.CENTER, 0, 0);
        mSuccessToast.show();
    }

    private TextView getTv(ViewGroup viewGroup) {
        TextView textView = null;
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof TextView) {
                textView = (TextView) child;
                break;
            }
            if (child instanceof ViewGroup) {
                textView = getTv((ViewGroup) child);
            }
        }
        return textView;
    }
}
