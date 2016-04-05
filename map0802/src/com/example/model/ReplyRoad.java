package com.example.model;

import com.example.base.BaseModel;

public class ReplyRoad extends BaseModel{
	private String uptime;
	private String id;
	private String safeid;
	private String name;
	private String content;
	private String scan;
	public ReplyRoad() {
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
	public String getSafeid(){
		return safeid;
	}
	public void setSafeid(String safeId){
		this.safeid = safeId;
	}
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getContent(){
		return content;
	}
	public void setContent(String content){
		this.content = content;
	}
	public String getScan(){
		return scan;
	}
	public void setScan(String scan){
		this.scan = scan;
	}
}
