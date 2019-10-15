package com.ubfx.theme;

import android.support.annotation.ColorInt;

/**
 * Created by yangchuanzhe on 2019/8/19.
 */
public interface ThemeProvider {


    //主色
    @ColorInt
    int main();

    //字黄色
    @ColorInt
    int mainHighlight();

    //超浅黄
    @ColorInt
    int yellowLightest();

    //浅黄
    @ColorInt
    int yellowLight();

    //深色文字
    @ColorInt
    int fontMain();

    //中色文字
    @ColorInt
    int fontMiddle();

    //浅色文字
    @ColorInt
    int fontLight();

    //彩色背景上的文字颜色
    @ColorInt
    int fontWhite();

    //至灰文字 深边线
    @ColorInt
    int grayLightest();

    //浅边线
    @ColorInt
    int borderLight();

    //深背景线
    @ColorInt
    int background();

    //浅背景色
    @ColorInt
    int backgroundLight();

    //用于大面积底色
    @ColorInt
    int backgroundMain();

    @ColorInt
    int priceRed();

    @ColorInt
    int priceGreen();

}
