package com.example.wh.cachedemo;

import android.graphics.Bitmap;

public interface LoadListener {
    void onSuccess(String url, Bitmap bitmap);

    void onError(String error);
}
