package com.ubfx.network.request;

import com.ubfx.network.UBHttpError;

public interface UBErrorListener{
    void onErrorResponse(UBHttpError error);
}