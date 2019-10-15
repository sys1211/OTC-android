package com.ubfx.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;

import com.ubfx.library.R;

/**
 * Created by zhouzhou on 2018/2/5.
 */

public class DashView extends View {
    private static final String TAG = "DashView";
    public static final int DEFAULT_DASH_WIDTH = 100;
    public static final int DEFAULT_LINE_WIDTH = 100;
    public static final int DEFAULT_LINE_HEIGHT = 10;
    public static final int DEFAULT_LINE_COLOR = 0x9E9E9E;

    /**
     * 虚线的方向
     */
    public static final int ORIENTATION_HORIZONTAL = 0;
    public static final int ORIENTATION_VERTICAL = 1;
    /**
     * 默认为水平方向
     */
    public static final int DEFAULT_DASH_ORIENTATION = ORIENTATION_HORIZONTAL;
    /**
     * 间距宽度
     */
    private float dashWidth;
    /**
     * 线段高度
     */
    private float lineHeight;
    /**
     * 线段宽度
     */
    private float lineWidth;
    /**
     * 线段颜色
     */
    private int lineColor;
    private int dashOrientation;

    private Paint mPaint;
    private int widthSize;
    private int heightSize;

    private Path linePath = new Path();

    public DashView(Context context) {
        this(context, null);
    }

    public DashView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DashView);
        dashWidth = typedArray.getDimension(R.styleable.DashView_dashWidth, DEFAULT_DASH_WIDTH);
        lineHeight = typedArray.getDimension(R.styleable.DashView_lineHeight, DEFAULT_LINE_HEIGHT);
        lineWidth = typedArray.getDimension(R.styleable.DashView_lineWidth, DEFAULT_LINE_WIDTH);
        lineColor = typedArray.getColor(R.styleable.DashView_lineColor, DEFAULT_LINE_COLOR);
        dashOrientation = typedArray.getInteger(R.styleable.DashView_dashOrientation, DEFAULT_DASH_ORIENTATION);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(lineColor);
        mPaint.setStrokeWidth(lineHeight);

        DashPathEffect pathEffect = new DashPathEffect(new float[]{lineWidth, dashWidth}, 1);

        mPaint.setPathEffect(pathEffect);

        typedArray.recycle();
    }

    public void setLineColor(@ColorInt int lineColor) {
        if (this.lineColor == lineColor) {
            return;
        }
        this.lineColor = lineColor;
        if (mPaint != null) {
            mPaint.setColor(lineColor);
        }
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        widthSize = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        heightSize = MeasureSpec.getSize(heightMeasureSpec - getPaddingTop() - getPaddingBottom());
        if (dashOrientation == ORIENTATION_HORIZONTAL) {
            //不管在布局文件中虚线高度设置为多少，虚线的高度统一设置为实体线段的高度
            setMeasuredDimension(widthSize, (int) lineHeight);
        } else {
            setMeasuredDimension((int) lineHeight, heightSize);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (dashOrientation) {
            case ORIENTATION_VERTICAL:
                drawVerticalLine(canvas);
                break;
            default:
                drawHorizontalLine(canvas);
        }
    }

    /**
     * 画水平方向虚线
     *
     * @param canvas
     */
    public void drawHorizontalLine(Canvas canvas) {
        canvas.save();
        linePath.reset();
        linePath.moveTo(getPaddingLeft(), getPaddingTop());
        linePath.lineTo(getWidth() - getPaddingLeft() - getPaddingRight(), getPaddingTop());
        canvas.drawPath(linePath, mPaint);
        canvas.restore();
    }

    /**
     * 画竖直方向虚线
     *
     * @param canvas
     */
    public void drawVerticalLine(Canvas canvas) {
        canvas.save();
        linePath.reset();
        linePath.moveTo(getPaddingLeft(), getPaddingTop());
        linePath.lineTo(getPaddingLeft(), getHeight() - getPaddingTop() - getPaddingBottom());
        canvas.drawPath(linePath, mPaint);
        canvas.restore();
    }
}
