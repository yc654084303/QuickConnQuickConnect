package com.huichuang.base;

import newlandSdk.common.Const.MessageTag;
import newlandSdk.mainapp.NewlandPOSMainActivity;
import newlandSdk.resources.AudioImpl;
import newlandSdk.resources.BluetoothImpl;
import newlandSdk.resources.K21Impl;
import newlandSdk.resources.UsbImpl;

import com.huichuang.inter.ConsInterface;
import com.huihuang.utils.Mtoast;
import com.huihuang.utils.NetUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.widget.Button;
import android.widget.Toast;

public class BaseActivity extends Activity implements ConsInterface{
	private BluetoothImpl bluetoothTools;
	private AudioImpl audioTools;
	private K21Impl k21Tools;
	private UsbImpl usbTools;
	private Boolean processing = false;
	private String deviceInteraction = "", newstring;
	private int FLAG = -1;
	private  ProgressDialog waitingDialog;
@Override
protected void onCreate(Bundle savedInstanceState) {
	if (!NetUtils.isConnected(this)) {
		NetUtils.setNetworkMethod(this);
	}
	Mtoast.isShow=true;
	super.onCreate(savedInstanceState);
}
public void toActivity(Class<?> cls,Bundle bun){
	Intent intent=new Intent(this,cls);
	startActivity(intent);
}
public String appendInteractiveInfoAndShow(final String string, final int messageTag) {
	runOnUiThread(new Runnable() {
		@Override
		public void run() {
			switch (messageTag) {
			case MessageTag.NORMAL:
				newstring = "<font color='black'>" + string + "</font>";
				break;
			case MessageTag.ERROR:
				newstring = "<font color='red'>" + string + "</font>";
				break;
			case MessageTag.TIP:
				newstring = "<font color='blue'>" + string + "</font>";
				break;
			case MessageTag.DATA:
				if (string.equals("null")) {
					return;
				}
				String arr[] = string.split(":");
				newstring = "<font color='green'>" + arr[0] + "</font>" + ":" + arr[1];
				break;
			default:
				break;
			}
			deviceInteraction = newstring + "<br>" + deviceInteraction;
			System.out.println(deviceInteraction);
//			mTextView.setText(Html.fromHtml(deviceInteraction));
		}
	});
	return deviceInteraction;
}

public void processingLock() {
	SharedPreferences setting = getSharedPreferences("setting", 0);
	SharedPreferences.Editor editor = setting.edit();
	editor.putBoolean("PBOC_LOCK", true);
	editor.commit();
}
public boolean processingisLocked() {
	SharedPreferences setting = getSharedPreferences("setting", 0);
	if (setting.getBoolean("PBOC_LOCK", true)) {
		return true;
	} else {
		return false;
	}
}
public void processingUnLock() {
	SharedPreferences setting = getSharedPreferences("setting", 0);
	SharedPreferences.Editor editor = setting.edit();
	editor.putBoolean("PBOC_LOCK", false);
	editor.commit();
}
@Override
protected void onDestroy() {
	super.onDestroy();
	if (FLAG == 0) {
		audioTools.disconnect();
	} else if (FLAG == 1) {
		unregisterReceiver(bluetoothTools.getDiscoveryReciever());
		bluetoothTools.disconnect();
	} else if (FLAG == 2) {
		k21Tools.disconnect();
	}

}

//public void showProgressDialog(){
//	showWaitingDialog("加载中...");
//}
/**
 * 显示一个等待的对话框
 */
//protected final void showWaitingDialog() {
//	waitingDialog = new ProgressDialog(this);
//	waitingDialog.setCancelable(false);
//	waitingDialog.setIndeterminate(true);
//	waitingDialog.setMessage(getString(R.string.waiting));
//	waitingDialog.show();
//}
//protected final void showWaitingDialog(String str) {
//	waitingDialog = new ProgressDialog(this);
//	waitingDialog.setCancelable(false);
//	waitingDialog.setIndeterminate(true);
//	waitingDialog.setMessage(str);
//	waitingDialog.show();
//}
///**
// * 关闭等待的对话框
// */
//public final void closeWaitingDialog() {
//	waitingDialog.dismiss();
//	waitingDialog = null;
//}
}
