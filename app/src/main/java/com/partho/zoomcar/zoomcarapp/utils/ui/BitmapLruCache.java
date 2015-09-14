package com.partho.zoomcar.zoomcarapp.utils.ui;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by partho on 22/8/15.
 */

public class BitmapLruCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache{
    public BitmapLruCache()
    {
        this(getDefaultLruCacheSize());
    }

    public BitmapLruCache(int sizeInKbs)
    {
        super(sizeInKbs);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes()*value.getHeight()/1024;
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url,bitmap);
    }

    public static int getDefaultLruCacheSize()
    {
        final int maxMemory = (int)(Runtime.getRuntime().maxMemory()/1024);
        final int cacheSize = maxMemory/8;
        return cacheSize;
    }
}
