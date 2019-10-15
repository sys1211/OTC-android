package com.ubfx.library.diskcache;

public interface Callback<T> {
    void onValue(T value);
}