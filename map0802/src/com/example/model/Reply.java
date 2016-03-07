package com.example.model;

import com.example.base.BaseModel;

public class Reply extends BaseModel{
	private String uptime;
	private String id;
	private String commentId;
	private String name;
	private String content;
	public Reply() {
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
	public String getCommentId(){
		return commentId;
	}
	public void setCommentId(String commentId){
		this.commentId = commentId;
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
}
