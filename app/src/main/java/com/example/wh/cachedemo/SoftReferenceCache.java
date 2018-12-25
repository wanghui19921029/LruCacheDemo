package com.example.wh.cachedemo;

import android.graphics.Bitmap;
import android.util.Log;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public class SoftReferenceCache implements ImageCache {
    private static final String TAG = "SoftReferenceCache";
    private static SoftReferenceCache mSoftReference;
    private Map<String, SoftReference<Bitmap>> mMap = null;

    private SoftReferenceCache() {
        mMap = new HashMap<String, SoftReference<Bitmap>>();
    }

    public static SoftReferenceCache getInstance() {
        if (mSoftReference == null) {
            mSoftReference = new SoftReferenceCache();
        }
        return mSoftReference;
    }

    @Override
    public void put(String key, Bitmap value) {
        Log.i(TAG, "put: ");
        mMap.put(key, new SoftReference<Bitmap>(value));
    }

    @Override
    public Bitmap get(String key) {
        Log.i(TAG, "get: ");
        SoftReference<Bitmap> softReference = mMap.get(key);
        if (softReference != null) {
            return softReference.get();
        }
        return null;
    }
}
