package com.huichuang.http;
import java.util.HashMap;
import java.util.Map;

/**
 * 网络请求结果回调接口
 * @author yangcan
 */
public abstract class Callback {
	void invoke(Map<String, Object>  result) {
		if (result==null) {
			result=new HashMap<String, Object>();
			result.put("returnCode", RequestResult.REQUESCODE_NO);
			result.put("comments","网络中断");
			onFail(result);
			return;
		}
		if (result.get("returnCode") == RequestResult.REQUESCODE_OK) {
			onSuccess(result);
		} else {
			onFail(result);
		}
	}
	protected abstract void onSuccess(Map<String, Object> result); 
	protected abstract void onFail(Map<String, Object> result) ;
}
