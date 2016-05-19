package com.wangln.model;

import com.wangln.base.BaseModel;

public class SafeRoad extends BaseModel{
	private String uptime;
	private String id;
	private String customerid;
	private String url;
	private String content;
	private String username;
	private String replycount;
	public SafeRoad() {
		// TODO Auto-generated constructor stub
	}
	public String getUptime(){
		return uptime;
	}
	public void setUptime(String uptime){
		this.uptime = uptime;
	}
	public String getId(){
		return id;
	}
	public void setId(String id){
		this.id = id;
	}
	public String getCustomerid(){
		return customerid;
	}
	public void setCustomerid(String commentId){
		this.customerid = commentId;
	}
	public String getUrl(){
		return url;
	}
	public void setUrl(String url){
		this.url = url;
	}
	public String getContent(){
		return content;
	}
	public void setContent(String content){
		this.content = content;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsername(){
		return username;
	} 
	public String getReplycount(){
		return replycount;
	} 
	public void setReplycount(String replycount){
		this.replycount = replycount;
	} 
}
