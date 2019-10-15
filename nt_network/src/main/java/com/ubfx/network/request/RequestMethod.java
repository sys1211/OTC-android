package com.ubfx.network.request;

/**
 * Created by yangchuanzhe on 2019/9/6.
 *
 * same as Request.Method in volley
 */
public interface RequestMethod{
    int DEPRECATED_GET_OR_POST = -1;
    int GET = 0;
    int POST = 1;
    int PUT = 2;
    int DELETE = 3;
    int HEAD = 4;
    int OPTIONS = 5;
    int TRACE = 6;
    int PATCH = 7;
}
