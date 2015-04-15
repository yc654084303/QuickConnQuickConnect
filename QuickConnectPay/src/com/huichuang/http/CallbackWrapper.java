package com.huichuang.http;
import java.util.Map;

class CallbackWrapper extends Callback {
	private final Callback ref;

	public CallbackWrapper(Callback callback) {
		this.ref = callback;
	}

	@Override
	void invoke(RequestResult result) {
		super.invoke(result);
		if (ref != null) {
			ref.invoke(result);
		}
	}

	@Override
	protected void onSuccess(RequestResult result, Map<String, Object> json) {
	}

	@Override
	protected void onFail(RequestResult result) {
	}
}
