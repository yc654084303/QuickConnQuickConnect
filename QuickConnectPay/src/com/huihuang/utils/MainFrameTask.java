package com.huihuang.utils;

import android.app.Activity;
import android.view.View;

public class MainFrameTask{
	private Activity mainFrame = null;
	private CustomProgressDialog progressDialog = null;
	
	public MainFrameTask(Activity mainFrame){
		this.mainFrame = mainFrame;
	}
	
//	@Override
//	protected void onCancelled() {
//		stopProgressDialog();
//		super.onCancelled();
//	}
//
//		
//	@Override
//	protected void onPreExecute() {
//		startProgressDialog();
//	}
//
//	@Override
//	protected void onPostExecute(Integer result) {
//		stopProgressDialog();
//	}
	
	public void startProgressDialog(String values){
		if (progressDialog == null){
			progressDialog = CustomProgressDialog.createDialog(mainFrame);
	    	progressDialog.setMessage(values);
		}
    	progressDialog.show();
	}
	
	
	public void stopProgressDialog(){
		if (progressDialog != null){
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
}