package com.ubfx.library.view;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ubfx.library.R;
import com.ubfx.library.utils.DrawableUtils;
import com.ubfx.library.utils.ScreenUtils;


public class SearchEditText extends AppCompatEditText implements View.OnFocusChangeListener, TextWatcher {


    /**
     * 是否显示在左边
     */
    private boolean isShowNormal = false;
    /**
     * 是否点击软键盘搜索
     */
    private boolean pressSearch = false;

    private SearchContentChangedListener mContentChangeListener;

    private Drawable[] drawables; // 控件的图片资源
    private Drawable drawableLeft, drawableDel; // 搜索图标和删除按钮图标

    public void setShowNormal(boolean showNormal) {
        isShowNormal = showNormal;
    }

    public interface SearchContentChangedListener {
        void searchContentChanged(CharSequence text);
    }

    public void setContentChangeListener(SearchContentChangedListener contentChangeListener) {
        this.mContentChangeListener = contentChangeListener;
    }


    public CharSequence getSearchText() {
        return getText();
    }

    public SearchEditText(Context context) {
        this(context, null);
    }

    public SearchEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public SearchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {


        setHint(R.string.lib_search);

        setCompoundDrawablesWithIntrinsicBounds(R.drawable.lib_icon_search, 0, R.drawable.lib_icon_delete_gray, 0);


        setOnFocusChangeListener(this);
        addTextChangedListener(this);

        drawables = getCompoundDrawables();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (isShowNormal) { // 如果是默认样式，直接绘制
            if (length() < 1) {
                drawableDel = null;
            }
            this.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, drawableDel, null);
            super.onDraw(canvas);
        } else { // 如果不是默认样式，需要将图标绘制在中间
            drawableLeft = drawables[0];
            float textWidth = 0;
            if (!TextUtils.isEmpty(getHint())) {
                textWidth = getPaint().measureText(getHint().toString());
            }

            int drawablePadding = getCompoundDrawablePadding();
            int drawableWidth = drawableLeft.getIntrinsicWidth();

            float bodyWidth = textWidth + drawableWidth + drawablePadding;
            canvas.translate((getWidth() - bodyWidth - getPaddingLeft() - getPaddingRight()) / 2, 0);
            super.onDraw(canvas);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        // 被点击时，恢复默认样式
        if (!pressSearch && TextUtils.isEmpty(getText().toString())) {
            setShowNormal(hasFocus);
        }
    }


    /**
     * 当手指抬起的位置在clean的图标的区域 我们将此视为进行清除操作
     * getWidth():得到控件的宽度
     * event.getX():抬起时的坐标(改坐标是相对于控件本身而言的)
     * getTotalPaddingRight():clean的图标左边缘至控件右边缘的距离
     * getPaddingRight():clean的图标右边缘至控件右边缘的距离
     * 于是:
     * getWidth() - getTotalPaddingRight()表示: 控件左边到clean的图标左边缘的区域
     * getWidth() - getPaddingRight()表示: 控件左边到clean的图标右边缘的区域 所以这两者之间的区域刚好是clean的图标的区域
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                boolean isClean = (event.getX() > (getWidth() - getTotalPaddingRight()))
                        && (event.getX() < (getWidth() - getPaddingRight()));
                if (isClean) {
                    setText("");
                }
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }


    @Override
    public void afterTextChanged(Editable s) {
        if (this.length() < 1) {
            drawableDel = null;
        } else {
            drawableDel = this.getResources().getDrawable(R.drawable.lib_icon_delete_gray);
        }
        if (mContentChangeListener != null) {
            mContentChangeListener.searchContentChanged(s);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start,
                                  int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

}