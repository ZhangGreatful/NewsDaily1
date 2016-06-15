package com.example.administrator.newsdaily.model.biz;

import android.content.Context;

import com.example.administrator.newsdaily.common.CommonUtil;
import com.example.administrator.newsdaily.common.LogUtil;
import com.example.administrator.newsdaily.common.SystemUtils;
import com.example.administrator.newsdaily.model.httpclient.AsyncHttpClient;
import com.example.administrator.newsdaily.model.httpclient.RequestParams;
import com.example.administrator.newsdaily.model.httpclient.ResponseHandlerInterface;
import com.example.administrator.newsdaily.model.volleyhttp.VolleyHttp;
import com.example.administrator.newsdaily.volley.Response.ErrorListener;
import com.example.administrator.newsdaily.volley.Response.Listener;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 用户管理,执行用户的注册与登录信息的管理
 */
public class UserManager {
    private static UserManager userManager;
    private        Context     context;
    private        String      imei;

    private UserManager(Context context) {
        this.context = context;
        imei = SystemUtils.getIMEI(context);
    }

    public static UserManager getInstance(Context context) {
        if (userManager == null)
            userManager = new UserManager(context);
        return userManager;
    }

    public void user(String token, ResponseHandlerInterface handler) {
        LogUtil.i("用户中心交互", "执行");
        AsyncHttpClient httpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("token", token);
        params.put("imei", imei);
        params.put("ver", 1);
        httpClient.post(CommonUtil.APPURL + "/user_home", params, handler);
    }

    public void user(Context context, String token, Listener<String> listener,
                     ErrorListener errorListener) {

        new VolleyHttp(context).addUserString(CommonUtil.APPURL + "/user_home",
                token, imei, listener, errorListener);
    }

    /**
     * user_register?ver=版本号&uid=用户名&email=邮箱&pwd=登陆密码
     *
     * @param handler 回调接口
     * @param args    包含参数如下： ver : 版本 uid : 用户昵称 pwd : 密码 email : 邮箱
     */
    public void register(ResponseHandlerInterface handler, String... args) {
        LogUtil.d(LogUtil.TAG, "执行注册...");
        // 1.提交到服务器端 生成用户
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(CommonUtil.APPURL + "/user_register?ver=" + args[0]
                        + "&uid=" + args[1] + "&pwd=" + args[2] + "&email=" + args[3],
                handler);
    }

    /**
     * user_register?ver=版本号&uid=用户名&email=邮箱&pwd=登陆密码
     *
     * @param args          包含参数如下： ver : 版本 uid : 用户昵称 pwd : 密码 email : 邮箱
     * @param listener
     * @param errorListener
     */
    public void register(Context context, Listener<String> listener,
                         ErrorListener errorListener, String... args) {
        LogUtil.d(LogUtil.TAG, "执行注册...");
        new VolleyHttp(context).getJSONObject(CommonUtil.APPURL
                + "/user_register?ver=" + args[0] + "&uid=" + args[1] + "&pwd="
                + args[2] + "&email=" + args[3], listener, errorListener);
    }

    /**
     * 、http://118.244.212.82:9094//newsClient/login?uid=admin&pwd=admin&imei=
     * abc&ver=1&device=1
     * http://118.244.212.82:9094//newsClient/login?ver=1&uid=
     * admin&pwd=admin&device=000000000000000 用户登录处理方法
     *
     * @param args          包含参数如下： ver : 版本 uid : 用户昵称 pwd : 密码 imei : 手机IMEI号 device :
     *                      登录设备 ： 0 为移动端 ，1为PC端
     * @param listener
     * @param errorListener
     */
    public void login(Context context, Listener<String> listener,
                      ErrorListener errorListener, String... args) {
        LogUtil.d(LogUtil.TAG, "ִ�е�¼...");
        // 1.提交到服务器端 生成用户
        // login?uid=admin&pwd=admin&imei=abc&ver=1&device=1
        // AsyncHttpClient httpClient = new AsyncHttpClient();
        // httpClient.get(CommonUtil.APPURL + "/user_login?ver=" + args[0]
        // + "&uid=" + args[1] + "&pwd=" + args[2] + "&device=" + args[3],
        // handler);
        new VolleyHttp(context).getJSONObject(CommonUtil.APPURL
                + "/user_login?ver=" + args[0] + "&uid=" + args[1] + "&pwd="
                + args[2] + "&device=" + args[3], listener, errorListener);
    }

    /**
     * 、http://118.244.212.82:9094//newsClient/login?uid=admin&pwd=admin&imei=
     * abc&ver=1&device=1
     * http://118.244.212.82:9094//newsClient/login?ver=1&uid=
     * admin&pwd=admin&device=000000000000000 用户登录处理方法
     *
     * @param handler 回调接口
     * @param args    包含参数如下： ver : 版本 uid : 用户昵称 pwd : 密码 imei : 手机IMEI号 device :
     *                登录设备 ： 0 为移动端 ，1为PC端
     */
    public void login(ResponseHandlerInterface handler, String... args) {
        LogUtil.d(LogUtil.TAG, "ִ�е�¼...");
        // 1.提交到服务器端 生成用户
        // login?uid=admin&pwd=admin&imei=abc&ver=1&device=1
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(CommonUtil.APPURL + "/user_login?ver=" + args[0]
                        + "&uid=" + args[1] + "&pwd=" + args[2] + "&device=" + args[3],
                handler);
    }

    /***
     * user_forgetpass?ver=版本号&email=邮箱
     *
     * @param handler
     * @param args    包含的参数如下：
     *                ver：版本号
     *                email：邮箱
     */
    public void forgetPass(ResponseHandlerInterface handler, String... args) {
        LogUtil.d(LogUtil.TAG, "执行忘记密码...");
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(CommonUtil.APPURL + "/user_forgetpass?ver=" + args[0]
                + "&email=" + args[1], handler);
    }

    /***
     * user_forgetpass?ver=版本号&email=邮箱
     *
     * @param args ver：版本号
     *             email：邮箱
     */
    public void forgetPass(Context context, Listener<String> listener,
                           ErrorListener errorListener, String... args) {
        LogUtil.d(LogUtil.TAG, "ִ执行忘记密码...");
        // AsyncHttpClient httpClient = new AsyncHttpClient();
        // httpClient.get(CommonUtil.APPURL + "/user_forgetpass?ver=" + args[0]
        // + "&email=" + args[1] ,
        // handler);
        new VolleyHttp(context).getJSONObject(CommonUtil.APPURL
                        + "/user_forgetpass?ver=" + args[0] + "&email=" + args[1],
                listener, errorListener);
    }

    /**
     * http://118.244.212.82:9094//newsClient/home?ver=sfkl&token=admin1abc&imei
     * =sdf 获取用户中心数据
     *
     * @param handler 回调接口
     * @param args    包含参数 顺序如下 ver : 版本 token : 令牌 imei :手机IMEI
     */
    public void getUserInfo(ResponseHandlerInterface handler, String... args) {
        LogUtil.d(LogUtil.TAG, "ִ执行用户中心...");
        new AsyncHttpClient().get(CommonUtil.APPURL + "/user_home?ver="
                + args[0] + "&token=" + args[1] + "&imei=" + args[2], handler);
    }

    /**
     * http://118.244.212.82:9094//newsClient/home?ver=sfkl&token=admin1abc&imei
     * =sdf 获取用户中心数据
     *
     * @param args          包含参数 顺序如下 ver : 版本 token : 令牌 imei :手机IMEI
     * @param listener
     * @param errorListener
     */
    public void getUserInfo(Context context, Listener<String> listener,
                            ErrorListener errorListener, String... args) {
        LogUtil.d(LogUtil.TAG, "ִ执行用户中心...");
        // new AsyncHttpClient().get(CommonUtil.APPURL + "/user_home?ver="
        // + args[0] + "&token=" + args[1] + "&imei=" + args[2], handler);
        new VolleyHttp(context).getJSONObject(CommonUtil.APPURL
                + "/user_home?ver=" + args[0] + "&token=" + args[1] + "&imei="
                + args[2], listener, errorListener);
    }

    /**
     * 更换用户头像
     *
     * @param token
     * @param file
     * @param handler
     */

    public void changePhoto(String token, File file,
                            ResponseHandlerInterface handler) {
        System.out.println("修改头像");
        AsyncHttpClient httpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        try {
            // params.put("token",token);
            params.put("portrait", file);
            System.out.println("===========================");
            httpClient.post(CommonUtil.APPURL + "/user_image?token=" + token,
                    params, handler);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更换用户头像
     *
     * @param token
     * @param file
     */

    public void changePhoto(Context context, String token, File file,
                            Listener<String> listener, ErrorListener errorListener) {
        System.out.println("修改头像");
        new VolleyHttp(context).upLoadImage(CommonUtil.APPURL
                + "/user_image?token=" + token, file, listener, errorListener);
        // AsyncHttpClient httpClient = new AsyncHttpClient();
        // RequestParams params = new RequestParams();
        // try {
        // // params.put("token",token);
        // params.put("portrait", file);
        // System.out.println("===========================");
        // httpClient.post(CommonUtil.APPURL + "/user_image?token=" + token,
        // params, handler);
        // } catch (FileNotFoundException e) {
        // e.printStackTrace();
        // }
    }

}
