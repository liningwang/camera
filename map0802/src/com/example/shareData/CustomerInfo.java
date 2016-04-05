package com.example.shareData;

import android.annotation.SuppressLint;
import android.app.Application;

public class CustomerInfo extends Application {
	private String user = "";
	private String sign = "";
	private int customerId = 0;
	private String qq = "";
	private String email = "";
	private String cameraName = "";
	private String cameraUptime = "";
	private String cameraDirecton = "";
	private String cameraAddress = "";
	private String cameraId = "";
	private int count = 1;
	public void setCameraId(String cameraId) {
		this.cameraId = cameraId;
	}
	public String getCameraId() {
		return this.cameraId;
	}
	public void setCameraName(String cameraName) {
		this.cameraName = cameraName;
	}
	public String getCameraName() {
		return this.cameraName;
	}
	public void setCameraUptime(String cameraUptime) {
		this.cameraUptime = cameraUptime;
	}
	public String getCameraUptime() {
		return this.cameraUptime;
	}
	public void setCameraDirecton(String cameraDirecton) {
		this.cameraDirecton = cameraDirecton;
	}
	public String getCameraDirecton() {
		return this.cameraDirecton;
	}
	public void setCameraAddress(String cameraAddress) {
		this.cameraAddress = cameraAddress;
	}
	public String getCameraAddress() {
		return this.cameraAddress;
	}	
	public void setUser(String user) {
		this.user = user;
	}	
	public String getUser() {
		return this.user;
	}
	
	public void setSign(String sign) {
		this.sign = sign;
	} 
	public String getSign() {
		return this.sign;
	}

	public void setCustomerid(int customerId) {
		this.customerId = customerId;
	}
	public int getCustomerid() {
		return this.customerId;
	}
	
	public void setQQ(String qq) {
		this.qq = qq;
	}
	public String getQQ() {
		return this.qq;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail() {
		return this.email;
	}
	public void setAllCount(int count) {
		this.count = count;
	}
	public int getAllCount() {
		return count;
	}
	
	@SuppressLint("NewApi")
	public boolean isLogin() {
		if(user.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}
}
