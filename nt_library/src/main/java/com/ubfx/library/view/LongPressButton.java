package com.ubfx.library.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by limeng on 2017/11/17.
 */

public class LongPressButton extends AppCompatButton {
    private Timer timer;

    private LongPressCallBack mCallBack;
    private Handler mHandler;

    private Drawable mDrawable;

    public void setCallBack(LongPressCallBack callBack) {
        mCallBack = callBack;
    }

    public void setDrawable(Drawable mDrawable) {
        this.mDrawable = mDrawable;
        invalidate();
    }

    public LongPressButton(Context context) {
        super(context);
        init();
    }

    public LongPressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LongPressButton(Context context, AttributeSet attrs, int defStyleAttr) {
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDrawable != null) {
            canvas.translate((float)((getWidth() - mDrawable.getIntrinsicWidth()) / 2.0),(float)((getHeight() - mDrawable.getIntrinsicHeight()) / 2.0));
            mDrawable.draw(canvas);
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

    public static class LongPressCommCallBack implements LongPressCallBack {
        @Override
        public long getDelay() {
            return 500;
        }

        @Override
        public long getPeriod() {
            return 100;
        }

        @Override
        public boolean onDurationCallBack() {
            return true;
        }
    }
}
