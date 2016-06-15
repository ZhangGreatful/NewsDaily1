package com.example.administrator.newsdaily.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;

import com.example.administrator.newsdaily.model.volleyhttp.VolleyHttp;
import com.example.administrator.newsdaily.volley.toolbox.ImageLoader.ImageCache;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 下载网络图片,首先看内存中是否存在,
 * 再看缓存中是否存在,若都不存在,则执行下载任务
 */
public class LoadImage {
    private static LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(1024 * 1024 * 3);

    private Context           context;
    private ImageLoadListener listener;

    public LoadImage(Context context, ImageLoadListener listener) {
        this.context = context;
        this.listener = listener;
    }

    /**
     * 下载接口
     */
    public interface ImageLoadListener {
        void imageLoadOk(Bitmap bitmap, String url);
    }

    /**
     * 获取图片
     *
     * @param url 该图片的网址url
     * @param iv  图片格式
     */
    public void geBitmap(String url, ImageView iv) {
        Bitmap bitmap = null;
//        若url为空,则返回为空
        if (url == null || url.length() <= 0)
            return;
        //1.先看看内存中有没有,
        // 创建一个方法getBitmapFronReference,查看内存中的图片
        // 若有,则直接利用内存中的图片
        bitmap = getBitmapFromReference(url);
        if (bitmap != null) {
            iv.setImageBitmap(bitmap);
        }
        //2.去看看缓存文件有没有
//        创建一个方法getBitmapFromCache,查看缓存中的图片
        bitmap = getBitmapFromCache(url);
        if (bitmap != null) {
            cache.put(url, bitmap);
            iv.setImageBitmap(bitmap);
        }
        //3.异步加载
//        若内存,缓存中都没有,则执行异步加载,从网络下载
        VolleyHttp http = new VolleyHttp(context);
        http.addImage(url, imageCache, iv);

    }

    ImageCache imageCache = new ImageCache() {

        @Override
        public Bitmap getBitmap(String url) {
            // TODO Auto-generated method stub
            return cache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            // TODO Auto-generated method stub
            saveCacheFile(url, bitmap);
            cache.put(url, bitmap);
        }

    };

    /**
     * 通过异步加载得到图片
     *
     * @param url 图片网址url
     */
    private void getBitmapAsync(String url) {
//        异步加载的入口
        ImageAsyncTask imageAsyncTask = new ImageAsyncTask();
        imageAsyncTask.execute(url);
    }

    /**
     * 从缓存中得到图片的方法
     *
     * @param url
     * @return
     */
    private Bitmap getBitmapFromCache(String url) {
        String name = url.substring(url.lastIndexOf("/") + 1);
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir == null)
            return null;
        File[] files = cacheDir.listFiles();
        if (files == null) {
            return null;
        }
        File bitmapFile = null;
        for (File file : files) {
            if (file.getName().equals(name)) {
                bitmapFile = file;
                break;
            }
        }
        if (bitmapFile == null) {
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(bitmapFile.getAbsolutePath());
        if (bitmap == null)
            return null;
        return bitmap;
    }

    private Bitmap getBitmapFromReference(String url) {
        Bitmap bitmap = null;
        bitmap = cache.get(url);
        return bitmap;
    }


    /**
     * 执行异步加载,从网络获取图片
     * String  url类型
     * Void  空值
     * Bitmap  返回值类型
     */
    private class ImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private String url;

        @Override
        protected Bitmap doInBackground(String... params) {
            url = params[0];
            Bitmap bitmap = null;
            try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream is = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);
                cache.put(params[0], bitmap);
                //缓存文件
                saveCacheFile(params[0], bitmap);
                System.out.println("3.网络中的图片");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if (listener != null) {
                listener.imageLoadOk(result, url);
            }
        }
    }

    /**
     * 将图片放到内存
     * @param url
     * @param bitmap
     */
    public void saveCacheFile(String url, Bitmap bitmap) {
        String name = url.substring(url.lastIndexOf("/") + 1);

        File cacheDir = context.getCacheDir();
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        OutputStream stream;
        try {
            stream = new FileOutputStream(new File(cacheDir, name));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
