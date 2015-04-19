package com.huichuang.test;

import java.io.PipedOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.ViewsById;

import com.huichuang.http.Callback;
import com.huichuang.http.HttpUtils;
import com.huichuang.http.NetManager;
import com.huichuang.http.RequestResult;
import com.huichuang.log.L;
import com.huichuang.quickconnectpay.R;
import com.huichuang.test.bean.MessageModel;
import com.huichuang.test.bean.PosParameterModel;
import com.huichuang.test.bean.RegisterModel;
import com.huihuang.utils.BeanToMap;
import com.huihuang.utils.DesPlus;
import com.huihuang.utils.MD5HexUtil;
import com.huihuang.utils.Mtoast;
import com.huihuang.utils.NetUtils;
import com.huihuang.utils.PrimaryGenerater;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.provider.CallLog;
import android.widget.EditText;
@EActivity(R.layout.test_interface_view)
public class TestInterfaceMain extends Activity{
	@ViewById
	EditText edmessage;
	@Click(R.id.message)
	void myMessageClicked() {
		MessageModel messageModel=new MessageModel();
		messageModel.setRequestcode("1705");
		messageModel.setMobile("13381990853");
		Map<String, String> bean2Map = BeanToMap.bean2Map(messageModel); 
		setHttpData(bean2Map);
	}
	@Click(R.id.register) 
	void myregisterClicked() {
		RegisterModel registerData = new RegisterModel();
		registerData.setRequestcode("1701");
		registerData.setTemcurrent(PrimaryGenerater.getInstance().generaterNextNumber());
		registerData.setLoginname("13381990853");
		  registerData.setPassword("123456");
		  registerData.setMobilenum("13381990853");
		  registerData.setValidcode(edmessage.getText().toString().trim());
		Map<String, String> bean2Map = BeanToMap.bean2Map(registerData); 
		setHttpData(bean2Map);
	}

	private void setHttpData(Map<String, String> bean2Map) {
		Map<String, String> pubParameter = HttpUtils.pubParameter(bean2Map);
			String req= HttpUtils.appendpubPath(pubParameter);
			 PosParameterModel parameter=new PosParameterModel();
			L.e(req);
			try {
				DesPlus des=new DesPlus();
				parameter.setReq_str(des.DataEncrypt(req,DesPlus.mykey));
				parameter.setSign(MD5HexUtil.getEncryptedMd5(des.DataEncrypt(req,DesPlus.mykey)+DesPlus.mykey));
			} catch (Exception e) {
				e.printStackTrace();
			}
			Map<String, String> data = BeanToMap.bean2Map(parameter); 
			if (!NetUtils.isConnected(this)) {
			Mtoast.show(this, "网络不可用", 0);
			return;
			}
			NetManager.reqPostRegister(data, new Callback() {
				@Override
				protected void onSuccess(Map<String, Object> result) {
					Mtoast.show(getApplicationContext(), "成功", 0);
				}
				@Override
				protected void onFail(Map<String, Object> result) {
					Mtoast.show(getApplicationContext(), "失败"+result.get("comments"), 0);
				}
			});
	}
}
