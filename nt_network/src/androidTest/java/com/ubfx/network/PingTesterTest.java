package com.ubfx.network;

import android.support.test.runner.AndroidJUnit4;

import com.ubfx.network.ping.PingLevel;
import com.ubfx.network.ping.PingTestFinishListener;
import com.ubfx.network.ping.PingTester;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class PingTesterTest {
    @Test
    public void coreTest() throws InterruptedException {
        final Object sycObj = new Object();

        PingTester pingTester = new PingTester("s.ubfx.co.uk");
        pingTester.setListener(new PingTestFinishListener() {
            @Override
            public void onFinished(String host, PingLevel level, int delay) {

                assertEquals(host, "s.ubfx.co.uk");
                assertTrue(delay > 0);

                synchronized (sycObj) {
                    sycObj.notify();
                }
            }
        });
        pingTester.startPingTest();

        synchronized (sycObj) {
            sycObj.wait();
        }
    }
}
