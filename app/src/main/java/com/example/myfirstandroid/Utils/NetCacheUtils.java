package com.example.myfirstandroid.Utils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

/**
 * 三级缓存之网络缓存
 */
public class NetCacheUtils {

    private static final String TAG = "NetCacheUtils";

    private LocalCacheUtils localCacheUtils;
    private MemoryCacheUtils memoryCacheUtils;

    Bitmap bitmap;

    public NetCacheUtils(LocalCacheUtils localCacheUtils, MemoryCacheUtils memoryCacheUtils) {
        this.localCacheUtils = localCacheUtils;
        this.memoryCacheUtils = memoryCacheUtils;
    }

    /**
     * 从网络下载图片
     *
     * @param url 下载图片的网络地址
     */

    public Bitmap getBitmapFromNet(String url){
//        Log.i(TAG, "getBitmapFromNet(String url)----URL: "+url);
        new BitmapTask().execute(url);
        return bitmap;
    }

    /**
     * AsyncTask就是对handler和线程池的封装
     * 第一个泛型:执行AsyncTask需要传入的参数，可用于在后台任务中使用
     * 第二个泛型:更新进度的泛型（进度单位
     * 第三个泛型:onPostExecute任务执行结束后的返回结果
     */
     class BitmapTask extends AsyncTask<Object, Void, Bitmap> {

        private String url;

        /**
         * 后台耗时操作,存在于子线程中
         *
         * @param params
         * @return
         */
        @Override
        protected Bitmap doInBackground(Object[] params) {

            url = (String) params[0];
//            Log.i(TAG, "doInBackground(Object[] params)----URL: "+url);
            bitmap = downloadBitmap(url);
            return bitmap;
        }

        /**
         * 更新进度,在主线程中
         *
         * @param values
         */
        @Override
        protected void onProgressUpdate(Void[] values) {
            super.onProgressUpdate(values);
        }


        /**
         * 耗时方法结束后执行该方法,主线程中
         * @param result
         */
        @Override
        protected void onPostExecute(Bitmap result) {
//            Log.i(TAG, "onPostExecute(Bitmap result)---url:" + url);
            if (result != null){
                memoryCacheUtils.setBitmapToMemory(url, result);
                Log.i(TAG, "onPostExecute：保存内存ing");

                try {
                    localCacheUtils.setBitmapToLocal(url, result);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "onPostExecute：保存本地ing");


            }
        }
    }

    /**
     * 网络下载图片
     *
     * @param url
     * @return
     */
    private Bitmap downloadBitmap(String url) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode==200){
                // 图片压缩
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;  // 宽高压缩为原来的1/2
                // 图片解码时使用的颜色模式，也就是图片中每个像素颜色的表示方式。
                // ARGB_4444 图片中每个像素用两个字节（16位）存储，Alpha，R，G，B四个通道每个通道用4位表示
                options.inPreferredConfig = Bitmap.Config.ARGB_4444;
                bitmap = BitmapFactory.decodeStream(conn.getInputStream(), null, options);
                return bitmap;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }
}
