package com.ubfx.library.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

public class HandlerUtils {
    public interface Callback{
        void handleMessage(Message msg);
    }

    public static class WeakReferenceHandler extends Handler {
        WeakReference<HandlerUtils.Callback> callbackWeakReference;

        /**
         * 禁止传入匿名对象
         *
         * @param callback
         */
        public WeakReferenceHandler(HandlerUtils.Callback callback) {
            callbackWeakReference = new WeakReference<>(callback);
        }

        public WeakReferenceHandler(HandlerUtils.Callback callback, Looper looper) {
            super(looper);
            callbackWeakReference = new WeakReference<>(callback);
        }

        @Override
        public void handleMessage(Message msg) {
            HandlerUtils.Callback callback = callbackWeakReference.get();
            if (callback != null) {
                callback.handleMessage(msg);
            }
        }
    }
}