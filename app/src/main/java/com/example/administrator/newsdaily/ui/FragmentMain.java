package com.example.administrator.newsdaily.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.newsdaily.R;
import com.example.administrator.newsdaily.common.CommonUtil;
import com.example.administrator.newsdaily.common.LogUtil;
import com.example.administrator.newsdaily.common.SystemUtils;
import com.example.administrator.newsdaily.model.biz.NewsManager;
import com.example.administrator.newsdaily.model.biz.parser.ParserNews;
import com.example.administrator.newsdaily.model.dao.NewsDBManager;
import com.example.administrator.newsdaily.model.entity.News;
import com.example.administrator.newsdaily.model.entity.SubType;
import com.example.administrator.newsdaily.ui.adapter.NewsAdapter;
import com.example.administrator.newsdaily.ui.adapter.NewsTypeAdapter;
import com.example.administrator.newsdaily.view.HorizontalListView;
import com.example.administrator.newsdaily.view.xlistview.XListView;
import com.example.administrator.newsdaily.view.xlistview.XListView.IXListViewListener;
import com.example.administrator.newsdaily.volley.Response.ErrorListener;
import com.example.administrator.newsdaily.volley.Response.Listener;
import com.example.administrator.newsdaily.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

/**新闻列表界面**/
public class FragmentMain extends Fragment{
	//填充view
	private View view;
	//新闻列表
	private XListView listView;
	//新闻适配器
	private NewsAdapter newsAdapter;
	//分类列表
	private HorizontalListView hl_type;
	//分类适配器
	private NewsTypeAdapter typeAdapter;
	//新闻分类编号 默认为1
	private int subId = 1;
	//模式  1上拉 2下拉
    private int mode;
    //数据库
    private NewsDBManager dbManager;
    //当前Activity
    private ActivityMain mainActivity;
    //更多分类
    private ImageView btn_moretype;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_newslist,container,false);
		dbManager = new NewsDBManager(getActivity());
		mainActivity = (ActivityMain)getActivity();
		hl_type = (HorizontalListView) view.findViewById(R.id.hl_type);
		listView= (XListView) view.findViewById(R.id.listview);
		btn_moretype = (ImageView) view.findViewById(R.id.iv_moretype);
		btn_moretype.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mainActivity.showFragmentType();
			}
		});
		if(hl_type != null) {
			typeAdapter = new NewsTypeAdapter(getActivity());
			hl_type.setAdapter(typeAdapter);
			hl_type.setOnItemClickListener(typeItemListener);
		}
		//加载新闻分类
		loadNewsType();
		if(listView != null){
			newsAdapter=new NewsAdapter(getActivity(), listView);
			listView.setAdapter(newsAdapter);
			listView.setPullRefreshEnable(true);
			listView.setPullLoadEnable(true);
			listView.setXListViewListener(listViewListener);			
			listView.setOnItemClickListener(newsItemListener);
		}
		//加载新闻列表
		loadNextNews(true);
		mainActivity.showLoadingDialog(mainActivity, "加载中", false);
		return view;
	}
	
	private IXListViewListener listViewListener=new IXListViewListener() {
		@Override
		public void onRefresh() {
			//加载数据。。。。。。。。。。。。。。。。。。。
			loadNextNews(false);
			// 加载完毕
			listView.stopLoadMore();
			listView.stopRefresh();
			listView.setRefreshTime(CommonUtil.getSystime());

		}
		@Override
		public void onLoadMore() {
			//加载数据。。。。。。。。。。。。。。。。。。。
			loadPreNews();
			listView.stopLoadMore();
			listView.stopRefresh();
		}
	};
	
	/**
	 * 分类单项点击事件
	 */
	private OnItemClickListener typeItemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			SubType subType = (SubType) parent.getItemAtPosition(position);
			subId = subType.getSubid();
			typeAdapter.setSelectedPosition(position);
			typeAdapter.update();
			loadNextNews(true);
			mainActivity.showLoadingDialog(mainActivity, "加载中", false);
		}
	};

	/**
	 * 新闻单项点击事件
	 */
	private OnItemClickListener newsItemListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// 打开显示当前选中的新闻
			News news = (News) parent.getItemAtPosition(position);
			Intent intent = new Intent(getActivity(), ActivityShow.class);
			intent.putExtra("newsitem", news);
			getActivity().startActivity(intent);
		}
	};

	/**
	 * 加载新闻类型
	 */
	protected void loadNewsType() {
		if (dbManager.queryNewsType().size() == 0) {
			if (SystemUtils.getInstance(getActivity()).isNetConn()) {
				System.out.println("loadNewsType");
				NewsManager.loadNewsType(getActivity(),
						new VolleyTypeResponseHandler(),new VolleyErrorHandler());
			}
		} else {
			List<SubType> types = dbManager.queryNewsType();
			typeAdapter.appendData(types, true);
			typeAdapter.update();
		}
	}

	/**
	 * 加载先前的新闻数据
	 */
	protected void loadPreNews() {
		if (listView.getCount() - 2 <= 0)
			return;
		int nId = newsAdapter.getItem(listView.getLastVisiblePosition() - 2)
				.getNid();
		mode = NewsManager.MODE_PREVIOUS;
		if (SystemUtils.getInstance(getActivity()).isNetConn()) {
			NewsManager.loadNewsFromServer(getActivity(),mode, subId, nId,
					new VolleyResponseHandler(),new VolleyErrorHandler());
		} else {
			NewsManager.loadNewsFromsLocal(mode, nId,
					new MyLocalResponseHandler());
		}
	}

	/**
	 * 加载新的数据
	 */
	protected void loadNextNews(boolean isNewType) {
		int nId = 1;
		if (!isNewType) {
			if (newsAdapter.getAdapterData().size() > 0) {
				nId = newsAdapter.getItem(0).getNid();
			}
		}
		mode = NewsManager.MODE_NEXT;
		if (SystemUtils.getInstance(getActivity()).isNetConn()) {
			NewsManager.loadNewsFromServer(getActivity(),mode, subId, nId,
					new VolleyResponseHandler(),new VolleyErrorHandler() );
		} else {
			NewsManager.loadNewsFromsLocal(mode, nId,
					new MyLocalResponseHandler());
		}
	}

	class VolleyTypeResponseHandler implements Listener<String> {

		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			LogUtil.d("TYPE", "TYPE Response = " + response);
			List<SubType> types = ParserNews.parserTypeList(response);
			dbManager.saveNewsType(types);
			typeAdapter.appendData(types, true);
			typeAdapter.update();
		}
	}

	/**
	 * 
	 * Volley成功，新闻列表回调接口实现类
	 * 
	 */

	class VolleyResponseHandler implements Listener<String> {

		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			List<News> data = ParserNews.parserNewsList(response);
			boolean isClear = mode == NewsManager.MODE_NEXT ? true : false;
			newsAdapter.appendData((ArrayList<News>) data, isClear);
			mainActivity.cancelDialog();
			newsAdapter.update();
		}
	}

	class VolleyErrorHandler implements ErrorListener {

		@Override
		public void onErrorResponse(VolleyError error) {
			// TODO Auto-generated method stub
			mainActivity.cancelDialog();
			mainActivity.showToast("服务器连接异常");
		}

	}


	public class MyLocalResponseHandler implements
			NewsManager.LocalResponseHandler {
		public void update(ArrayList<News> data, boolean isClearOld) {
			newsAdapter.appendData(data, isClearOld);
			newsAdapter.update();
			if (data.size() <= 0) {
				Toast.makeText(getActivity(), "请先设置网络连接", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

}
