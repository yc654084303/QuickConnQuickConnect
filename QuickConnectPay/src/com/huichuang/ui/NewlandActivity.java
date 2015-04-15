package com.huichuang.ui;

import newlandSdk.common.Const.MessageTag;
import newlandSdk.listener.OperaListener;
import newlandSdk.mainapp.NewlandPOSMainActivity;
import newlandSdk.resources.AudioImpl;
import newlandSdk.resources.BluetoothImpl;
import newlandSdk.uilistener.UIBluetoothImpl;
import newlandSdk.uilistener.UIOperaListener;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huichuang.base.BaseActivity;
import com.huichuang.quickconnectpay.R;
@EActivity(R.layout.newland_view_activity)
/**
 * 
 * @author yangcan
 * 
 *
 */
public class NewlandActivity extends BaseActivity{
	@ViewById EditText gathering_money;
	@ViewById TextView gathering_accountName;
	@ViewById TextView gathering_account;
	@ViewById RelativeLayout gathering_progressbar;
	@ViewById (R.id.option_list) ExpandableListView expandableList;
	private UIOperaListener operaListener;
	private UIBluetoothImpl bluetoothTools;
	private int FLAG = -1;
	
	public Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				operaListener.jiner();
				break;
			default:
				break;
			}
		}
		
	};
	@Override protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		bluetoothTools = new UIBluetoothImpl(this,handler);
				final Builder builder = new android.app.AlertDialog.Builder(this);
				builder.setTitle("请选择设备的连接方式:");
				builder.setSingleChoiceItems(new String[] { "audio", "bluetooth", "im81Connector" , "usbConnector"}, 0, new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						switch (which) {
						case 1: {
							new Thread(new Runnable() {
								@Override
								public void run() {
									FLAG = 1;
									bluetoothTools.startDiscovery();
									operaListener = new UIOperaListener(bluetoothTools, NewlandActivity.this);
									
								}
							}).start();
							break;
						}
						
						
						
						}
					}
			
//				runOnUiThread(new Runnable() {
//					@Override
//					public void run() {
//						Dialog dialog = builder.create();
//						dialog.setCancelable(false);
//						dialog.setCanceledOnTouchOutside(false);
//						dialog.show();
//					}
//				});
			
		});
				 
	}
	
	@Click(R.id.back)
	void myButtonClicked() {
		finish();
	}
}
