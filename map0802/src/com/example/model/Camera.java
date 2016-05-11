package com.example.model;

import com.example.base.BaseModel;

public class Camera extends BaseModel{
	private String longitude;
	private String latitude;
	private String uptime;
	private String id;
	private String name;
	private String address;
	private String direction;
	private String type;
	private String zan;
	private String buzan;
	private String username;
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
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getAddress(){
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDirection(){
		return direction;
	}
	public void setDirection(String direction){
		this.direction = direction;
	}
	public String getType(){
		return type;
	}
	public void setType(String type){
		this.type = type;
	}
	public String getZan(){
		return zan;
	}
	public void setZan(String zan){
		this.zan = zan;
	}
	public String getBuzan(){
		return buzan;
	}
	public void setBuzan(String buzan){
		this.buzan = buzan;
	}
	public String getUsername(){
		return username;
	}
	public void setUsername(String username){
		this.username = username;
	}
}
