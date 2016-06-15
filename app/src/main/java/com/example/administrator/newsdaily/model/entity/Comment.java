package com.example.administrator.newsdaily.model.entity;

/**
 * 评论的实体
 */
public class Comment {
	
/*	public Comment(int cid, int cnid, String ccontent, String cdate,
			String cauthor, int cuserid) {
		this.cid = cid;
		this.cnid = cnid;
		this.ccontent = ccontent;
		this.cdate = cdate;
		this.cauthor = cauthor;
		this.cuserid = cuserid;
	}
	public int cid;
	public int cnid;
	public String ccontent;
	public String cdate;
	public String cauthor;
	public int cuserid;*/
	/*
	“cid”:评论编号,
	“uid”:评论者名字,
	“portrait”:用户头像链接,
	“stamp”:评论时间,
	“content":评论内容
	 */
	
	private int cid ;
	private String uid;
	private String portrait;
	private String stamp;
	private String content;
	
	
	public Comment() {
		
	}
	
	public Comment(int cid, String uid, String portrait, String stamp,
			String content) {
		this.cid = cid;
		this.uid = uid;
		this.portrait = portrait;
		this.stamp = stamp;
		this.content = content;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getPortrait() {
		return portrait;
	}
	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}
	public String getStamp() {
		return stamp;
	}
	public void setStamp(String stamp) {
		this.stamp = stamp;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
	
	
	
	
}
