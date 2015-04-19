package com.huichuang.http;
import java.util.HashMap;
import java.util.Map;

import com.huihuang.utils.XMLPaser;

import android.os.AsyncTask;
/**
 * 
 * @author yangcan
 *
 */
public abstract class AsynRequest extends AsyncTask<Void, Void, String> {
	private Callback callback;

	protected AsynRequest(Callback callback) {
		this.callback = callback;
		execute();
	}

	@Override
	protected final void onPostExecute(String result) {
		super.onPostExecute(result);
		Map<String, Object> map=null;
		try {
			map = XMLPaser.parseXml(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		callback.invoke(map);
	}
}
