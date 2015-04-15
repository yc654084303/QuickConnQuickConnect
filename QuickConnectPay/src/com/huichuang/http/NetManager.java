package com.huichuang.http;

import java.util.Map;

import com.huichuang.inter.ConsInterface;
import com.huichuang.log.L;
import com.huihuang.utils.HttpUtils;

public class NetManager implements ConsInterface{

	public static void reqPostRegister( final Map<String, String> data, final Callback callback) {
		new AsynRequest(new CallbackWrapper(callback)) {
			@Override
			protected RequestResult doInBackground(Void... params) {
				RequestResult result = null;
				try {
					result=HttpUtils.doPostByHttpUrlConnection(HTTP_URL, data);
				} catch (Exception e) {
					L.e("网络请求异常"+e.toString());
					e.printStackTrace();
				}
				return result ;
			}
		};
	}
		public static void reqGetRegister( final Map<String, String> data, final Callback callback) {
			new AsynRequest(new CallbackWrapper(callback)) {
				@Override
				protected RequestResult doInBackground(Void... params) {
					RequestResult result = null;
					try {
						result = HttpUtils.doGetByhttpUrlConnection(HTTP_URL, data);
					} catch (Exception e) {
						L.e("网络请求异常"+e.toString());
						e.printStackTrace();
					}
					return result ;
				}
			};
	}
	
	
}
