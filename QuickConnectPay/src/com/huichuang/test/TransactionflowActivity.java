package com.huichuang.test;
/**
 * 交易逻辑
 * 1、签到、获取token
 * 2，进行解码terminal_key
 */
import java.util.Map;

import javax.xml.transform.Result;

import newlandSdk.common.Const.MKIndexConst;
import newlandSdk.common.Const.MessageTag;
import newlandSdk.common.Const.PinWKIndexConst;
import newlandSdk.listener.OperaListener;
import newlandSdk.mainapp.NewlandPOSMainActivity;
import newlandSdk.resources.BluetoothImpl;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.w3c.dom.Text;

import android.app.Application;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.huichuang.application.KLApplication;
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
import com.huichuang.test.bean.Test;
import com.huihuang.utils.BeanToMap;
import com.huihuang.utils.Mtoast;
import com.huihuang.utils.PrimaryGenerater;
import com.newland.me.ConnUtils;
import com.newland.mtype.ModuleType;
import com.newland.mtype.module.common.pin.PinInput;
import com.newland.mtype.module.common.pin.WorkingKeyType;
import com.newland.mtype.module.common.swiper.SwipResult;
import com.newland.mtype.util.ISOUtils;
@EActivity(R.layout.transactionflow)
public class TransactionflowActivity extends BaseActivity{
	private BluetoothImpl bluetoothTools;
	private OperaListener operaListener;
	String terminal_key ="";
//	protected static final String WORKINGKEY_DATA_PIN = "D2CEEE5C1D3AFBAF00374E0CC1526C86";// 预设输入值
//	protected static final String WORKINGKEY_DATA_MAC = "DBFE96D0A5F09D24";// 预设输入值
//	protected static final String MAINKEY = "253C9D9D7C2FBBFA253C9D9D7C2FBBFA";// 预设输入值
	private String token;
	private String secondTrack;//二磁道
	private String thirdTrack;//三磁道
	private String key="D5C0FE26CABEEF50115C1C254F98C83B";//主密钥明文
	private	String key0 = "0000000000000000";
	private String pikText;
	private String pikCheckValueText;
	private String makText;
	private String makCheckValueText;
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
				  terminal_key = BackrParameterModel.getInstance().getTerminal_key();
					Mtoast.show(getApplicationContext(), "成功"+terminal_key, 0);
					String pik = terminal_key.substring(0, 32);
					String pikCheckvalue = terminal_key.substring(32, 40);
					String mak = terminal_key.substring(40, 56);
					String makCheckvalue = terminal_key.substring(72, 80);
					pikText = Test.UnionDecryptData(key, pik);
					pikCheckValueText = Test.UnionEncryptData(pikText.toUpperCase(),key0);
					makText = Test.UnionDecryptData(key, mak);
					makCheckValueText = Test.UnionEncryptData(makText.toUpperCase(),key0);
					
//					bluetoothTools.getController().updateWorkingKey(WorkingKeyType.PININPUT, ISOUtils.hex2byte(pikText),ISOUtils.hex2byte(pikCheckValueText) );
//					bluetoothTools.getController().updateWorkingKey(WorkingKeyType.MAC, ISOUtils.hex2byte(makText),ISOUtils.hex2byte(makCheckValueText));
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
				 String tokendata = BackrParameterModel.getInstance().getToken();
				  token = Test.UnionDecryptData(BackrParameterModel.getInstance().getUserkey(), tokendata);
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
	void mybutconsume(){//交易
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
