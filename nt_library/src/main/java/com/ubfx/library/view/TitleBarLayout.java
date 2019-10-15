package com.ubfx.library.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ubfx.library.R;
import com.ubfx.library.UBFXLibraryConfig;
import com.ubfx.library.utils.DrawableUtils;
import com.ubfx.library.utils.ScreenUtils;

import java.util.LinkedList;

/**
 * Created by yinsheng on 16/8/22.
 */
public class TitleBarLayout extends ViewGroup implements View.OnClickListener {
    private static final int DEFAULT_MAIN_TEXT_SIZE = 17;
    private static final int DEFAULT_SUB_TEXT_SIZE = 12;
    private static final int DEFAULT_ACTION_TEXT_SIZE = 15;

    private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";

    private TextView mLeftText;
    private TextView mLeftSubText;
    private LinearLayout mRightLayout;
    private LinearLayout mCenterLayout;
    private TextView mCenterText;
    private TextView mSubTitleText;
    private View mCustomCenterView;
    private View mDividerView;

    private boolean mImmersive;

    private int mScreenWidth;
    private int mStatusBarHeight;
    private int mActionPadding;
    private int mOutPadding;
    @ColorInt
    private int mActionTextColor;
    private int mHeight;

    @ColorInt
    private int tintColor;

    public TitleBarLayout(Context context) {
        super(context);
        init(context, null);
    }

    public TitleBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TitleBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        if (mImmersive) {
            mStatusBarHeight = getStatusBarHeight();
        }
        mActionPadding = dip2px(5);
        mOutPadding = dip2px(8);
        mHeight = getResources().getDimensionPixelSize(R.dimen.lib_actionbar_height);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        mLeftText = new TextView(context);
        mLeftSubText = new TextView(context);
        mCenterLayout = new LinearLayout(context);
        mRightLayout = new LinearLayout(context);
        mDividerView = new View(context);

        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);

        mLeftText.setTextSize(DEFAULT_ACTION_TEXT_SIZE);
        mLeftText.setSingleLine();
        mLeftText.setGravity(Gravity.CENTER_VERTICAL);
        mLeftText.setPadding(mOutPadding + mActionPadding, 0, mOutPadding, 0);

        mLeftSubText.setTextSize(DEFAULT_ACTION_TEXT_SIZE);
        mLeftSubText.setSingleLine();
        mLeftSubText.setGravity(Gravity.CENTER_VERTICAL);
        mLeftSubText.setPadding(mOutPadding + mActionPadding, 0, mOutPadding, 0);
        mLeftSubText.setVisibility(GONE);


        mCenterText = new TextView(context);
        mSubTitleText = new TextView(context);
        mCenterLayout.addView(mCenterText);
        mCenterLayout.addView(mSubTitleText);

        mCenterLayout.setGravity(Gravity.CENTER);
        mCenterText.setTextSize(DEFAULT_MAIN_TEXT_SIZE);
        mCenterText.setSingleLine();
        mCenterText.setGravity(Gravity.CENTER);
        mCenterText.setEllipsize(TextUtils.TruncateAt.END);
        //设置粗体
        mCenterText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        mSubTitleText.setTextSize(DEFAULT_SUB_TEXT_SIZE);
        mSubTitleText.setSingleLine();
        mSubTitleText.setGravity(Gravity.CENTER);
        mSubTitleText.setEllipsize(TextUtils.TruncateAt.END);

        mRightLayout.setPadding(mOutPadding, 0, mOutPadding, 0);

        addView(mLeftText, layoutParams);
        addView(mLeftSubText, layoutParams);
        addView(mCenterLayout);
        addView(mRightLayout, layoutParams);
        addView(mDividerView, new LayoutParams(LayoutParams.MATCH_PARENT, 0));

        setTintColor(UBFXLibraryConfig.getTitleBackColor());

        TypedArray types = getContext().obtainStyledAttributes(attrs, R.styleable.TitleBarLayout);
        setTitle(types.getString(R.styleable.TitleBarLayout_TitleBar_title));
        setTitleSizePx(types.getDimension(R.styleable.TitleBarLayout_titleSize, getResources().getDimension(R.dimen.lib_text_18)));

        int titleColorRes = types.getResourceId(R.styleable.TitleBarLayout_titleColor, 0);
        int titleColor = titleColorRes == 0 ? UBFXLibraryConfig.getMainTextColor() : getResources().getColor(titleColorRes);
        setTitleColor(titleColor);

        if (types.hasValue(R.styleable.TitleBarLayout_leftIcon)) {
            int leftRes = types.getResourceId(R.styleable.TitleBarLayout_leftIcon, UBFXLibraryConfig.getTitleBackIcon());
            setLeftImageResource(leftRes);

            int leftIconColorRes = types.getResourceId(R.styleable.TitleBarLayout_leftIconColor, 0);
            int leftIconColor = leftIconColorRes == 0 ? UBFXLibraryConfig.getTitleBackColor() : getResources().getColor(leftIconColorRes);
            setLeftIconColor(leftIconColor);
        }


        int layoutId = types.getResourceId(R.styleable.TitleBarLayout_customCenterView, 0);
        if (layoutId > 0) {
            mCenterLayout = (LinearLayout) LayoutInflater.from(context).inflate(layoutId, null);
            addView(mCenterLayout);
        }

        setImmersive(types.getBoolean(R.styleable.TitleBarLayout_immersive, false));

        setDividerColor(types.getColor(R.styleable.TitleBarLayout_divider_color, Color.LTGRAY));
        if (types.hasValue(R.styleable.TitleBarLayout_divider_height)) {
            int dividerHeight = types.getDimensionPixelSize(R.styleable.TitleBarLayout_divider_height, 0);
            setDividerHeight(dividerHeight);
        }
        types.recycle();

    }

    public void setImmersive(boolean immersive) {
        mImmersive = immersive;
        if (mImmersive) {
            mStatusBarHeight = getStatusBarHeight();
        } else {
            mStatusBarHeight = 0;
        }
    }

    public void setHeight(int height) {
        mHeight = height;
        setMeasuredDimension(getMeasuredWidth(), mHeight);
    }

    public void setTintColor(@ColorInt int color) {
        if (this.tintColor != color) {
            this.tintColor = color;
            // color changed
            updateLeftImageTint();
            updateSubLeftImageTint();
            updateTitleDrawableRightTint();
            updateImageActionTint();
        }
    }

    /**
     * set back button drawable
     *
     * @param resId
     */
    public void setLeftImageResource(int resId) {
        Drawable drawable = null;
        if (resId != 0) {
            drawable = getResources().getDrawable(resId);
        }
        setLeftImageDrawable(drawable);
    }

    /**
     * set back button drawable
     *
     * @param resDrawable
     */
    public void setLeftImageDrawable(Drawable resDrawable) {
        mLeftText.setCompoundDrawablesWithIntrinsicBounds(resDrawable, null, null, null);
        mLeftText.setCompoundDrawablePadding(10);
        updateLeftImageTint();
    }

    private void updateLeftImageTint() {
        Drawable drawable = mLeftText.getCompoundDrawables()[0];
        if (drawable != null) {
            drawable = DrawableUtils.tint(drawable.mutate(), this.tintColor);
            mLeftText.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        }
    }


    /**
     * set sub left button drawable
     *
     * @param resId
     */
    public void setSubLeftImageResource(int resId) {
        Drawable drawable = null;
        if (resId != 0) {
            drawable = getResources().getDrawable(resId);
        }
        setSubLeftImageDrawable(drawable);
    }

    /**
     * set sub left button drawable
     *
     * @param drawable
     */
    public void setSubLeftImageDrawable(Drawable drawable) {
        mLeftSubText.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        mLeftSubText.setCompoundDrawablePadding(10);
        mLeftSubText.setVisibility(View.VISIBLE);
        updateSubLeftImageTint();
        requestLayout();
    }

    private void updateSubLeftImageTint() {
        Drawable drawable = mLeftSubText.getCompoundDrawables()[0];
        if (drawable != null) {
            drawable = DrawableUtils.tint(drawable.mutate(), this.tintColor);
            mLeftSubText.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        }
    }


    public void setTitleDrawableRight(@DrawableRes int resId) {
        Drawable drawable = null;
        if (resId != 0) {
            drawable = getResources().getDrawable(resId);
        }
        setTitleDrawableRight(drawable);
    }

    /**
     * set image button drawable right of title
     *
     * @param drawable
     */
    public void setTitleDrawableRight(Drawable drawable) {
        mCenterText.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        mCenterText.setCompoundDrawablePadding(ScreenUtils.dp2px(getContext(), 7));
        updateTitleDrawableRightTint();
    }

    private void updateTitleDrawableRightTint() {
        Drawable drawable = mCenterText.getCompoundDrawables()[2];
        if (drawable != null) {
            drawable = DrawableUtils.tint(drawable.mutate(), this.tintColor);
            mCenterText.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        }
    }

    public void setLeftIconColorRes(@ColorRes int colorRes) {
        int color = getResources().getColor(colorRes);
        setLeftIconColor(color);
    }

    /**
     * will set title tint color
     *
     * @param color
     */
    public void setLeftIconColor(@ColorInt int color) {
        Drawable[] drawables = mLeftText.getCompoundDrawables();
        if (drawables.length > 0 && drawables[0] != null) {
            Drawable d = DrawableUtils.tint(drawables[0].mutate(), color);
            setLeftImageDrawable(d);

            setTintColor(color);
        }
    }

    public void setLeftClickListener(OnClickListener l) {
        mLeftText.setOnClickListener(l);
    }

    public void setLeftSubClickListener(OnClickListener l) {
        mLeftSubText.setOnClickListener(l);
    }

    public void setLeftText(CharSequence title) {
        mLeftText.setText(title);
    }

    public void setLeftText(int resid) {
        mLeftText.setText(resid);
    }

    public void setLeftTextSize(float size) {
        mLeftText.setTextSize(size);
    }

    public void setLeftTextColor(int color) {
        mLeftText.setTextColor(color);
    }

    public void setLeftVisible(boolean visible) {
        mLeftText.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setTitle(CharSequence title) {
        if (title == null) {
            return;
        }
        int index = title.toString().indexOf("\n");
        if (index > 0) {
            setTitle(title.subSequence(0, index), title.subSequence(index + 1, title.length()), LinearLayout.VERTICAL);
        } else {
            index = title.toString().indexOf("\t");
            if (index > 0) {
                setTitle(title.subSequence(0, index), "  " + title.subSequence(index + 1, title.length()), LinearLayout.HORIZONTAL);
            } else {
                mCenterText.setText(title);
                mSubTitleText.setVisibility(View.GONE);
            }
        }
    }

    private void setTitle(CharSequence title, CharSequence subTitle, int orientation) {
        mCenterLayout.setOrientation(orientation);
        mCenterText.setText(title);

        mSubTitleText.setText(subTitle);
        mSubTitleText.setVisibility(View.VISIBLE);
    }

    public void setCenterClickListener(OnClickListener l) {
        mCenterLayout.setOnClickListener(l);
    }

    public void setTitle(int resid) {
        setTitle(getResources().getString(resid));
    }

    public void setTitleColorRes(@ColorRes int resid) {
        mCenterText.setTextColor(getResources().getColor(resid));
    }

    public void setTitleColor(@ColorInt int color) {
        mCenterText.setTextColor(color);
    }

    public void setTitleSize(float size) {
        mCenterText.setTextSize(size);
    }

    public void setTitleSizePx(float size) {
        mCenterText.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    public void setTitleBackground(int resid) {
        mCenterText.setBackgroundResource(resid);
    }

    public void setSubTitleColor(int resid) {
        mSubTitleText.setTextColor(resid);
    }

    public void setSubTitleSize(float size) {
        mSubTitleText.setTextSize(size);
    }

    public void setCustomTitle(View titleView) {
        if (titleView == null) {
            mCenterText.setVisibility(View.VISIBLE);
            if (mCustomCenterView != null) {
                mCenterLayout.removeView(mCustomCenterView);
            }

        } else {
            if (mCustomCenterView != null) {
                mCenterLayout.removeView(mCustomCenterView);
            }
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            mCustomCenterView = titleView;
            mCenterLayout.addView(titleView, layoutParams);
            mCenterText.setVisibility(View.GONE);
        }
    }

    public void setDivider(Drawable drawable) {
        mDividerView.setBackgroundDrawable(drawable);
    }

    public void setDividerColor(int color) {
        mDividerView.setBackgroundColor(color);
    }

    public void setDividerHeight(int dividerHeight) {
        mDividerView.getLayoutParams().height = dividerHeight;
    }

    public void setActionTextColor(@ColorInt int colorResId) {
        mActionTextColor = colorResId;
    }


    /**
     * Function to set a click listener for Title TextView
     *
     * @param listener the onClickListener
     */
    public void setOnTitleClickListener(OnClickListener listener) {
        mCenterText.setOnClickListener(listener);
    }

    @Override
    public void onClick(View view) {
        final Object tag = view.getTag();
        if (tag instanceof Action) {
            final Action action = (Action) tag;
            action.performAction(view);
        }
    }

    private void updateImageActionTint() {
        int count = mRightLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = mRightLayout.getChildAt(count);
            if (view instanceof ImageView) {
                Drawable drawable = ((ImageView) view).getDrawable();
                if (drawable != null) {
                    drawable = DrawableUtils.tint(drawable, tintColor);
                    ((ImageView) view).setImageDrawable(drawable);
                }
            }
        }
    }

    /**
     * Adds a list of {@link Action}s.
     *
     * @param actionList the actions to add
     */
    public void addActions(ActionList actionList) {
        int actions = actionList.size();
        for (int i = 0; i < actions; i++) {
            addAction(actionList.get(i));
        }
    }

    /**
     * Adds a new {@link Action}.
     *
     * @param action the action to add
     */
    public View addAction(Action action) {
        final int index = mRightLayout.getChildCount();
        return addAction(action, index);
    }

    /**
     * Adds a new {@link Action} at the specified index.
     *
     * @param action the action to add
     * @param index  the position at which to add the action
     */
    public View addAction(Action action, int index) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);
        View view = inflateAction(action);
        mRightLayout.addView(view, index, params);
        return view;
    }

    /**
     * Removes all action views from this action bar
     */
    public void removeAllActions() {
        mRightLayout.removeAllViews();
    }

    /**
     * Remove a action from the action bar.
     *
     * @param index position of action to remove
     */
    public void removeActionAt(int index) {
        mRightLayout.removeViewAt(index);
    }

    /**
     * Remove a action from the action bar.
     *
     * @param action The action to remove
     */
    public void removeAction(Action action) {
        int childCount = mRightLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = mRightLayout.getChildAt(i);
            if (view != null) {
                final Object tag = view.getTag();
                if (tag instanceof Action && tag.equals(action)) {
                    mRightLayout.removeView(view);
                }
            }
        }
    }

    /**
     * Returns the number of actions currently registered with the action bar.
     *
     * @return action count
     */
    public int getActionCount() {
        return mRightLayout.getChildCount();
    }

    /**
     * Inflates a {@link View} with the given {@link Action}.
     *
     * @param action the action to inflate
     * @return a view
     */
    private View inflateAction(Action action) {
        View view;
        int cusLayoutId = action.getCustomLayoutId();
        if (cusLayoutId > 0) {
            view = LayoutInflater.from(getContext()).inflate(cusLayoutId, mRightLayout, false);
        } else if (TextUtils.isEmpty(action.getText())) {
            AppCompatImageView img = new AppCompatImageView(getContext());
            Drawable drawable = DrawableUtils.tint(action.getDrawable().mutate(), this.tintColor);
            img.setImageDrawable(drawable);
            view = img;
        } else {
            AppCompatTextView text = new AppCompatTextView(getContext());
            text.setGravity(Gravity.CENTER);
            text.setText(action.getText());
            text.setTextSize(DEFAULT_ACTION_TEXT_SIZE);
            if (mActionTextColor != 0) {
                text.setTextColor(mActionTextColor);
            }
            view = text;
        }

        view.setPadding(mActionPadding, 0, mActionPadding, 0);
        view.setTag(action);
        view.setOnClickListener(this);
        return view;
    }

    public View getViewByAction(Action action) {
        return findViewWithTag(action);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height;
        if (heightMode != MeasureSpec.EXACTLY) {
            height = mHeight + mStatusBarHeight;
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY);
        } else {
            height = MeasureSpec.getSize(heightMeasureSpec) + mStatusBarHeight;
        }

        measureChild(mLeftText, widthMeasureSpec, heightMeasureSpec);
        measureChild(mLeftSubText, widthMeasureSpec, heightMeasureSpec);
        measureChild(mRightLayout, widthMeasureSpec, heightMeasureSpec);
        if (mLeftText.getMeasuredWidth() + mLeftSubText.getMeasuredWidth() > mRightLayout.getMeasuredWidth()) {
            mCenterLayout.measure(
                    MeasureSpec.makeMeasureSpec(mScreenWidth - 2 * (mLeftText.getMeasuredWidth() + mLeftSubText.getMeasuredWidth()), MeasureSpec.EXACTLY)
                    , heightMeasureSpec);
        } else {
            mCenterLayout.measure(
                    MeasureSpec.makeMeasureSpec(mScreenWidth - 2 * mRightLayout.getMeasuredWidth(), MeasureSpec.EXACTLY)
                    , heightMeasureSpec);
        }
        measureChild(mDividerView, widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mLeftText.layout(0, mStatusBarHeight, mLeftText.getMeasuredWidth(), mLeftText.getMeasuredHeight() + mStatusBarHeight);
        mLeftSubText.layout(mLeftText.getMeasuredWidth(),
                mStatusBarHeight,
                mLeftSubText.getMeasuredWidth() + mLeftText.getMeasuredWidth(),
                mLeftSubText.getMeasuredHeight() + mStatusBarHeight);
        mRightLayout.layout(mScreenWidth - mRightLayout.getMeasuredWidth(), mStatusBarHeight,
                mScreenWidth, mRightLayout.getMeasuredHeight() + mStatusBarHeight);
        if ((mLeftText.getMeasuredWidth() + mLeftSubText.getMeasuredWidth()) > mRightLayout.getMeasuredWidth()) {
            mCenterLayout.layout((mLeftText.getMeasuredWidth() + mLeftSubText.getMeasuredWidth()), mStatusBarHeight,
                    mScreenWidth - (mLeftText.getMeasuredWidth() + mLeftSubText.getMeasuredWidth()), getMeasuredHeight());
        } else {
            mCenterLayout.layout(mRightLayout.getMeasuredWidth(), mStatusBarHeight,
                    mScreenWidth - mRightLayout.getMeasuredWidth(), getMeasuredHeight());
        }
        mDividerView.layout(0, getMeasuredHeight() - mDividerView.getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight());
    }

    public static int dip2px(int dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 计算状态栏高度高度
     * getStatusBarHeight
     *
     * @return
     */
    public static int getStatusBarHeight() {
        return getInternalDimensionSize(Resources.getSystem(), STATUS_BAR_HEIGHT_RES_NAME);
    }

    private static int getInternalDimensionSize(Resources res, String key) {
        int result = 0;
        int resourceId = res.getIdentifier(key, "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }


    /**
     * A {@link LinkedList} that holds a list of {@link Action}s.
     */
    @SuppressWarnings("serial")
    public static class ActionList extends LinkedList<Action> {
    }

    /**
     * Definition of an action that could be performed, along with a icon to
     * show.
     */
    public interface Action {
        String getText();

        Drawable getDrawable();

        void performAction(View view);

        int getCustomLayoutId();
    }

    public static abstract class ImageAction implements Action {
        private Drawable mDrawable;

        public ImageAction(Context context, @DrawableRes int drawable) {
            mDrawable = context.getResources().getDrawable(drawable);
        }

        public ImageAction(Drawable drawable) {
            mDrawable = drawable;
        }

        @Override
        public Drawable getDrawable() {
            return mDrawable;
        }

        @Override
        public String getText() {
            return null;
        }

        @Override
        public int getCustomLayoutId() {
            return 0;
        }
    }

    public static abstract class TextAction implements Action {
        final private String mText;

        public TextAction(String text) {
            mText = text;
        }

        @Override
        public Drawable getDrawable() {
            return null;
        }

        @Override
        public String getText() {
            return mText;
        }

        @Override
        public int getCustomLayoutId() {
            return 0;
        }
    }

    public static abstract class CustomLayoutAction implements Action {
        private int mCusLayoutId;

        public CustomLayoutAction(int layoutId) {
            mCusLayoutId = layoutId;
        }

        @Override
        public Drawable getDrawable() {
            return null;
        }

        @Override
        public String getText() {
            return null;
        }

        @Override
        public int getCustomLayoutId() {
            return mCusLayoutId;
        }
    }


}
