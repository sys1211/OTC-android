package com.ubfx.network.request;

/**
 * Created by yangchuanzhe on 2019/9/6.
 */
public interface UBSuccessListener<T> {
    void onResponse(T result);
}