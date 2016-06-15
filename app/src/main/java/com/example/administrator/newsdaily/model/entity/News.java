package com.example.administrator.newsdaily.model.entity;

import java.io.Serializable;

/**
 * 新闻类的实体
 */
public class News implements Serializable{

	private static final long serialVersionUID = 1L;
//	  类型标识  1:列表新闻 2：大图新闻
	private int type;
//	新闻id
	private int nid;
//	时间戳
	private String stamp;
//	图标
	private String icon;
//	新闻标题
	private String title;
//	新闻摘要
	private String summary;
//	新闻链接
	private String link;
	
	/**
	 * 
	 * @param type
	 * @param nid
	 * @param stamp
	 * @param icon
	 * @param title
	 * @param summary
	 * @param link
	 */
	public News(int type, int nid, String stamp, String icon, String title,
			String summary, String link) {
		super();
		this.type = type;
		this.nid = nid;
		this.stamp = stamp;
		this.icon = icon;
		this.title = title;
		this.summary = summary;
		this.link = link;
	}

	public int getType() {
		return type;
	}

	public int getNid() {
		return nid;
	}

	public String getStamp() {
		return stamp;
	}

	public String getIcon() {
		return icon;
	}

	public String getTitle() {
		return title;
	}

	public String getSummary() {
		return summary;
	}

	public String getLink() {
		return link;
	}
	
	
}
