package com.huichuang.test.bean;
/**
 * 
 * @author 短信返回参数
 *
 */
public class BackrParameterModel {
private String trade_id;//交易代码
private String send_state;//终端流水号
public BackrParameterModel() {
}
//已经自行实例化   
private static final BackrParameterModel single = new BackrParameterModel();  
/**
 * 短信
 * @return
 */
public static BackrParameterModel getInstance() {  
    return single;  
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

}
