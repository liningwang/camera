package com.wangln.model;

import com.wangln.base.BaseModel;

public class Comment extends BaseModel{
	private String uptime;
	private String id;
	private String customerid;
	private String name;
	private String content;
	private String cameraid;
	private String replycount;
	public Comment() {
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
	public void setCustomerid(String customerid){
		this.customerid = customerid;
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
	public String getCameraid(){
		return cameraid;
	}
	public void setCameraid(String cameraid){
		this.cameraid = cameraid;
	}
	public String getReplycount(){
                return replycount;
        }
        public void setReplycount(String replycount){
                this.replycount = replycount;
        }

}
