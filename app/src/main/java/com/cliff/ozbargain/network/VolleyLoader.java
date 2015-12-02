package com.cliff.ozbargain.network;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.cliff.ozbargain.core.OZBApplication;

/**
 * Created by Clifford on 25/11/2015.
 */
public class VolleyLoader {
    private static VolleyLoader loader =null;
    private RequestQueue mRequestQueue;
    private ImageLoader imageLoader;

    private VolleyLoader() {
        mRequestQueue= Volley.newRequestQueue(OZBApplication.getAppContext());
        imageLoader=new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {

            LruCache<String,Bitmap> lruCache = new LruCache<>((int) (Runtime.getRuntime().maxMemory()/1024/8));
            @Override
            public Bitmap getBitmap(String url) {
                return lruCache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                lruCache.put(url, bitmap);
            }
        });
    }

    public static VolleyLoader getInstance(){
        if (loader == null){
            loader=new VolleyLoader();
        }
        return loader;
    }

    public RequestQueue getRequestQueue(){
        return mRequestQueue;
    }

    public ImageLoader getImageLoader(){
        return imageLoader;
    }
}
