package com.ubfx.library.utils;

import android.support.annotation.NonNull;

import com.ubfx.library.utils.StringUtils;

/**
 * Created by yangchuanzhe on 30/01/2018.
 */

public class InputCheckUtils {

    public static boolean pswValid(@NonNull String input) {
        return !(input.length() < 6 || input.length() > 14);
    }

    public static boolean pswFmtInvalid(@NonNull String input) {
        return !StringUtils.regexMatch(input, "[0-9a-zA-Z]");
    }

    public static boolean nickValid(@NonNull String input) {
        return !(input.length() < 2 || input.length() > 16);
    }

    public static boolean nickFmtInvalid(@NonNull String input) {
        return !StringUtils.regexMatch(input, "[\\u4e00-\\u9fa5a-zA-Z0-9]");
    }

    public static boolean emailValid(@NonNull String input) {
        return input.contains("@");
    }
}
