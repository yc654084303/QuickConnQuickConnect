package com.huichuang.test;

import newlandSdk.listener.OperaListener;
import newlandSdk.mainapp.NewlandPOSMainActivity;
import newlandSdk.resources.BluetoothImpl;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;

import com.huichuang.base.BaseActivity;
import com.huichuang.quickconnectpay.R;
import com.huihuang.utils.Mtoast;
@EActivity(R.layout.transactionflow)
public class TransactionflowActivity extends BaseActivity{
	private BluetoothImpl bluetoothTools;
	private OperaListener operaListener;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bluetoothTools = new BluetoothImpl(this);
		operaListener = new OperaListener(bluetoothTools, this);
		processingUnLock();
	}
	@Click(R.id.butconsume)
	void mybutconsume(){
		bluetoothTools.startDiscovery();
	}
		@Override
	public String appendInteractiveInfoAndShow(final String string, final int messageTag) {
			updateUi(string, messageTag);
			return string;
	}
	@UiThread //UI线程
    void updateUi(String message, int messageTag) {
		 setProgressBarIndeterminateVisibility(false);
	     Mtoast.showShort(TransactionflowActivity.this, message);
		    }
	@UiThread //UI线程
    void setupdateUi(String message, int messageTag) {
		final Builder builder = new android.app.AlertDialog.Builder(this);
		Dialog dialog = builder.create();
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		    }
	
}
