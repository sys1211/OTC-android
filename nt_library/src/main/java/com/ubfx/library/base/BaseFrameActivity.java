package com.ubfx.library.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.ubfx.library.R;

/**
 * Created by yangchuanzhe on 2017/9/4.
 */

public abstract class BaseFrameActivity extends UBFXActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lib_layout_single_framlayout);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.lib_fl_single, getFragment());
            ft.commitAllowingStateLoss();
        }

    protected abstract Fragment getFragment();
}
