package com.ubfx.library.view.recycler;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * RecyclerView分割条
 */
public class DividerDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    private Drawable mDivider;

    private Context context;

    private int heightPx = 1;

    public DividerDecoration(Context context) {
        this.context = context;
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    public DividerDecoration heightPx(int heightPx){
        this.heightPx = heightPx;
        return this;
    }

    public DividerDecoration withDivider(@DrawableRes int resId) {
        if (resId != 0) mDivider = ContextCompat.getDrawable(context, resId);
        return this;
    }

    public DividerDecoration withDivider(Drawable d) {
        if (d != null) mDivider = d;
        return this;
    }

    private int getOrientation(RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).getOrientation();
        }
        return VERTICAL_LIST;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        if (getOrientation(parent) == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int recyclerViewTop = parent.getPaddingTop();
        final int recyclerViewBottom = parent.getHeight() - parent.getPaddingBottom();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = Math.max(recyclerViewTop, child.getBottom() + params.bottomMargin);
            final int bottom = Math.min(recyclerViewBottom, top + getOffset(child,parent));
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();
        final int recyclerViewLeft = parent.getPaddingLeft();
        final int recyclerViewRight = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = Math.max(recyclerViewLeft, child.getRight() + params.rightMargin);
            final int right = Math.min(recyclerViewRight, left + getOffset(child,parent));
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    // addition
    private int getOffset(View view, RecyclerView parent){
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter instanceof SectioningAdapter){
            int position = parent.getChildAdapterPosition(view);
            if(position < 0){
                //fix bug
                return 0;
            }
            SectioningAdapter sectioningAdapter = (SectioningAdapter)adapter;
            int type = sectioningAdapter.getItemViewBaseType(position);
            if (type == SectioningAdapter.TYPE_HEADER || type == SectioningAdapter.TYPE_GHOST_HEADER || type == SectioningAdapter.TYPE_FOOTER){
                return 0;
            }else if (type == SectioningAdapter.TYPE_ITEM){
                int section = sectioningAdapter.getSectionForAdapterPosition(position);
                int count = sectioningAdapter.getNumberOfItemsInSection(section);
                int positionInSection = sectioningAdapter.getPositionOfItemInSection(section,position);
                if (positionInSection == count - 1){// last one in section
                    return 0;
                }
            }
        }
        return heightPx;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (getOrientation(parent) == VERTICAL_LIST) {
            outRect.set(0, 0, 0, getOffset(view,parent));
        } else {
            outRect.set(0, 0, getOffset(view,parent), 0);
        }
    }
}