package com.huichuang.http;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.huichuang.log.L;
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
		L.e("=========================================="+result);
		Map<String, String> map=null;
		try {
			map = XMLPaser.parseXml(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		L.e("=========================================="+map);
		callback.invoke(map);
	}
}
