package com.ubfx.library.region;

import android.support.annotation.Nullable;

import com.ubfx.library.model.RegionModel;

/**
 * Created by yangchuanzhe on 2019/10/11.
 */
public interface RegionCallback {
    void callback(@Nullable RegionModel regionModel);
}
