package com.ubfx.network;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

/**
 * Created by yangchuanzhe on 2019/2/22.
 */
public class UBHttpError extends VolleyError {

    private VolleyError innerError;

    public VolleyError getInnerError() {
        return innerError;
    }

    public UBHttpError() {
    }

    UBHttpError(VolleyError error) {
        this.innerError = error;
    }

    public static UBHttpError wrap(VolleyError error) {
        if (error instanceof UBHttpError) {
            return (UBHttpError) error;
        }
        return new UBHttpError(error);
    }

    public int getEventCode() {
        return -1;
    }

    /**
     * @param context
     * @return
     */
    public String getErrorMessage(@Nullable Context context) {
        if (context != null) {
            if (innerError instanceof ParseError) {
                return context.getString(R.string.api_error_parse);
            } else if (innerError instanceof TimeoutError) {
                return context.getString(R.string.api_error_timeout);
            } else if (innerError instanceof NoConnectionError) {
                return context.getString(R.string.api_error_no_connection);
            } else if (innerError instanceof AuthFailureError) {
                return context.getString(R.string.api_error_auth_failure) + getStatusCodeStr();
            } else if (innerError instanceof ClientError) {
                return context.getString(R.string.api_error_client) + getStatusCodeStr();
            } else if (innerError instanceof ServerError) {
                return context.getString(R.string.api_error_server) + getStatusCodeStr();
            } else if (innerError instanceof NetworkError) {
                return context.getString(R.string.api_error_network);
            }
        }
        return "";
    }

    private String getStatusCodeStr() {
        if (innerError.networkResponse == null) {
            return "";
        }
        return "(" + innerError.networkResponse.statusCode + ")";
    }
}
