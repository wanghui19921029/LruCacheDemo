package com.example.wh.cachedemo;

import android.graphics.Bitmap;

public interface ImageCache {

    void put(String key, Bitmap value);

    Bitmap get(String key);
}
