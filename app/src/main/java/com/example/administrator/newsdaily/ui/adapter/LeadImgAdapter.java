package com.example.administrator.newsdaily.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class LeadImgAdapter extends PagerAdapter{
	private List<View> list;
	
	public LeadImgAdapter(List<View> list){
		this.list=list;
	}
	/** 设置控件中显示界面的数量*/
	@Override
	public int getCount() {
		return list.size();
	}
	/** 判断是否由对象生成界面*/
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==(View)arg1;
	}
	/**销毁position位置的界面*/
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(list.get(position));
	}
	/** 初始化position位置的界面*/
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view=list.get(position);
		container.addView(view);
		return view;
	}
	
}
