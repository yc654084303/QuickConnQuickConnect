package com.huichuang.http;
import java.util.HashMap;
import java.util.Map;

import com.huichuang.log.L;

/**
 * 网络请求结果回调接口
 * @author yangcan
 */
public abstract class Callback implements RequestInterface{
	@SuppressWarnings("unused")
	void invoke(Map<String, Object>  result) {
		
		L.e(result.toString());
		if (result==null) {
			result=new HashMap<String, Object>();
			result.put("returnCode", REQUESCODE_NO);
			result.put("comments","网络中断");
			onFail(result);
			return;
		}
		if (result.get("returnCode") .equals(REQUESCODE_OK) ){
			onSuccess(result);
		} else {
			onFail(result);
		}
	}
	protected abstract void onSuccess(Map<String, Object> result); 
	protected abstract void onFail(Map<String, Object> result) ;
}
