package com.huichuang.http;



import java.util.List;
import java.util.Map;

import com.huihuang.utils.JSON;
public class RequestResult {
	private static final int ERR_CONNECT_FAIL = 1000;// 连接失败
	public static final int ERR_SERVER_WRONG = 1001;// 服务器错误
	public static final int ERR_INVALID_TOKEN = -1;// 无效的token
	public static final int OK = 1;
	public final int result;
	public final String failure_cause;
	public final Map<String, Object> json;

	public RequestResult(Map<String, Object> json) {
		if (json == null) {
			result = ERR_CONNECT_FAIL;
			failure_cause = "连接服务器失败";
		} else if (json.size() == 0) {
			json = null;
			result = ERR_SERVER_WRONG;
			failure_cause = "服务器错误";
		} else {
			result = JSON.getInt(json, "result");
			failure_cause = JSON.getString(json, "failure_cause", null);
		}
		this.json = json;
	}

	public int getInt(String name) {
		return JSON.getInt(json, name);
	}

	public long getLong(String name) {
		return JSON.getLong(json, name);
	}

	public String getString(String name) {
		return JSON.getString(json, name);
	}

	@SuppressWarnings("unchecked")
	public List<Object> getArray(String name) {
		return (List<Object>) json.get(name);
	}
}
