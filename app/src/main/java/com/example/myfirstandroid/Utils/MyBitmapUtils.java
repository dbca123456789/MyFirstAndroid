package com.example.myfirstandroid.Utils;


import android.content.Context  ;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * 自定义的BitmapUtils,实现三级缓存
 */

public class MyBitmapUtils {
    private NetCacheUtils netCacheUtils;
    private LocalCacheUtils localCacheUtils;
    private MemoryCacheUtils memoryCacheUtils;

    public MyBitmapUtils(Context context) {
        localCacheUtils = new LocalCacheUtils(context);
        memoryCacheUtils = new MemoryCacheUtils();
        netCacheUtils = new NetCacheUtils(localCacheUtils, memoryCacheUtils);
    }

    public Bitmap getBitmap(String url) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if(TextUtils.isEmpty(url)){
            return null;
        }
        Bitmap bitmap;
        // 内存缓存
        bitmap = memoryCacheUtils.getBitmapFromMemory(url);
        if (bitmap != null){
            Log.i("getBitmap", "内存获取Bitmap");
            return bitmap;
        }
        // 本地缓存
        bitmap = localCacheUtils.getBitmapFromLocal(url);
        if (bitmap != null){
            Log.i("getBitmap", "本地获取Bitmap");
            return bitmap;
        }

        // 网络缓存
        bitmap = netCacheUtils.getBitmapFromNet(url);
        Log.i("getBitmap", "网络获取Bitmap");
        return bitmap;

    }

}
