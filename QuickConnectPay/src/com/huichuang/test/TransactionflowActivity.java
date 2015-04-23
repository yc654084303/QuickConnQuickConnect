package com.huichuang.test;
/**
 * 交易逻辑
 * 1、签到、获取token
 * 2，进行解码terminal_key
 */
import java.util.Map;

import newlandSdk.common.Const.MessageTag;
import newlandSdk.listener.OperaListener;
import newlandSdk.mainapp.NewlandPOSMainActivity;
import newlandSdk.resources.BluetoothImpl;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.huichuang.base.BaseActivity;
import com.huichuang.http.Callback;
import com.huichuang.http.HttpUtils;
import com.huichuang.http.NetManager;
import com.huichuang.log.L;
import com.huichuang.quickconnectpay.R;
import com.huichuang.test.bean.BackrParameterModel;
import com.huichuang.test.bean.LoginModel;
import com.huichuang.test.bean.ReqTonken;
import com.huichuang.test.bean.SignModel;
import com.huihuang.utils.BeanToMap;
import com.huihuang.utils.Mtoast;
import com.huihuang.utils.PrimaryGenerater;
@EActivity(R.layout.transactionflow)
public class TransactionflowActivity extends BaseActivity{
	private BluetoothImpl bluetoothTools;
	private OperaListener operaListener;
	private String token;
	private String secondTrack;//二磁道
	private String thirdTrack;//三磁道
	private Handler hander=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				if (!bluetoothTools.isControllerAlive()) {
					appendInteractiveInfoAndShow("未连接设备",-1);
					return;
				}
				operaListener.jiaoyi();
				break;

			default:
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bluetoothTools = new BluetoothImpl(this);
		operaListener = new OperaListener(bluetoothTools, this);
		processingUnLock();
	}
	
	@Click(R.id.butsign)//签到
	void mybutsign(){
		SignModel sign=new SignModel();
		sign.setRequestcode("1507");
		sign.setMerchantid("309190057229999");
		sign.setSessionid(BackrParameterModel.getInstance().getSessionid());
		sign.setTemcurrent(PrimaryGenerater.getInstance().generaterNextNumber());
		sign.setTermsn("99990010");
		sign.setUserid(BackrParameterModel.getInstance().getUserid());
		Map<String, String> bean2Map = BeanToMap.bean2Map(sign); 
		Map<String, String> data = HttpUtils.setHttpData(bean2Map);
		NetManager.reqPostRegister(data, new Callback() {
			
			@Override
			protected void onSuccess(Map<String, String> result) {
				Mtoast.show(getApplicationContext(), "成功", 0);
			}
			
			@Override
			protected void onFail(Map<String, String> result) {
				Mtoast.show(getApplicationContext(), "失败"+result.get("comments"), 0);
				L.e("失败"+result.get("comments"));
			}
		});
	}
	
	@Click(R.id.butconsume_id)//token
	void mybutconsumeid(){
		ReqTonken reqToken=new ReqTonken();
		reqToken.setRequestcode("1509");
		reqToken.setTemcurrent(PrimaryGenerater.getInstance().generaterNextNumber());
		reqToken.setUserid(BackrParameterModel.getInstance().getUserid());
		reqToken.setSelservid("1502");
		Map<String, String> bean2Map = BeanToMap.bean2Map(reqToken); 
		Map<String, String> data = HttpUtils.setHttpData(bean2Map);
		NetManager.reqPostRegister(data, new Callback() {
			@Override
			protected void onSuccess(Map<String, String> result) {
				Mtoast.show(getApplicationContext(), "成功", 0);
				 token = BackrParameterModel.getInstance().getToken();
				 L.e("成功"+token);
				
			}
			@Override
			protected void onFail(Map<String, String> result) {
				Mtoast.show(getApplicationContext(), "失败"+result.get("comments"), 0);
				L.e("失败"+result.get("comments"));
			}
		});
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
	     if (message.equals("控制器已初始化")) {	
	    	 hander.sendEmptyMessage(0);
		}else if (messageTag==212){
			secondTrack = message;
		}if (messageTag==213){
			thirdTrack = message;
		}
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
