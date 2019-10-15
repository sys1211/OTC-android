package com.ubfx.library.view;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by limeng on 2017/11/17.
 */

public class LongPressImageButton extends AppCompatImageButton {
    private Timer timer;

    private LongPressCallBack mCallBack;
    private Handler mHandler;

    public void setCallBack(LongPressCallBack callBack) {
        mCallBack = callBack;
    }

    public LongPressImageButton(Context context) {
        super(context);
        init();
    }

    public LongPressImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LongPressImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mHandler = new Handler();
    }

    private void startTimer() {
        if (mCallBack == null) {
            return;
        }

        if (timer != null) {
            timer.cancel();
        }
        long delay = mCallBack == null ? 1000 : mCallBack.getDelay();
        long period = mCallBack == null ? 100 : mCallBack.getPeriod();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                execute();
            }
        }, delay, period);
    }

    private void execute() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mCallBack != null) {
                    boolean finish = mCallBack.onDurationCallBack();
                    if (finish) {
                        cancelTimer();
                    }
                }
            }
        });
    }

    private void cancelTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            startTimer();
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            cancelTimer();
        }
        return super.onTouchEvent(event);
    }

    public interface LongPressCallBack {
        long getDelay();

        long getPeriod();

        boolean onDurationCallBack();
    }
}
