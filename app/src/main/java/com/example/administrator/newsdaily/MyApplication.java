package com.example.administrator.newsdaily;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/14 0014.
 */
public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    private static Map<String, Object> globalData =new HashMap<String, Object>();

    public static MyApplication application;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application=this;
        Log.d(TAG, "MyApplication  onCreate() ");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        Log.d(TAG, "\"MyApplication  onLowMemory() ");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d(TAG, "MyApplication  onTerminate() ");
    }

    //增加数据
    public static void addCacheData(String key,Object value){
        globalData.put(key, value);
    }


    //删除数据
    public static void delCacheData(String key){
        if(globalData.containsKey(key))
            globalData.remove(key);
    }


    //清空数据
    public static void clearCacheData(){
        globalData.clear();
    }

    /**
     *
     * @param key
     * @return
     */
    public static Object getCacheData(String key){
        if(globalData.containsKey(key))
            return globalData.get(key);
        return null;
    }
}
