package com.huichuang.http;
import java.util.Map;

class CallbackWrapper extends Callback {
	private final Callback ref;

	public CallbackWrapper(Callback callback) {
		this.ref = callback;
	}

	@Override
	void invoke(Map<String, Object> result) {
		super.invoke(result);
		if (ref != null) {
			ref.invoke(result);
		}
	}

	@Override
	protected void onSuccess(Map<String, Object> result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onFail(Map<String, Object> result) {
		// TODO Auto-generated method stub
		
	}



	
}
