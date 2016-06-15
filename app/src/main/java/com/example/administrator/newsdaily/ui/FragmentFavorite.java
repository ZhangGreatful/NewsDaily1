package com.example.administrator.newsdaily.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.administrator.newsdaily.R;
import com.example.administrator.newsdaily.model.dao.NewsDBManager;
import com.example.administrator.newsdaily.model.entity.News;
import com.example.administrator.newsdaily.ui.adapter.NewsAdapter;

import java.util.ArrayList;

/**
 * 收藏界面
 * **/
public class FragmentFavorite extends Fragment{
	private View view;
	private ListView listView;
	private NewsAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_favorite,container,false);
		listView=(ListView) view.findViewById(R.id.listview);
		adapter=new NewsAdapter(getActivity(), listView);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(itemListener);
		//TODO 加载数据库
		loadLoveNews();
		return view;
	}
	/**从数据库中加载保存的新闻*/
	private void loadLoveNews() {
		ArrayList<News> data=new NewsDBManager(getActivity()).queryLoveNews();
		adapter.appendData(data, true);
	}

	private OnItemClickListener itemListener=new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			// 打开显示当前选中的新闻
			News news = (News) parent.getItemAtPosition(position);
			Intent intent=new Intent(getActivity(), ActivityShow.class);
			intent.putExtra("newsitem", news);
		    getActivity().startActivity(intent);
		}
	};
}
