package com.ubfx.library.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.ubfx.library.utils.HandlerUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by chuanzheyang on 17/2/14.
 */

public class VerifyButton extends AppCompatButton implements HandlerUtils.Callback {

    public final static int MSG_COUNT_DOWN_GET_VERIFY_CODE = 3000;
    public final static int COUNT_DOWN_SECONDS = 60;
    protected Timer mCountDownTimer;
    protected int mLeftSeconds = COUNT_DOWN_SECONDS;

    private String normalText;

    public VerifyButton(Context context) {
        super(context);
        init();
    }

    public VerifyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VerifyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setTextSize(17);
    }


    private Handler weakReferenceHandler;

    public synchronized Handler getActivityHandler() {
        if (weakReferenceHandler == null) {
            weakReferenceHandler = new HandlerUtils.WeakReferenceHandler(this, Looper.getMainLooper());
        }
        return weakReferenceHandler;
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.what == MSG_COUNT_DOWN_GET_VERIFY_CODE && mCountDownTimer != null) {
            mLeftSeconds--;

            if (mLeftSeconds <= 0) {
                stopCountingTimer();
                onCountDownFinish();
            } else {
                String text = mLeftSeconds + "";
                setText(text);
            }
        }
    }

    public void finish() {
        stopCountingTimer();
        onCountDownFinish();
    }

    private void onCountDownFinish() {
        mLeftSeconds = COUNT_DOWN_SECONDS;
        setEnabled(true);
        setText(normalText);
    }

    public void start() {
        normalText = getText().toString();
        setEnabled(false);

        stopCountingTimer();
        mCountDownTimer = new Timer();
        mCountDownTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivityHandler().sendEmptyMessage(MSG_COUNT_DOWN_GET_VERIFY_CODE);
            }
        }, 100, 1000);
    }

    private void stopCountingTimer() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }

}
