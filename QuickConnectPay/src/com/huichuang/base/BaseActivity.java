package com.huichuang.base;

import newlandSdk.common.Const.MessageTag;
import newlandSdk.mainapp.NewlandPOSMainActivity;
import newlandSdk.resources.AudioImpl;
import newlandSdk.resources.BluetoothImpl;
import newlandSdk.resources.K21Impl;
import newlandSdk.resources.UsbImpl;

import com.huichuang.inter.ConsInterface;
import com.huihuang.utils.Mtoast;

import android.app.Activity;
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
@Override
protected void onCreate(Bundle savedInstanceState) {
	Mtoast.isShow=true;
	super.onCreate(savedInstanceState);
}
public void toActivity(Class<?> cls,Bundle bun){
	Intent intent=new Intent(this,cls);
	startActivity(intent);
}
public void appendInteractiveInfoAndShow(final String string, final int messageTag) {
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
//			mTextView.setText(Html.fromHtml(deviceInteraction));
		}
	});
}
/**
 * 设置成处理中状态
 * 
 * @since ver1.0
 */
public void btnStateToProcessing() {
	runOnUiThread(new Runnable() {
		@Override
		public void run() {
//			btnConnect.setEnabled(false);
//			btnDisconnect.setEnabled(false);
//			processing = true;
		}
	});
}

/**
 * 设置成等待初始化结束状态
 * 
 * @since ver1.0
 */
public void btnStateToWaitingInitFinished() {
	runOnUiThread(new Runnable() {
		@Override
		public void run() {
//			btnConnect.setEnabled(false);
//			btnDisconnect.setEnabled(false);
//			processing = true;
		}
	});
}

/**
 * 设置成初始化结束状态
 * 
 * @since ver1.0
 */
public void btnStateInitFinished() {
	runOnUiThread(new Runnable() {
		@Override
		public void run() {
//			btnConnect.setEnabled(false);
//			btnDisconnect.setEnabled(true);
//			processing = false;
		}
	});

}

/**
 * 设置成设备销毁状态
 * 
 * @since ver1.0
 */
public void btnStateDestroyed() {
	runOnUiThread(new Runnable() {
		@Override
		public void run() {
//			btnConnect.setEnabled(true);
//			btnDisconnect.setEnabled(false);
//			processing = true;
		}
	});

}

public void btnStateToWaitingConn() {
	runOnUiThread(new Runnable() {
		@Override
		public void run() {
//			btnConnect.setEnabled(false);
//			btnDisconnect.setEnabled(false);
		}
	});

}

public void btnStateConnectFinished() {
	runOnUiThread(new Runnable() {
		@Override
		public void run() {
//			btnConnect.setEnabled(false);
//			btnDisconnect.setEnabled(true);
		}
	});

}

public void btnStateDisconnected() {
	runOnUiThread(new Runnable() {
		@Override
		public void run() {
//			btnConnect.setEnabled(true);
//			btnDisconnect.setEnabled(false);
		}
	});

}

/**
 * 显示消息
 * */
public void showToast(final String message) {
	runOnUiThread(new Runnable() {
		@Override
		public void run() {
			Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
		}
	});
}

//public Button getBtnConnect() {
//	return btnConnect;
//}

public Boolean getProcessing() {
	return processing;
}

public void setProcessing(Boolean processing) {
	this.processing = processing;
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

public void doPinInputShower(final boolean isNext) {
	runOnUiThread(new Runnable() {
		@Override
		public void run() {
//			if (isNext) {
//				deviceInteraction = "*" + deviceInteraction;
//				mTextView.setText(Html.fromHtml(deviceInteraction));
//			} else {
//				deviceInteraction = deviceInteraction.substring(1, deviceInteraction.length());
//				mTextView.setText(Html.fromHtml(deviceInteraction));
//			}

		}
	});
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
}
