package com.example.administrator.newsdaily.model.volleyhttp;

import android.content.Context;
import android.widget.ImageView;

import com.example.administrator.newsdaily.R;
import com.example.administrator.newsdaily.volley.RequestQueue;
import com.example.administrator.newsdaily.volley.Response;
import com.example.administrator.newsdaily.volley.Response.ErrorListener;
import com.example.administrator.newsdaily.volley.Response.Listener;
import com.example.administrator.newsdaily.volley.toolbox.ImageLoader;
import com.example.administrator.newsdaily.volley.toolbox.ImageLoader.ImageCache;
import com.example.administrator.newsdaily.volley.toolbox.ImageLoader.ImageListener;
import com.example.administrator.newsdaily.volley.toolbox.MultiPosttRequest;
import com.example.administrator.newsdaily.volley.toolbox.StringRequest;
import com.example.administrator.newsdaily.volley.toolbox.Volley;

import java.io.File;

/**
 * Created by Administrator on 2016/6/14 0014.
 */
public class VolleyHttp {
    public static RequestQueue mQueue;
    Context context;

    public VolleyHttp(Context context) {
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(context);
        }
        this.context = context;
    }

    //      解析Json数据
    public void getJSONObject(String url, Listener<String> listener,
                              ErrorListener errorListener) {
        StringRequest request = new StringRequest(url, listener, errorListener);
        mQueue.add(request);
    }

    //      添加图片
    public void addImage(String url, ImageCache imageCache, ImageView iv) {
        ImageLoader mImageLoader = new ImageLoader(mQueue, imageCache);
        ImageListener listener = ImageLoader.getImageListener(iv,
                R.drawable.defaultpic, android.R.drawable.ic_delete);
        mImageLoader.get(url, listener);
    }


    //    下载图片
    public void upLoadImage(String url, File file, Listener<String> listener,
                            ErrorListener errorListener) {
        MultiPosttRequest request = new MultiPosttRequest(url, listener,
                errorListener);
        request.buildMultipartEntity("portrait", file);
        mQueue.add(request);
    }

    //      添加用户信息
    public void addUserString(String url, String token, String imei,
                              Response.Listener<String> listener, ErrorListener errorListener) {
        MultiPosttRequest request = new MultiPosttRequest(url, listener,
                errorListener);
        request.buildMultipartEntity("token", token);
        request.buildMultipartEntity("imei", imei);
        request.buildMultipartEntity("ver", 1 + "");
        mQueue.add(request);
    }

}
