package com.ubfx.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;

import com.ubfx.library.R;
import com.ubfx.library.utils.ScreenUtils;


public class ClearEditText extends android.support.v7.widget.AppCompatEditText {

    private static final int ICON_CLEAR_DEFAULT = R.drawable.lib_icon_delete_gray;


    private Bitmap clearBitmap;

    private int clearPadding = 10;

    private int mInitPaddingRight;

    Paint bitmapPaint;

    private Rect clearRect;

    public ClearEditText(Context context) {
        super(context);
        init(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClearEditText);
        int iconClear =
                typedArray.getResourceId(R.styleable.ClearEditText_iconClear, ICON_CLEAR_DEFAULT);
        clearBitmap = ((BitmapDrawable)getResources().getDrawable(iconClear)).getBitmap();
        typedArray.recycle();
        bitmapPaint = new Paint();
        bitmapPaint.setAntiAlias(true);
        mInitPaddingRight = getPaddingRight();
        clearPadding = ScreenUtils.dp2px(context,6);
        if(isPasswordInputType(getInputType())){//fix hint font style different when input type password
            setTypeface(Typeface.DEFAULT);
            setTransformationMethod(new PasswordTransformationMethod());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (event.getX() - (getMeasuredWidth() - getPaddingRight()) >= 0) {
                setError(null);
                this.setText("");
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        drawBitmap(canvas, getRect(length() > 0));
        canvas.restore();
    }

    private void drawBitmap(Canvas canvas, Rect rect) {
        if (rect != null) {
            canvas.drawBitmap(clearBitmap, null, rect, bitmapPaint);
        }
    }

    private Rect getRect(boolean isShow) {
        setPadding(isShow);
        int left, top, right, bottom;
        right   = isShow ? getMeasuredWidth() + getScrollX() - clearPadding - clearPadding : 0;
        left    = isShow ? right - clearBitmap.getWidth() : 0;
        top     = isShow ? (getMeasuredHeight() - clearBitmap.getHeight())/2 : 0;
        bottom  = isShow ? top + clearBitmap.getHeight() : 0;
        if(clearRect == null){
            clearRect = new Rect(left, top, right, bottom);
        }else{
            clearRect.left = left;
            clearRect.top = top;
            clearRect.right = right;
            clearRect.bottom = bottom;
        }
        return clearRect;
    }

    private void setPadding(boolean isShow) {
        int paddingRight = mInitPaddingRight + (isShow ? clearBitmap.getWidth() + clearPadding + clearPadding : 0);
        setPadding(getPaddingLeft(), getPaddingTop(), paddingRight, getPaddingBottom());
    }


    private boolean isPasswordInputType(int inputType) {
        final int variation =
                inputType & (EditorInfo.TYPE_MASK_CLASS | EditorInfo.TYPE_MASK_VARIATION);
        return variation
                == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD)
                || variation
                == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_WEB_PASSWORD)
                || variation
                == (EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD);
    }

//    @Override
//    public void setRawInputType(int type) {
//        super.setRawInputType(type);
//        if(type == EditorInfo.TYPE_TEXT_VARIATION_PASSWORD){
//            setTypeface(Typeface.DEFAULT);
//            setTransformationMethod(new PasswordTransformationMethod());
//        }
//    }

//    @Override
//    public void setInputType(int type) {
//        if(type == EditorInfo.TYPE_TEXT_VARIATION_PASSWORD){
//            setTypeface(Typeface.DEFAULT);
//            setTransformationMethod(new PasswordTransformationMethod());
//            return;
//        }
//        super.setInputType(type);
//    }
}