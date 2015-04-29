package com.huichuang.test.bean;
/**
 * 签到
 * @author yangcan
 *
 */
public class SignModel {
	private String requestcode;
	private String 	temcurrent;
	private String 	userid;
	private String 	termsn;
	private String  merchantid;
	private String sessionid;
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
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getTermsn() {
		return termsn;
	}
	public void setTermsn(String termsn) {
		this.termsn = termsn;
	}
	public String getMerchantid() {
		return merchantid;
	}
	public void setMerchantid(String merchantid) {
		this.merchantid = merchantid;
	}
	public String getSessionid() {
		return sessionid;
	}
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	
	
}
