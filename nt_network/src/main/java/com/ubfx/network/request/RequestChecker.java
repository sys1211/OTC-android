package com.ubfx.network.request;


import com.ubfx.network.UBHttpError;

/**
 * Created by yangchuanzhe on 2019/2/27.
 */
public interface RequestChecker {

    public boolean check(String responseStr);

    public UBHttpError getError();
}
