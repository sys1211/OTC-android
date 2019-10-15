package com.ubfx.network;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ClearCacheRequest;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.Volley;
import com.ubfx.log.LogUtils;
import com.ubfx.network.request.HttpRequestBase;
import com.ubfx.network.utils.FileUtils;

import java.io.File;

public class RequestManager {

    private IRequestAgent requestAgent;

    private static volatile RequestManager instance = null;

    private static RequestQueue queue;

    public static RequestManager getInstance() {
        if (instance == null) {
            synchronized (RequestManager.class) {
                if (instance == null) {
                    instance = new RequestManager();
                }
            }
        }
        return instance;
    }

    public void setRequestAgent(IRequestAgent requestAgent) {
        this.requestAgent = requestAgent;
    }

    public void init(Context context) {
        if (queue == null) {
            queue = Volley
                    .newRequestQueue(context.getApplicationContext());
        }
    }

    /**
     * @param myRequest
     * @param tag       Activity or Fragment
     */
    public void addRequest(HttpRequestBase<?> myRequest, Object tag) {
        if (myRequest != null) {
            if (queue == null) {
                throw new RuntimeException("Request Manager must call init(context) first !!!");
            }
            if ((tag instanceof Activity || tag instanceof Fragment)) {
                myRequest.setTag(tag.toString());
            }

            if (requestAgent != null) {
                requestAgent.beforeRequestEnqueue(myRequest);
            }

            queue.add(myRequest);
        }
    }

    /**
     * 取消所有Request
     */
    public void cancelAllRequest() {
        RequestQueue localRequestQueue = queue;
        if (localRequestQueue != null) {
            localRequestQueue.cancelAll(new RequestQueue.RequestFilter() {

                @Override
                public boolean apply(Request<?> Request) {
                    return true;
                }
            });
            localRequestQueue.stop();
        }
    }

    /**
     * 根据标签取消指定Request
     *
     * @param tag
     */
    public void cancelRequestByTag(@NonNull Object tag) {
        if (tag instanceof Activity || tag instanceof Fragment) {
            cancelRequestByTag(tag.toString());
        }
    }

    /**
     * 根据标签取消指定Request
     *
     * @param tag
     */
    public void cancelRequestByTag(final String tag) {
        if (!TextUtils.isEmpty(tag)) {
            if (queue != null) {
                queue.cancelAll(new RequestQueue.RequestFilter() {

                    @Override
                    public boolean apply(Request<?> Request) {
                        return tag.equals(Request.getTag());
                    }
                });
            }
        }
    }

    public void clearCache(Context context) {
        if (queue == null) {
            return;
        }

        File cacheDir = new File(context.getCacheDir(), "volley");
        try {
            if (FileUtils.getFileSizes(cacheDir) > 1024 * 1024 * 10) {
                LogUtils.d("volley clear cache");
                DiskBasedCache cache = new DiskBasedCache(cacheDir);
                queue.add(new ClearCacheRequest(cache, null));
                queue.start();
            }
        } catch (Exception e) {
            LogUtils.e(e.getMessage());
        }
    }

}