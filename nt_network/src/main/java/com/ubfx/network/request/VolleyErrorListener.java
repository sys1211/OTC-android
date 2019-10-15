package com.ubfx.network.request;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ubfx.network.UBHttpError;

/**
 * Created by yangchuanzhe on 2019/9/6.
 */
class VolleyErrorListener implements Response.ErrorListener {

    private UBErrorListener listener;

    VolleyErrorListener(UBErrorListener listener) {
        this.listener = listener;
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        if (this.listener != null) {
            this.listener.onErrorResponse(UBHttpError.wrap(volleyError));
        }
    }
}
