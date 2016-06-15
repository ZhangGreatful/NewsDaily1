package com.example.administrator.newsdaily.model.entity;

/**
 * 新闻类型的实体
 *
 */
public class SubType{
	
	private int subid;
	private String subgroup;
	
	public SubType(int subid, String subgroup) {
		this.subid = subid;
		this.subgroup = subgroup;
	}
	
	public int getSubid() {
		return subid;
	}
	public String getSubgroup() {
		return subgroup;
	}
	
}