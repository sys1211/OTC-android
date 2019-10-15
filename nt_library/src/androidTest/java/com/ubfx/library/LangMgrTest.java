package com.ubfx.library;

import com.ubfx.library.language.LangManager;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by yangchuanzhe on 2019/6/27.
 */
public class LangMgrTest {

    @Test
    public void appendLangTest() {

        String url = "http://fx-dev.nuagetimes.com/mobile/account?lang=zh-CN#/register_live/ubfx/trans";
        String result = LangManager.urlAppendLang(url);
        assertEquals(url, result);

        url = "http://fx-dev.nuagetimes.com/mobile/account#/register_live/ubfx/trans";
        result = LangManager.urlAppendLang(url);
        assertEquals("http://fx-dev.nuagetimes.com/mobile/account?lang=zh-CN#/register_live/ubfx/trans", (result));

        url = "http://fx-dev.nuagetimes.com/mobile/account?source=xxx";
        result = LangManager.urlAppendLang(url);
        assertEquals("http://fx-dev.nuagetimes.com/mobile/account?source=xxx&lang=zh-CN", (result));
    }


}
