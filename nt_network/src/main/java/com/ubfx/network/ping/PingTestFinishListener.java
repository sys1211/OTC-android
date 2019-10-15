package com.ubfx.network.ping;

/**
 * Created by yangchuanzhe on 2018/6/27.
 */

public interface PingTestFinishListener {
    /**
     *
     * @param host host only , example www.google.com
     * @param level see PingLevel
     * @param delay out of service return -1 , otherwise ground truth.
     */
    void onFinished(String host, PingLevel level,int delay);
}
