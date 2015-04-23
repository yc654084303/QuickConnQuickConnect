package com.huichuang.test.bean;
/**
 * 
 * @author 返回参数
 *
 */
public class BackrParameterModel {
	private String requestcode;//
	private String token;//
	private String userid;//用户 ID
	private String temcurrent;//终端流水号
	private String sessionid;
	private String trade_id;//交易代码
	private String send_state;//短信状态
	private String termsn;//终端号
	private String merchantid;//商户号
	private String tradetime;//交易时间
	private String receive_part_code;//受理方标识码
	private String serchno;//检索参考号
	private String answer_code;//应答码
	private String terminal_key;//终端密钥
	private String custom60;//自定义域
	
	public String getCustom60() {
		return custom60;
	}

	public void setCustom60(String custom60) {
		this.custom60 = custom60;
	}
	//已经自行实例化   ;
	private static final BackrParameterModel single = new BackrParameterModel(); 
	/**
	 * 实例
	 * @return
	 */
	public static BackrParameterModel getInstance() {  
	    return single;  
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
	public String getTradetime() {
		return tradetime;
	}
	public void setTradetime(String tradetime) {
		this.tradetime = tradetime;
	}
	public String getReceive_part_code() {
		return receive_part_code;
	}
	public void setReceive_part_code(String receive_part_code) {
		this.receive_part_code = receive_part_code;
	}
	public String getSerchno() {
		return serchno;
	}
	public void setSerchno(String serchno) {
		this.serchno = serchno;
	}
	public String getAnswer_code() {
		return answer_code;
	}
	public void setAnswer_code(String answer_code) {
		this.answer_code = answer_code;
	}
	public String getTerminal_key() {
		return terminal_key;
	}
	public void setTerminal_key(String terminal_key) {
		this.terminal_key = terminal_key;
	}
	public static BackrParameterModel getSingle() {
		return single;
	}
public BackrParameterModel() {
}
public String getTrade_id() {
	return trade_id;
}
public void setTrade_id(String trade_id) {
	this.trade_id = trade_id;
}
public String getSend_state() {
	return send_state;
}
public void setSend_state(String send_state) {
	this.send_state = send_state;
}
@Override
public String toString() {
	return trade_id+","+send_state;
}
public String getRequestcode() {
	return requestcode;
}
public void setRequestcode(String requestcode) {
	this.requestcode = requestcode;
}
public String getToken() {
	return token;
}
public void setToken(String token) {
	this.token = token;
}
public String getUserid() {
	return userid;
}
public void setUserid(String userid) {
	this.userid = userid;
}
public String getTemcurrent() {
	return temcurrent;
}
public void setTemcurrent(String temcurrent) {
	this.temcurrent = temcurrent;
}
public String getSessionid() {
	return sessionid;
}
public void setSessionid(String sessionid) {
	this.sessionid = sessionid;
}

}
