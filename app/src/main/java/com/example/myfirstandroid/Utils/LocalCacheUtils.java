package com.example.myfirstandroid.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;


/**
 * 三级缓存之本地缓存
 */
public class LocalCacheUtils {

    private Context context;
    private String path = null;

    private static final String CACHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WebNews";


    public LocalCacheUtils(Context context) {
        this.context = context;
        path = context.getCacheDir().getAbsolutePath();
    }

    /**
     * 从网络获取图片后,保存至本地缓存
     *
     * @param url
     * @param bitmap
     */

    public void setBitmapToLocal(String url, Bitmap bitmap) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Log.i("setBitmapToLocal", "url:" + url);
        String fileName = MD5Encoder.encode(url);
        Log.i("setBitmapToLocal", "fileName:" + fileName);
        File file = new File(path, fileName);
        try {
            // 把图片保存至本地
            // Bitmap.compress()可以压缩图片，但压缩的是存储大小,
            // BitmapFactory.decodeByteArray方法对压缩后的byte[]解码后，得到的Bitmap大小依然和未压缩过一样
            // 如果想要显示的Bitmap占用的内存少一点，需要设置加载的像素长度和宽度（变成缩略图）
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }


    /**
     * 从本地读取图片
     *
     * @param url
     */
    public Bitmap getBitmapFromLocal(String url) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        //把图片的url当做文件名
        String fileName = null;
        fileName = MD5Encoder.encode(url);
        File file = new File(path, fileName);
        if (file.exists()){
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                return bitmap;
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }

        }
        return null;
    }
}
