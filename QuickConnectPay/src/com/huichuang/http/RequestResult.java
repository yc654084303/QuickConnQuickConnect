package com.huichuang.http;




import java.util.List;
import java.util.Map;

import com.huichuang.test.bean.BackrParameterModel;


public class RequestResult implements RequestInterface {
	private  String returnCode;//状态码
	private  String comments;//提示信息
	private  String resTime;//时间
	private  BackrParameterModel respBody;//返回的数据

	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getResTime() {
		return resTime;
	}
	public void setResTime(String resTime) {
		this.resTime = resTime;
	}
	public BackrParameterModel getRespBody() {
		return respBody;
	}
	public void setRespBody(BackrParameterModel respBody) {
		this.respBody = respBody;
	}


	

	
	
}
