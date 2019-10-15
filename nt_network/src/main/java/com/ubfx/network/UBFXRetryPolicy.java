package com.ubfx.network;

import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.ubfx.network.request.HttpRequestBase;


/**
 * Created by yangchuanzhe on 2019/3/19.
 */
public class UBFXRetryPolicy implements RetryPolicy {


    private int mCurrentTimeoutMs;
    private int mCurrentRetryCount;
    private final int mMaxNumRetries;
    private final float mBackoffMultiplier;

    private HttpRequestBase request;

    private IRequestRetryListener requestRetryListener;

    public void setRequestRetryListener(IRequestRetryListener requestRetryListener) {
        this.requestRetryListener = requestRetryListener;
    }

    public UBFXRetryPolicy(HttpRequestBase request) {
        this.request = request;
        int maxRetries = 2;

        if (requestRetryListener != null) {
            if (requestRetryListener.blockRetry(request)) {
                maxRetries = 0;
            }
        }

        this.mMaxNumRetries = maxRetries;
        this.mBackoffMultiplier = 1.0f;
        this.mCurrentTimeoutMs = 15 * 1000;
    }

    @Override
    public int getCurrentTimeout() {
        return mCurrentTimeoutMs;
    }

    @Override
    public int getCurrentRetryCount() {
        return mCurrentRetryCount;
    }

    @Override
    public void retry(VolleyError volleyError) throws VolleyError {
        if (requestRetryListener != null) {
            requestRetryListener.onRetry(this,request, UBHttpError.wrap(volleyError));
        }
        ++this.mCurrentRetryCount;
        this.mCurrentTimeoutMs = (int) ((float) this.mCurrentTimeoutMs + (float) this.mCurrentTimeoutMs * this.mBackoffMultiplier);
        if (!this.hasAttemptRemaining()) {
            throw volleyError;
        }
    }

    protected boolean hasAttemptRemaining() {
        return this.mCurrentRetryCount <= this.mMaxNumRetries;
    }
}
