package com.example.administrator.newsdaily.model.biz;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;

import com.example.administrator.newsdaily.common.CommonUtil;
import com.example.administrator.newsdaily.model.httpclient.AsyncHttpClient;
import com.example.administrator.newsdaily.model.httpclient.ResponseHandlerInterface;
import com.example.administrator.newsdaily.model.volleyhttp.VolleyHttp;
import com.example.administrator.newsdaily.volley.Response.ErrorListener;
import com.example.administrator.newsdaily.volley.Response.Listener;

public class UpdateManager {
	
	/**
	 * 下载版本
	 * @param context
	 * @param url 下载地址
	 */
	public static void downLoad(Context context, String url) {
		DownloadManager manager = (DownloadManager) context
				.getSystemService(Context.DOWNLOAD_SERVICE); // 初始化下载管理器
		DownloadManager.Request request = new DownloadManager.Request(
				Uri.parse(url));// 创建请求
		// 设置允许使用的网络类型，wifi
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
		// 在通知栏显示下载详情  在API 11中被setNotificationVisibility()取代
		request.setShowRunningNotification(true);
		// 显示下载界面 
		request.setVisibleInDownloadsUi(true);
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-ddhh-mm-ss");
		String date = dateformat.format(new Date());
		//设置下载后文件存放的位置--如果目标位置已经存在这个文件名，则不执行下载，所以用date类型随机取名。
		request.setDestinationInExternalFilesDir(context, null, date + ".apk");
		manager.enqueue(request);// 将下载请求放入队列
	}

	/**
	 * 判断是否更新
	 * @param url 请求路径地址
	 * @param responseHandler 回调接口
	 * @param args 请求参数 ，顺序如下：arg[0] : IMEI ,  arg[1] : pkg , arg[2] : ver 
	 */
	public static void judgeUpdate( ResponseHandlerInterface responseHandler  , String ...args){
		String url = CommonUtil.APPURL+"/update?imei="+args[0]+"&pkg="+args[1]+"&ver="+args[2];
		new AsyncHttpClient().get(url, responseHandler);
	}
	
	/**
	 * 判断是否更新
	 * @param url 请求路径地址
	 * @param listener 成功回调接口
	 * @param errorListener 失败回调接口
	 * @param args 请求参数 ，顺序如下：arg[0] : IMEI ,  arg[1] : pkg , arg[2] : ver 
	 */
	public static void judgeUpdate(Context context, Listener<String> listener ,ErrorListener errorListener, String ...args){
		String url = CommonUtil.APPURL+"/update?imei="+args[0]+"&pkg="+args[1]+"&ver="+args[2];
//		new AsyncHttpClient().get(url, responseHandler);
		new VolleyHttp(context).getJSONObject(url, listener, errorListener);
	}
}
