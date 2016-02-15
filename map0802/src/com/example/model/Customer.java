package com.example.model;

import com.example.base.BaseModel;

public class Customer extends BaseModel{
	private String id;
	private String name;
	private String pass;
	private String qq;
	private String email;
	private String sign;
	
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getPass() {
		return pass;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getQq() {
		return qq;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail() {
		return email;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}	
	public String getSign() {
		return sign;
	}
}
