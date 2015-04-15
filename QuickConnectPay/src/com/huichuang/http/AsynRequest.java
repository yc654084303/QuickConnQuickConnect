package com.huichuang.http;
import android.os.AsyncTask;
/**
 * 
 * @author yangcan
 *
 */
public abstract class AsynRequest extends AsyncTask<Void, Void, RequestResult> {
	private Callback callback;

	protected AsynRequest(Callback callback) {
		this.callback = callback;
		execute();
	}

	@Override
	protected final void onPostExecute(RequestResult result) {
		super.onPostExecute(result);
		callback.invoke(result);
	}
}
