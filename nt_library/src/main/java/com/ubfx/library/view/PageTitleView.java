package com.ubfx.library.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shizhefei.view.indicator.FixedIndicatorView;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.slidebar.TextWidthColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
import com.ubfx.library.R;
import com.ubfx.library.UBFXLibraryConfig;
import com.ubfx.library.language.LangManager;
import com.ubfx.library.utils.ScreenUtils;
import com.ubfx.theme.ThemeMgr;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by limeng on 2017/9/5.
 */

public class PageTitleView {
    private TitleBarLayout titleBarLayout;
    private FixedIndicatorView indicatorView;
    private TitleMenuView titleMenuView;

    private Context mContext;
    private ViewGroup mContainerView;
    private List<String> mTitles;
    private PageTitleViewHandler mHandler;
    private int mSplitMethod = FixedIndicatorView.SPLITMETHOD_WEIGHT;

    private boolean mListStyle;

    private int colorYellow;
    private int colorGray;

    public boolean isListStyle() {
        return mListStyle;
    }

    public Indicator getIndicator() {
        return indicatorView;
    }

    public PageTitleView(Context context, ViewGroup containerView, List<String> titles, PageTitleViewHandler handler) {
        this(context, containerView, titles, FixedIndicatorView.SPLITMETHOD_WEIGHT, handler);
    }

    public PageTitleView(Context context, ViewGroup containerView, List<String> titles, int splitMethod, PageTitleViewHandler handler) {
        mContext = context;
        mContainerView = containerView;
        mTitles = titles;
        mHandler = handler;
        mSplitMethod = splitMethod;

        colorYellow = UBFXLibraryConfig.getMainTextHighlightColor();
        colorGray = ThemeMgr.get().getThemeProvider().fontMiddle();

        boolean isLangCN = LangManager.get().currentIsChina();
        init(!isLangCN);
    }

    private void init(boolean listStyle) {
        mListStyle = listStyle;

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.dp2px(mContext, 44));
        titleBarLayout = new TitleBarLayout(mContext);
        titleBarLayout.setTitleSize(18);
        titleBarLayout.setBackgroundColor(ThemeMgr.get().getThemeProvider().backgroundMain());

        mContainerView.addView(titleBarLayout, 0, params);

        if (listStyle) {
            titleMenuView = new TitleMenuView(mContext);
            titleMenuView.setMenus(mTitles);
            titleMenuView.setMenuItemSelectedListener(new TitleMenuView.OnMenuItemSelectedListener() {
                @Override
                public void onMenuItemSelected(int position) {
                    if (mHandler != null) {
                        mHandler.onTitleSelected(position, mListStyle);
                    }
                }
            });
            titleBarLayout.setCustomTitle(titleMenuView);
        } else {
            indicatorView = new FixedIndicatorView(mContext);
            indicatorView.setAdapter(new IndicatorTitleAdapter(mContext, mTitles));
            indicatorView.setScrollBar(new TextWidthColorBar(mContext, indicatorView, colorYellow, ScreenUtils.dp2px(mContext, 2)));
            indicatorView.setSplitMethod(mSplitMethod);
            indicatorView.setOnTransitionListener(new OnTransitionTextListener().setColor(colorYellow, colorGray));
            indicatorView.setOnItemSelectListener(new Indicator.OnItemSelectedListener() {
                @Override
                public void onItemSelected(View view, int newIdx, int oldIdx) {
                    if (mHandler != null) {
                        mHandler.onTitleSelected(newIdx, mListStyle);
                    }
                }
            });
            titleBarLayout.setCustomTitle(indicatorView);
        }

        if (mHandler != null && mHandler.leftBackVisible()) {
            titleBarLayout.setLeftImageResource(UBFXLibraryConfig.getTitleBackIcon());
            titleBarLayout.setLeftIconColor(UBFXLibraryConfig.getTitleBackColor());
            titleBarLayout.setLeftClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mHandler != null) {
                        mHandler.onClickLeftBack();
                    }
                }
            });
        }
        if (mHandler != null && mHandler.getCustomAction() != null) {
            titleBarLayout.addAction(mHandler.getCustomAction());
        }
    }

    public void selectTitle(int position) {
        if (indicatorView != null) {
            indicatorView.setCurrentItem(position, true);
        } else if (titleMenuView != null) {
            titleMenuView.setCurrentItem(position);
        }
    }

    public interface PageTitleViewHandler {
        boolean leftBackVisible();

        void onClickLeftBack();

        TitleBarLayout.Action getCustomAction();

        void onTitleSelected(int position, boolean listStyle);
    }

    public static abstract class FragmentTitleViewHandler implements PageTitleViewHandler {
        @Override
        public boolean leftBackVisible() {
            return false;
        }

        @Override
        public void onClickLeftBack() {
        }

        @Override
        public TitleBarLayout.Action getCustomAction() {
            return null;
        }
    }

    public static abstract class ActivityTitleViewHandler implements PageTitleViewHandler {
        @Override
        public boolean leftBackVisible() {
            return true;
        }

        @Override
        public void onClickLeftBack() {
        }

        @Override
        public TitleBarLayout.Action getCustomAction() {
            return null;
        }
    }

    public static class IndicatorTitleAdapter extends Indicator.IndicatorAdapter {
        LayoutInflater inflater;
        List<String> titles;

        public IndicatorTitleAdapter(Context context, List<String> titles) {
            this.inflater = LayoutInflater.from(context);
            this.titles = titles == null ? new ArrayList<String>() : titles;
        }

        public void setTitles(List<String> titles) {
            if (!checkListChanged(this.titles, titles)) {
                return;
            }
            this.titles = titles;
            notifyDataSetChanged();
        }

        public boolean checkListChanged(@NonNull List<String> oldList, @NonNull List<String> newList) {
            if (oldList.size() != newList.size()) {
                return true;
            }

            for (int i = 0; i < oldList.size(); i++) {
                if (!oldList.get(i).equals(newList.get(i))) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public int getCount() {
            return titles == null ? 0 : titles.size();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = inflater.inflate(R.layout.lib_tab_title, viewGroup, false);
            }

            ((TextView) view).setText(titles.get(i));
            return view;
        }
    }
}
