package com.ubfx.network;

import android.content.Context;
import android.support.annotation.Nullable;

/**
 * Created by yangchuanzhe on 2019/9/6.
 */
public class UBParseError extends UBHttpError {
    @Override
    public String getErrorMessage(@Nullable Context context) {
        if (context != null) {
            return context.getResources().getString(R.string.api_error_parse);
        }
        return "";
    }
}
