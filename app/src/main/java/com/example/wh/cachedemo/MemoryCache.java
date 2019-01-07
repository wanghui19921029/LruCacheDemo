package com.example.wh.cachedemo;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

public class MemoryCache implements ImageCache {
    private static final String TAG = "MemoryCache";
    private static MemoryCache mMemoryCache;
    private LruCache<String, Bitmap> mLruCache;


    private MemoryCache() {
        // 获取应用程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory() / 1024;
        int maxSize = maxMemory / 8;
        // 设置图片缓存大小为程序最大可用内存的1/8
        mLruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
                // 这种写法在一些手机上不好用，具体原因不清楚。
                // return bitmap.getByteCount() * bitmap.getHeight() / 1024;
            }
        };
    }

    public static MemoryCache getInstance() {
        if (mMemoryCache == null) {
            mMemoryCache = new MemoryCache();
        }
        return mMemoryCache;
    }

    @Override
    public void put(String key, Bitmap value) {
        Log.i(TAG, "put: ");
        mLruCache.put(key, value);
    }

    @Override
    public Bitmap get(String key) {
        Log.i(TAG, "get: ");
        return mLruCache.get(key);
    }
}
