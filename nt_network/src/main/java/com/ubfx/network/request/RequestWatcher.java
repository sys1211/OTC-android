package com.ubfx.network.request;

import com.android.volley.NetworkResponse;

/**
 * Created by yangchuanzhe on 2019/2/27.
 */
public interface RequestWatcher {
    public void onStart(HttpRequestBase requestBase);

    public void onResponse(HttpRequestBase requestBase, NetworkResponse networkResponse);
}
