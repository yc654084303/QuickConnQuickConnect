package com.huichuang.test.bean;

import java.util.HashMap;
import java.util.Map;

public class RegisterModel {
	private String requestcode;
	private String temcurrent;
	private String	loginname;
	private String	password;
	private String mobilenum;
	private String 	validcode;
	 private String trade_id;//交易代码
	public String getTrade_id() {
		return trade_id;
	}
	public void setTrade_id(String trade_id) {
		this.trade_id = trade_id;
	}
	public String getRequestcode() {
		return requestcode;
	}
	public void setRequestcode(String requestcode) {
		this.requestcode = requestcode;
	}
	public String getTemcurrent() {
		return temcurrent;
	}
	public void setTemcurrent(String temcurrent) {
		this.temcurrent = temcurrent;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMobilenum() {
		return mobilenum;
	}
	public void setMobilenum(String mobilenum) {
		this.mobilenum = mobilenum;
	}
	public String getValidcode() {
		return validcode;
	}
	public void setValidcode(String validcode) {
		this.validcode = validcode;
	}


	
}
