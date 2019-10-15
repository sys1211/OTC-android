package com.ubfx.library.loading;

/**
 * Created by ybq.
 */
public enum Style {

    FADING_LINE(1),// old loading
    ROTATING_RING(2),
    WAVE(3);

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private int value;

    Style(int value) {
        this.value = value;
    }
}
