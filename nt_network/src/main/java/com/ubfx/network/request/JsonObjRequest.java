package com.ubfx.network.request;

import org.json.JSONObject;

/**
 * Created by chuanzheyang on 17/1/25.
 */

public class JsonObjRequest extends ApiRequest<JSONObject> {

    private final UBSuccessListener<JSONObject> mListener;

    public JsonObjRequest(int method, String url, UBSuccessListener<JSONObject> mListener, UBErrorListener errorListener) {
        super(method, url, true, JSONObject.class, mListener, errorListener);
        this.mListener = mListener;
    }

    @Override
    protected void deliverResponse(JSONObject object) {
        if (mListener != null) {
            mListener.onResponse(object);
        }

    }
}
