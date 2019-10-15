package com.ubfx.network.request;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.ubfx.network.utils.GsonHelper;
import com.ubfx.log.LogUtils;
import com.ubfx.network.Http;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

public class ApiRequest<T> extends HttpRequestBase<T> {
    private Type type;
    private final UBSuccessListener<T> mListener;

    public ApiRequest(int method, String url, boolean requestJson, Class<T> classOfT, UBSuccessListener<T> listener, UBErrorListener errorListener) {
        this(method, url, requestJson, (Type) classOfT, listener, errorListener);
    }

    public ApiRequest(int method, String url, Class<T> classOfT, UBSuccessListener<T> listener, UBErrorListener errorListener) {
        this(method, url, true, classOfT, listener, errorListener);
    }

    public ApiRequest(int method, String url, Type type, UBSuccessListener<T> listener, UBErrorListener errorListener) {
        this(method, url, true, type, listener, errorListener);
    }

    public ApiRequest(int method, String url, boolean requestJson, Type type, UBSuccessListener<T> listener, UBErrorListener errorListener) {
        super(method, url, errorListener);
        this.type = type;
        mListener = listener;
        if (requestJson) {
            setContentType(Http.ContentTypes.JSON_UTF8);
        }
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response, String responseString) {
        Gson gson = GsonHelper.get();

        try {
            if (getRequestChecker() == null) {
                throw new JsonParseException("Request must has the request checker !!!");
            }

            if (getRequestChecker().check(responseString)) {

                Cache.Entry entry = HttpHeaderParser.parseCacheHeaders(response);

                JSONObject responseJson = new JSONObject(responseString);

                if (type == JSONObject.class) {
                    return Response.success((T) responseJson,
                            entry);
                }

                String jsonStr = "";
                Object dataObj = responseJson.opt("data");
                if (dataObj != null) {
                    jsonStr = dataObj.toString();
                }

                return Response.success(gson.<T>fromJson(jsonStr, type),
                        entry);
            }
            return Response.error(getRequestChecker().getError());
        } catch (JsonParseException | OutOfMemoryError | JSONException e) {
            LogUtils.e("Error when parsing response: " + responseString);
            return Response.error(new ParseError(e));
        }
    }


    @Override
    protected void deliverResponse(T t) {
        if (mListener != null) {
            mListener.onResponse(t);
        }
    }
}