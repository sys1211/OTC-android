package com.ubfx.library.dialog.builder;

import android.content.Context;

import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.ubfx.library.R;
import com.ubfx.library.dialog.OnConfirmListener;

public class ConfirmDialogBuilder {
    private String content;
    private String title;
    private String confirmDesc;
    private String cancelDesc;
    private OnConfirmListener confirmListener;
    private OnCancelListener cancelListener;
    private OnDismissListener dismissListener;
    private boolean isCancelable;

    public ConfirmDialogBuilder(Context context) {
        confirmDesc = context.getString(R.string.dialog_confirm);
        cancelDesc = context.getString(R.string.dialog_cancel);
        isCancelable = true;
    }

    public boolean isCancelable() {
        return isCancelable;
    }

    public ConfirmDialogBuilder setCancelable(boolean cancelable) {
        isCancelable = cancelable;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ConfirmDialogBuilder setContent(String content) {
        this.content = content;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ConfirmDialogBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getConfirmDesc() {
        return confirmDesc;
    }

    public ConfirmDialogBuilder setConfirmDesc(String confirmDesc) {
        this.confirmDesc = confirmDesc;
        return this;
    }

    public String getCancelDesc() {
        return cancelDesc;
    }

    public ConfirmDialogBuilder setCancelDesc(String cancelDesc) {
        this.cancelDesc = cancelDesc;
        return this;
    }

    public OnConfirmListener getConfirmListener() {
        return confirmListener;
    }

    public ConfirmDialogBuilder setConfirmListener(OnConfirmListener confirmListener) {
        this.confirmListener = confirmListener;
        return this;
    }

    public OnCancelListener getCancelListener() {
        return cancelListener;

    }

    public ConfirmDialogBuilder setCancelListener(OnCancelListener cancelListener) {
        this.cancelListener = cancelListener;
        return this;
    }

    public OnDismissListener getDismissListener() {
        return dismissListener;
    }

    public ConfirmDialogBuilder setDismissListener(OnDismissListener dismissListener) {
        this.dismissListener = dismissListener;
        return this;
    }
}