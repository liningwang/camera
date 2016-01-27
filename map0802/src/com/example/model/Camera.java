package com.example.model;

import com.example.base.BaseModel;

public class Camera extends BaseModel{
	private String longitude;
	private String latitude;
	private String uptime;
	private String id;
	public Camera() {
		// TODO Auto-generated constructor stub
	}
	public String getLongitude(){
		return longitude;
	}
	public void setLongitude(String longitude){
		this.longitude = longitude;
	}
	public String getLatitude(){
		return latitude;
	}
	public void setLatitude(String latitude){
		this.latitude = latitude;
	}
	public String getUptime(){
		return uptime;
	}
	public void setUptime(String uptime) {
		this.uptime = uptime;
	}
	public String getId(){
		return id;
	}
	public void setId(String id){
		this.id = id;
	}
}
