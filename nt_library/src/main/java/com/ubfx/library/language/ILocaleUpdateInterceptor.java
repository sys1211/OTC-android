package com.ubfx.library.language;

import android.support.annotation.Nullable;

import java.util.Locale;

/**
 * Created by yangchuanzhe on 2019/2/28.
 */
public interface ILocaleUpdateInterceptor {
    @Nullable
    public Locale processLocal(Locale defaultLocal);
}
