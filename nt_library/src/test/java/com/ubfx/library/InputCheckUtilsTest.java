package com.ubfx.library;

import com.ubfx.library.utils.InputCheckUtils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by yangchuanzhe on 30/01/2018.
 */
public class InputCheckUtilsTest {
    @Test
    public void pswValid() {
        assertTrue(InputCheckUtils.pswValid("asdasa"));
        assertFalse(InputCheckUtils.pswValid("asda"));
        assertFalse(InputCheckUtils.pswValid("aaaaaaaaaaaaaaaaaaaa"));
    }

    @Test
    public void pswFmtInvalid() {
        assertTrue(InputCheckUtils.pswFmtInvalid("@!##"));
        assertFalse(InputCheckUtils.pswFmtInvalid("dsQQ121"));
    }

    @Test
    public void nickValid() {
        assertTrue(InputCheckUtils.nickValid("aaaaaa"));
        assertFalse(InputCheckUtils.nickValid("a"));
        assertFalse(InputCheckUtils.nickValid("aaaaaaaaaaaaaaaaaaaa"));
    }

    @Test
    public void nickFmtInvalid() {
        assertTrue(InputCheckUtils.nickFmtInvalid("@@##!"));
        assertFalse(InputCheckUtils.nickFmtInvalid("啊as12"));
    }

    @Test
    public void emailValid() {
        assertTrue(InputCheckUtils.emailValid("asd@asdas"));
        assertTrue(InputCheckUtils.emailValid(".asd@a.sdas"));
        assertFalse(InputCheckUtils.emailValid("asdassad"));
    }

}