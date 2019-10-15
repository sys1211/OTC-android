package com.ubfx.library.view.indicator;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.viewpager.SViewPager;

/**
 * Created by yangchuanzhe on 27/04/2018.
 */

public class MyIndicatorViewPager extends IndicatorViewPager {

    public MyIndicatorViewPager(Indicator indicator, ViewPager viewPager) {
        super(indicator, viewPager);
    }

    public MyIndicatorViewPager(Indicator indicator, ViewPager viewPager, boolean indicatorClickable) {
        super(indicator, viewPager, indicatorClickable);
    }

    @Override
    protected void iniOnItemSelectedListener() {
        this.indicatorView.setOnItemSelectListener(new Indicator.OnItemSelectedListener() {
            public void onItemSelected(View selectItemView, int select, int preSelect) {
                if(getOnIndicatorPageChangeListener() instanceof MyOnIndicatorPageChangeListener){
                    if(!((MyOnIndicatorPageChangeListener)getOnIndicatorPageChangeListener()).beforeIndicatorPageChanged(select,preSelect)){
                        indicatorView.setCurrentItem(preSelect);
                        return;
                    }
                }

                if(MyIndicatorViewPager.this.viewPager instanceof SViewPager) {
                    MyIndicatorViewPager.this.viewPager.setCurrentItem(select, ((SViewPager)MyIndicatorViewPager.this.viewPager).isCanScroll());
                } else {
                    MyIndicatorViewPager.this.viewPager.setCurrentItem(select, false);
                }

            }
        });
    }

    public void setOnIndicatorPageChangeListener(MyOnIndicatorPageChangeListener onIndicatorPageChangeListener) {
        super.setOnIndicatorPageChangeListener(onIndicatorPageChangeListener);
    }

    public interface MyOnIndicatorPageChangeListener extends IndicatorViewPager.OnIndicatorPageChangeListener{
        boolean beforeIndicatorPageChanged(int shouldSelect , int oldIndex);
    }
}
