package com.ubfx.library.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

import com.ubfx.library.view.recycler.SectioningAdapter;
import com.ubfx.library.view.recycler.StickyHeaderLayoutManager;

/**
 * @author wangwenting  2018/11/20
 */
public class FloatingUtils {

    public static int getScreenHeight(Context mContext) {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        int heightPixels = dm.heightPixels;
        float density = dm.density;
        int screenHeight = (int) (heightPixels / density);
        return screenHeight;
    }

    public static int getFlag(RecyclerView.LayoutManager manager) {
        if (manager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) manager;
            //获取RecyclerView当前顶部显示的第一个条目对应的索引
            int position = linearLayoutManager.findFirstVisibleItemPosition();
            //根据索引来获取对应的itemView
            View firstVisiableChildView = manager.findViewByPosition(position);
            if (firstVisiableChildView != null) {
                //获取当前显示条目的高度
                int itemHeight = firstVisiableChildView.getHeight();
                //获取当前Recyclerview 偏移量
                int flag = (position) * itemHeight - firstVisiableChildView.getTop();
                return flag;
            }
        } else if (manager instanceof StickyHeaderLayoutManager) {
            StickyHeaderLayoutManager stickyHeaderManager = (StickyHeaderLayoutManager) manager;
            SectioningAdapter.ItemViewHolder firstVisibleItemViewHolder = stickyHeaderManager.getFirstVisibleItemViewHolder(true);
            if (firstVisibleItemViewHolder != null) {
                int layoutPosition = firstVisibleItemViewHolder.getLayoutPosition();
                int itemHeight = firstVisibleItemViewHolder.itemView.getHeight();
                int flag = (layoutPosition) * itemHeight;
                return flag;
            }
        }
        return 0;
    }

    public static void moveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int n) {
        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(n);
        }
    }

}
