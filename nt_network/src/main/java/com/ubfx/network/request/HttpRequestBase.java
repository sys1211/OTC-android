package com.ubfx.network.request;


import android.net.Uri;
import android.text.TextUtils;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.ubfx.log.LogUtils;
import com.ubfx.network.Http;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public abstract class HttpRequestBase<T> extends com.android.volley.Request<T> {

    // Take over control of url from super class.
    private String mUrl;
    private Map<String, String> mHeaders = new HashMap<>();
    private Map<String, String> mParams = new HashMap<>();
    private Map<String, Object> mJsonParams = new HashMap<>();
    private String mParamsEncoding = Http.Charsets.UTF8;
    private String mContentType = Http.ContentTypes.FORM_UTF8;
    private Priority mPriority = Priority.NORMAL;

    private RequestWatcher requestWatcher;
    private RequestChecker requestChecker;

    public RequestChecker getRequestChecker() {
        return requestChecker;
    }

    public void setRequestChecker(RequestChecker requestChecker) {
        this.requestChecker = requestChecker;
    }

    public RequestWatcher getRequestWatcher() {
        return requestWatcher;
    }

    public void setRequestWatcher(RequestWatcher interceptor) {
        this.requestWatcher = interceptor;
    }

    public HttpRequestBase(int method, String url, final UBErrorListener listener) {
        super(method, url, new VolleyErrorListener(listener));
        mUrl = url;
    }

    @Override
    public String getUrl() {
        return mUrl;
    }

    @Override
    public void addMarker(String tag) {
        super.addMarker(tag);
        // TODO can not find request start position.
//        LogUtils.e("url : " + getUrl() + " tag : " + tag);
        if (!"add-to-queue".equals(tag)) {
            return;
        }
        if (getRequestWatcher() != null) {
            getRequestWatcher().onStart(this);
        }
    }

    @Override
    final protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
        if (getRequestWatcher() != null) {
            getRequestWatcher().onResponse(this, networkResponse);
        }
        String responseString;
        try {
            responseString = new String(networkResponse.data,
                    HttpHeaderParser.parseCharset(networkResponse.headers));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
        LogUtils.d(getUrl() + " \n parseNetworkResponse : " + responseString);

        //LogUtils.d(getUrl() + " \n networkTimeMs : " + networkResponse.networkTimeMs);

        return parseNetworkResponse(networkResponse, responseString);
    }

    protected abstract Response<T> parseNetworkResponse(NetworkResponse networkResponse, String responseString);

    @Override
    public String getParamsEncoding() {
        return mParamsEncoding;
    }

    public void setParamsEncoding(String paramsEncoding) {
        mParamsEncoding = paramsEncoding;
    }

    @Override
    public String getCacheKey() {
        return getMethod() + ":" + mUrl;
    }

    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    public HttpRequestBase<T> addHeader(String name, String value) {
        if (!TextUtils.isEmpty(value)) {
            mHeaders.put(name, value);
        }
        return this;
    }

    public HttpRequestBase<T> addHeader(Map.Entry<String, String> header) {
        return addHeader(header.getKey(), header.getValue());
    }

    public HttpRequestBase<T> addHeaderAccept(String contentType) {
        return addHeader(Http.Headers.ACCEPT, contentType);
    }

    public HttpRequestBase<T> addHeaderAcceptWithCharset(String contentType, String charset) {
        return addHeaderAccept(Http.ContentTypes.withCharset(contentType, charset));
    }

    public HttpRequestBase<T> addHeaderAcceptJson(String charset) {
        return addHeaderAcceptWithCharset(Http.ContentTypes.JSON, charset);
    }

    public HttpRequestBase<T> addHeaderAcceptJsonUtf8() {
        return addHeaderAcceptJson(Http.Charsets.UTF8);
    }

    public HttpRequestBase<T> addHeaderAcceptCharset(String charset) {
        return addHeader(Http.Headers.ACCEPT_CHARSET, charset);
    }

    public HttpRequestBase<T> addHeaderAcceptCharsetUtf8() {
        return addHeaderAcceptCharset(Http.Charsets.UTF8);
    }

    public HttpRequestBase<T> addHeaderAcceptEncoding(String encoding) {
        return addHeader(Http.Headers.ACCEPT_ENCODING, encoding);
    }

    public HttpRequestBase<T> addHeaderAuthorization(String authorization) {
        return addHeader(Http.Headers.AUTHORIZATION, authorization);
    }

    public HttpRequestBase<T> addHeaderAuthorizationBearer(String token) {
        return addHeaderAuthorization(Http.Headers.makeBearerAuthorization(token));
    }

    public HttpRequestBase<T> addHeaderUserAgent(String userAgent) {
        return addHeader(Http.Headers.USER_AGENT, userAgent);
    }

    public HttpRequestBase<T> addHeaders(Map<String, String> headers) {
        mHeaders.putAll(headers);
        return this;
    }

    public HttpRequestBase<T> removeHeader(String name) {
        mHeaders.remove(name);
        return this;
    }

    public HttpRequestBase<T> clearHeaders() {
        mHeaders.clear();
        return this;
    }

    public HttpRequestBase<T> setHeaders(Map<String, String> headers) {
        return clearHeaders().addHeaders(headers);
    }

    @Override
    public Map<String, String> getParams() {
        return mParams;
    }

    public HttpRequestBase<T> addParam(String name, String value) {
        mParams.put(name, value);
        return this;
    }

    public HttpRequestBase<T> addJsonParam(String name, Object value) {
        mJsonParams.put(name, value);
        return this;
    }

    public HttpRequestBase<T> addAllJsonParam(Map<String, Object> params) {
        mJsonParams.putAll(params);
        return this;
    }

    public HttpRequestBase<T> addParam(Map.Entry<String, String> param) {
        return addParam(param.getKey(), param.getValue());
    }

    public HttpRequestBase<T> addParams(Map<String, String> params) {
        mParams.putAll(params);
        return this;
    }

    public HttpRequestBase<T> removeParam(String name) {
        mParams.remove(name);
        return this;
    }

    public HttpRequestBase<T> clearParams() {
        mParams.clear();
        return this;
    }

    public HttpRequestBase<T> setParams(Map<String, String> params) {
        return clearParams().addParams(params);
    }


    public String getContentType() {
        return mContentType;
    }

    public HttpRequestBase<T> setContentType(String contentType) {
        mContentType = contentType;
        return this;
    }

    @Override
    public String getBodyContentType() {
        return getContentType();
    }

    @Override
    public byte[] getBody() {
        int method = getMethod();
        if ((method == Method.POST || method == Method.PUT)) {
            if (getContentType().equals(Http.ContentTypes.FORM_UTF8) && !mParams.isEmpty()) {
                return encodeParams();
            } else if (getContentType().equals(Http.ContentTypes.JSON_UTF8) && !mJsonParams.isEmpty()) {
                return getJsonParams();
            }
        }
        return new byte[]{};
    }

    @Override
    public Priority getPriority() {
        return mPriority;
    }

    public void setPriority(Priority priority) {
        mPriority = priority;
    }

    public void onPreparePerformRequest() {
    }

    private byte[] getJsonParams() {
        JSONObject obj = new JSONObject();
        try {
            for (Map.Entry<String, Object> entry : mJsonParams.entrySet()) {
                if (entry.getValue() instanceof Map) {
                    obj.put(entry.getKey(), new JSONObject((Map) entry.getValue()));
                } else {
                    obj.put(entry.getKey(), entry.getValue());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            return obj.toString().getBytes(mParamsEncoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private StringBuilder appendEncodedParams(StringBuilder builder) {
        for (Map.Entry<String, String> entry : mParams.entrySet()) {
            builder
                    .append(Uri.encode(entry.getKey()))
                    .append('=')
                    .append(Uri.encode(entry.getValue()))
                    .append('&');
        }
        return builder;
    }

    // TODO: Cache this?
    private String getUrlWithParams() {
        StringBuilder builder = new StringBuilder(mUrl)
                .append('?');
        return appendEncodedParams(builder)
                .toString();
    }

    private byte[] encodeParams() {
        try {
            return appendEncodedParams(new StringBuilder())
                    .toString()
                    .getBytes(mParamsEncoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param oldHost
     * @param newHttpHost
     * @return return true if url replaced.
     */
    public boolean replaceUrlHost(String oldHost, String newHttpHost) {
        // check whether replace request url or not.
        if (!TextUtils.isEmpty(oldHost) && !TextUtils.isEmpty(newHttpHost) && oldHost.equals(newHttpHost)) {
            return false;
        }
        String url = mUrl;
        if (!TextUtils.isEmpty(url)) {
            Uri uri = Uri.parse(url);
            if (uri != null) {
                String requestHost = uri.getHost();
                if (!TextUtils.isEmpty(requestHost) && requestHost.equals(oldHost)) {
                    if (!requestHost.equals(newHttpHost)) {
                        String newUrl = url.replace(requestHost, newHttpHost);
                        uri = Uri.parse(newUrl);
                        if (uri != null) {
                            mUrl = newUrl;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}