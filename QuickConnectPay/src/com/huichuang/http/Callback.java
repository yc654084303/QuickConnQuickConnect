package com.huichuang.http;
import java.util.Map;

/**
 * 网络请求结果回调接口
 * @author yangcan
 */
public abstract class Callback {
	void invoke(RequestResult result) {
		if (result.result == RequestResult.OK) {
			onSuccess(result, result.json);
		} else {
			onFail(result);
		}
	}
	protected abstract void onSuccess(RequestResult result, Map<String, Object> json); 
	protected abstract void onFail(RequestResult result) ;
}
