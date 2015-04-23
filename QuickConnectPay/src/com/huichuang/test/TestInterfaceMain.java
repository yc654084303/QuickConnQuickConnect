package com.huichuang.test;

import java.io.PipedOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.ViewsById;

import com.huichuang.base.BaseActivity;
import com.huichuang.http.Callback;
import com.huichuang.http.HttpUtils;
import com.huichuang.http.NetManager;
import com.huichuang.log.L;
import com.huichuang.quickconnectpay.R;
import com.huichuang.test.bean.BackrParameterModel;
import com.huichuang.test.bean.LoginModel;
import com.huichuang.test.bean.MessageModel;
import com.huichuang.test.bean.PosParameterModel;
import com.huichuang.test.bean.RegisterModel;
import com.huichuang.test.bean.RequestResult;
import com.huihuang.utils.BeanToMap;
import com.huihuang.utils.DesPlus;
import com.huihuang.utils.MD5HexUtil;
import com.huihuang.utils.Mtoast;
import com.huihuang.utils.NetUtils;
import com.huihuang.utils.PrimaryGenerater;
import com.landicorp.mpos.util.StringUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.provider.CallLog;
import android.view.View;
import android.widget.EditText;
@EActivity(R.layout.test_interface_view)
public class TestInterfaceMain extends BaseActivity{
	@ViewById
	EditText edmessage;
	@Click(R.id.transactionflow)
	void startExtraActivity(){
		startActivity(new Intent(this, TransactionflowActivity_.class));
	}
	@Click(R.id.Login)
	void myLoginClicked()
	{
		LoginModel login=new LoginModel();
		login.setRequestcode("1702");
		login.setValidcode(getValidcode());
		login.setTemcurrent(PrimaryGenerater.getInstance().generaterNextNumber());
		login.setLoginname("13381990853");
		login.setPassword("123456");
		login.setTrade_id(BackrParameterModel.getInstance().getTrade_id());
//		login.setAppscreenflag("");不传
		Map<String, String> bean2Map = BeanToMap.bean2Map(login); 
		Map<String, String> data = HttpUtils.setHttpData(bean2Map);
		NetManager.reqPostRegister(data, new Callback() {
			@Override
			protected void onSuccess(Map<String, String> result) {
				Mtoast.show(getApplicationContext(), "成功", 0);
			}
			@Override
			protected void onFail(Map<String, String> result) {
				Mtoast.show(getApplicationContext(), "失败"+result.get("comments"), 0);
			}
		});
	}
	@Click(R.id.message)
	void myMessageClicked() {
		MessageModel messageModel=new MessageModel();
		messageModel.setRequestcode("1705");
		messageModel.setMobile("13381990853");
		Map<String, String> bean2Map = BeanToMap.bean2Map(messageModel); 
		Map<String, String> data = HttpUtils.setHttpData(bean2Map);
		NetManager.reqPostRegister(data, new Callback() {
			@Override
			protected void onSuccess(Map<String, String> result) {
				Mtoast.show(getApplicationContext(), "成功", 0);
			}
			@Override
			protected void onFail(Map<String, String> result) {
				Mtoast.show(getApplicationContext(), "失败"+result.get("comments"), 0);
			}
		});
	}
	@Click(R.id.register) 
	void myregisterClicked() {
		RegisterModel registerData = new RegisterModel();
		registerData.setRequestcode("1701");
//		 registerData.setValidcode(getValidcode());
		 registerData.setValidcode("123111");
		registerData.setTemcurrent(PrimaryGenerater.getInstance().generaterNextNumber());
		registerData.setLoginname("13381990853");
		  registerData.setPassword("123456");
		  registerData.setMobilenum("13381990853");
//		  registerData.setTrade_id(BackrParameterModel.getInstance().getTrade_id());
		Map<String, String> bean2Map = BeanToMap.bean2Map(registerData); 
		Map<String, String> data = HttpUtils.setHttpData(bean2Map);
			NetManager.reqPostRegister(data, new Callback() {
				@Override
				protected void onSuccess(Map<String, String> result) {
					Mtoast.show(getApplicationContext(), "成功", 0);
				}
				@Override
				protected void onFail(Map<String, String> result) {
					Mtoast.show(getApplicationContext(), "失败"+result.get("comments"), 0);
				}
			});
	}
	
	private String getValidcode(){
		if (edmessage.getText().toString().trim().equals("")||edmessage.getText().toString().trim()==null) {
			return "123111";
		}else {
			return edmessage.getText().toString().trim();
		}
		
	}
	
}
