package com.ubfx.network.ping;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.ubfx.log.LogUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Created by yangchuanzhe on 2018/6/26.
 */


public class PingTester {

    private String domain;
    private int pingCount;

    private PingTestFinishListener mListener;

    public PingTester(@NonNull String domain) {
        this(domain, 5);
    }

    public PingTester(@NonNull String domain, int count) {
        this.domain = domain;
        this.pingCount = Math.max(1, count);
    }

    public void setListener(PingTestFinishListener mListener) {
        this.mListener = mListener;
    }

    public void startPingTest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PingResult result = ping();
                if (result.result.contains("fail")) {
                    callback(domain, PingLevel.outOfService, -1);
                } else {
                    PingLevel level = PingLevel.perfect;
                    float packetLoss = getPacketLossFloat(result.result);
                    if (packetLoss > 0 && packetLoss < 70) {
                        level = PingLevel.poor;
                    } else if (packetLoss >= 70 || packetLoss < 0) {
                        level = PingLevel.outOfService;
                    }

                    callback(domain, level, result.timeAverage);
                }
            }
        }).start();
    }

    private void callback(final String domain, final PingLevel level, final int delay) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (mListener != null) {
                    mListener.onFinished(domain, level, delay);
                }
            }
        });
    }


    static class PingResult {
        String result;
        int timeAverage = -1;

        public PingResult(String result) {
            this.result = result;
        }

        public PingResult(String result, int timeAverage) {
            this.result = result;
            if (timeAverage <= 0) {
                this.timeAverage = -1;
            } else {
                this.timeAverage = timeAverage;
            }
        }
    }

    private PingResult ping() {
        if (TextUtils.isEmpty(domain)) {
            return new PingResult("ping fail");
        }
        String line = null;
        Process process = null;
        BufferedReader successReader = null;
        int pingCount = this.pingCount;
        // -c 设置完成要求回应的次数 -i 指定收发信息的间隔时间
        String command = "ping -c " + pingCount + " -i 0.2 " + domain;
        StringBuffer resultBuffer = new StringBuffer();
        int delayAmount = 0;
        try {
            process = Runtime.getRuntime().exec(command);
            if (process == null) {
                LogUtils.e("ping fail:process is null.");
                append(resultBuffer, "ping fail:process is null.");
                return new PingResult(resultBuffer.toString());
            }
            successReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = successReader.readLine()) != null) {
                LogUtils.d(line);
                append(resultBuffer, line);
                String time;
                if ((time = getTime(line)) != null) {
                    LogUtils.d("ping time " + time + " MS");
                    try {
                        delayAmount += Double.valueOf(time);
                    } catch (Exception e) {
                    }
                }
            }
            int status = process.waitFor();
            if (status == 0) {
                LogUtils.d("exec cmd success:" + command);
                append(resultBuffer, "exec cmd success:" + command);
            } else {
                LogUtils.d("exec cmd fail.");
                append(resultBuffer, "exec cmd fail.");
            }
            LogUtils.d("exec finished.");
        } catch (IOException e) {
            LogUtils.e(e.getMessage());
        } catch (InterruptedException e) {
            LogUtils.e(e.getMessage());
        } finally {
            LogUtils.d("ping exit.");
            if (process != null) {
                process.destroy();
            }
            if (successReader != null) {
                try {
                    successReader.close();
                } catch (IOException e) {
                    LogUtils.e(e.getMessage());
                }
            }
        }
        LogUtils.d(resultBuffer.toString());
        return new PingResult(resultBuffer.toString(), delayAmount / pingCount);
    }

    private void append(StringBuffer stringBuffer, String text) {
        if (stringBuffer != null) {
            stringBuffer.append(text + "\n");
        }
    }

    private String getTime(String line) {
        String[] lines = line.split("\n");
        String time = null;
        for (String l : lines) {
            if (!l.contains("time="))
                continue;
            int index = l.indexOf("time=");
            time = l.substring(index + "time=".length());
            time = String.valueOf(new Scanner(time).nextDouble());
        }
        return time;
    }

    private float getPacketLossFloat(String pingResult) {
        String packetLossInfo = getPacketLoss(pingResult);
        if (null != packetLossInfo) {
            try {
                return Float.valueOf(packetLossInfo.replace("%", ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    private String getPacketLoss(String pingResult) {
        if (null != pingResult) {
            try {
                String tempInfo = pingResult.substring(pingResult.indexOf("received,"));
                return tempInfo.substring(9, tempInfo.indexOf("packet"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private int getAvgRTT(String pingResult) {
        if (null != pingResult) {
            try {
                String tempInfo = pingResult.substring(pingResult.indexOf("min/avg/max/mdev") + 19);
                String[] temps = tempInfo.split("/");
                return Math.round(Float.valueOf(temps[1]));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    private String getIP(String pingResult) {
        if (null != pingResult) {
            try {
                String tempInfo = pingResult.substring(pingResult.indexOf("from") + 5);
                return tempInfo.substring(0, tempInfo.indexOf("icmp_seq") - 2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
