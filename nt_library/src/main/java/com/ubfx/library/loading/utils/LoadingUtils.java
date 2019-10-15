package com.ubfx.library.loading.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.ubfx.library.R;
import com.ubfx.library.UBFXLibraryConfig;
import com.ubfx.library.loading.SpinKitView;
import com.ubfx.library.loading.SpriteFactory;
import com.ubfx.library.loading.sprite.Sprite;
import com.ubfx.library.utils.ScreenUtils;
import com.ubfx.theme.ThemeMgr;

public class LoadingUtils {

    public static LoadingHolder loadingHolder;

    public static View createLoadingView(Context context) {
        CardView cardView = new CardView(context);
        if (loadingHolder != null) {
            cardView.setRadius(loadingHolder.getCorner());
            cardView.setCardElevation(loadingHolder.getCardElevation());
            cardView.setCardBackgroundColor(loadingHolder.getColorBg());
        } else {
            cardView.setRadius(context.getResources().getDimension(R.dimen.lib_default_corner_radius));
            cardView.setCardElevation(20);
            cardView.setCardBackgroundColor(ThemeMgr.get().getThemeProvider().backgroundMain());
        }


        SpinKitView loadingView = new SpinKitView(context);

        Sprite sprite = SpriteFactory.create(UBFXLibraryConfig.getLoadingStyle());
        sprite.setColor(UBFXLibraryConfig.getMainColor());
        loadingView.setIndeterminateDrawable(sprite);
        int width = ScreenUtils.dp2px(context, 40);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, width);
        params.gravity = Gravity.CENTER;
        cardView.addView(loadingView, params);
        return cardView;
    }

    public static void showLoadingOn(@NonNull FrameLayout rootView) {
        Context context = rootView.getContext();
        View loadingView = createLoadingView(context);
        loadingView.setId(R.id.ubfx_loading_view);

        int width = ScreenUtils.dp2px(context, 100);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, width);
        params.gravity = Gravity.CENTER;
        rootView.addView(loadingView, params);

        loadingView.bringToFront();
        loadingView.setVisibility(View.VISIBLE);
        if (loadingView instanceof CardView) {
            ((SpinKitView) ((CardView) loadingView).getChildAt(0)).getIndeterminateDrawable().start();
        }
    }

    public static void dismissLoadingOn(@NonNull FrameLayout rootView) {
        View view = rootView.findViewById(R.id.ubfx_loading_view);
        if (view != null) {
            view.setVisibility(View.GONE);
            if (view instanceof CardView) {
                ((SpinKitView) ((CardView) view).getChildAt(0)).getIndeterminateDrawable().stop();
            }
            rootView.removeView(view);
        }
    }
}