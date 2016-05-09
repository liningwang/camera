package com.example.model;

import com.example.base.BaseModel;

public class GongGao extends BaseModel{
	private String title;
	private String content;
	public GongGao() {
		// TODO Auto-generated constructor stub
	}
	public String getTitle(){
		return title;
	}
	public void setTitle(String title){
		this.title = title;
	}
	public String getContent(){
		return content;
	}
	public void setContent(String content){
		this.content = content;
	}
}
