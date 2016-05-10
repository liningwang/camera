package com.example.model;

import com.example.base.BaseModel;

public class UpdateApk extends BaseModel{
	private String id;
	private String verName; 
	private String verCode;
	private String flag;
	public UpdateApk() {
		// TODO Auto-generated constructor stub
	}
	public String getId(){
		return id;
	}
	public void setId(String id){
		this.id = id;
	}
	public String getVerName(){
		return verName;
	}
	public void setVerName(String verName){
		this.verName = verName;
	}
	public String getVerCode(){
		return verCode;
	}
	public void setVerCode(String verCode){
		this.verCode = verCode;
	}
	public String getFlag(){
		return flag;
	}
	public void setFlag(String flag){
		this.flag = flag;
	}
}
