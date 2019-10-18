package com.otcbase.merchant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LaunchScreenActivity extends AppCompatActivity {

    @BindView(R.id.iv_launch_screen)
    ImageView ivLaunchScreen;
    @BindView(R.id.tv_launch_screen)
    TextView tvLaunchScreen;

    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);//将状态栏设置成透明色
            getWindow().setNavigationBarColor(Color.TRANSPARENT);//将导航栏设置为透明色
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView()
                        .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
        setContentView(R.layout.activity_launch_screen);
        ButterKnife.bind(this);
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);
        int heightPixels = metric.heightPixels;

        RelativeLayout.LayoutParams ivParams = (RelativeLayout.LayoutParams) ivLaunchScreen.getLayoutParams();
        ivParams.topMargin = (int) (heightPixels * 0.208);
        ivLaunchScreen.setLayoutParams(ivParams);
        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tvLaunchScreen.getLayoutParams();
        // fix some device height in params not working
        tvParams.bottomMargin = (int) (heightPixels * 0.04);
        tvLaunchScreen.setLayoutParams(tvParams);


        runnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LaunchScreenActivity.this, MainActivity.class));
                finish();
            }
        };
        handler.postDelayed(runnable, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
