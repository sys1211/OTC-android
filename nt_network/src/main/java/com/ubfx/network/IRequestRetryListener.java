package com.ubfx.network;

import com.ubfx.network.request.HttpRequestBase;

/**
 * Created by yangchuanzhe on 2019/3/19.
 */
public interface IRequestRetryListener {
    boolean blockRetry(HttpRequestBase request);

    void onRetry(UBFXRetryPolicy retryPolicy, HttpRequestBase request, UBHttpError volleyError);
}
