package com.ubfx.library.util;

import android.graphics.Color;

import com.ubfx.library.utils.StringUtils;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by yangchuanzhe on 2019/8/21.
 */
public class StringUtilsTest {
    @Test
    public void testHexFromColor() {
        assertEquals(StringUtils.hexFrom(Color.BLACK), "#000000");
        assertEquals(StringUtils.hexFrom(Color.WHITE), "#FFFFFF");
        assertEquals(StringUtils.hexFrom(Color.RED), "#FF0000");
    }
}
