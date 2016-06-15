package com.example.administrator.newsdaily.model.entity;

import java.util.List;

public class NewsType {

	/**
	 * 分类号
	 */
	private int gid;
	/**
	 * 分类名
	 */
	private String group;
	/**
	 * 子对象
	 */
	private List<SubType> subgrp;
	

	public NewsType(int gid, String group) {
		this.gid = gid;
		this.group = group;
	}
	
	public int getGid() {
		return gid;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public List<SubType> getSubgrp() {
		return subgrp;
	}

	public void setSubgrp(List<SubType> subgrp) {
		this.subgrp = subgrp;
	}
	
}
