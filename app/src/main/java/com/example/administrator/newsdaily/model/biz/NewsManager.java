package com.example.administrator.newsdaily.model.biz;

import android.content.Context;

import com.example.administrator.newsdaily.common.CommonUtil;
import com.example.administrator.newsdaily.common.SystemUtils;
import com.example.administrator.newsdaily.model.entity.News;
import com.example.administrator.newsdaily.model.volleyhttp.VolleyHttp;
import com.example.administrator.newsdaily.volley.Response.ErrorListener;
import com.example.administrator.newsdaily.volley.Response.Listener;

import java.util.ArrayList;

public class NewsManager {
	public static final int MODE_NEXT = 1;
	public static final int MODE_PREVIOUS = 2;

	/**
	 * news_sort?ver=版本号&imei=手机标识符 加载新闻分类
	 * 
	 * @param context
	 *            :上下文
	 * @param listener
	 *            :成功回调接口
	 * @param errorListener
	 *            :失败回调接口
	 * 
	 *            http://118.244.212.82:9092/newsClient/news_sort?ver=1&imei=1
	 */
	public static void loadNewsType(Context context, Listener<String> listener,
			ErrorListener errorListener) {
		int ver = CommonUtil.VERSION_CODE;
		String imei = SystemUtils.getIMEI(context);
		VolleyHttp http = new VolleyHttp(context);
		http.getJSONObject(CommonUtil.APPURL + "/news_sort?ver=" + ver
				+ "&imei=" + imei, listener, errorListener);
	}

	/**
	 * 从服务器加载新闻
	 * news_list?ver=版本号&gid=分类名&dir=1&nid=新闻id&stamp=20140321&cnt=20 加载新闻数据
	 * 
	 * @param mode
	 *            模式/方向
	 * @param nid
	 *            分类号
	 * @param nid
	 *            新闻id
	 * @param listener
	 *            成功回调接口
	 * @param errorListener
	 *            失败回调接口
	 */
	public static void loadNewsFromServer(Context context, int mode, int subId,
			int nid, Listener<String> listener, ErrorListener errorListener) {
		// 版本号
		int ver = CommonUtil.VERSION_CODE;
		String stamp = CommonUtil.getDate();
		VolleyHttp http = new VolleyHttp(context);
		http.getJSONObject(CommonUtil.APPURL + "/news_list?ver=" + ver
				+ "&subid=" + subId + "&dir=" + mode + "&nid=" + nid
				+ "&stamp=" + stamp + "&cnt=" + 20, listener, errorListener);
	}

	/**
	 * 从本地加载数据
	 * @param mode
	 * @param curId
	 * @param handler
     */
	public static void loadNewsFromsLocal(int mode, int curId,
			LocalResponseHandler handler) {
		System.out.println("数据库加载");
		if (mode == MODE_NEXT) {

		} else if (mode == MODE_PREVIOUS) {

		}
	}

	public interface LocalResponseHandler {
		public void update(ArrayList<News> data, boolean isCliearOld);
	}

}
