package com.ubfx.network;


import com.ubfx.network.request.HttpRequestBase;

/**
 * Created by yangchuanzhe on 2019/2/27.
 */
public interface IRequestAgent {
    public void beforeRequestEnqueue(HttpRequestBase requestBase);
}
