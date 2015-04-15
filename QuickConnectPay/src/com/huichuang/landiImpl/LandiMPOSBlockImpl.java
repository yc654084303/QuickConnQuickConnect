package com.huichuang.landiImpl;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.CountDownLatch;

import android.content.Context;
import android.os.ConditionVariable;
import android.util.Log;

import com.landicorp.android.mpos.reader.LandiMPos;
import com.landicorp.mpos.reader.BasicReaderListeners.GetDeviceInfoListener;
import com.landicorp.mpos.reader.BasicReaderListeners.SendAPDUListener;
import com.landicorp.mpos.reader.BasicReaderListeners.WaitCardType;
import com.landicorp.mpos.reader.model.MPosDeviceInfo;
import com.landicorp.mpos.reader.model.MPosInputPinDataIn;
import com.huihuang.utils.*;
import com.landicorp.mpos.util.ByteUtils;

public class LandiMPOSBlockImpl extends LandiMPOSBlock {
	private LandiMPos mpos;
	ConditionVariable cdVariable; // android线程同步操作
	static volatile boolean flag = true;
	private String result;
	MPosDeviceInfo device = new MPosDeviceInfo();

	public LandiMPOSBlockImpl(Context context) {
		mpos = LandiMPos.getInstance(context);
		cdVariable = new ConditionVariable(false);
	}

	@Override
	public  String sendApdu(final byte[] apdu) {
		final CountDownLatch startSignal = new CountDownLatch(1);
		try {
			final SendAPDUListener listener = new SendAPDUListener() {
				public void onError(int errCode, String errDesc) {
					result = errCode + errDesc;
					startSignal.countDown();
				}
				public void onSendAPDUSucc(byte[] apdu) {
					result = ByteUtils.byteArray2HexString(apdu);
					startSignal.countDown();
				}
			};
			mpos.sendAPDU(apdu, listener);
			startSignal.await();
		} catch (Exception e) {
			startSignal.countDown();
			e.printStackTrace();
		}
	return result;
}

	public   String SendAPDU(String APDU){
		
		String result1=sendApdu(ByteArrayUtils.toByteArray(APDU));	
		Log.e("发�1�7�指令返回：",result);
		return result;
	}
	
	@Override
	public String waitingCard(WaitCardType type, String amount, int timout) {
		return "";
	}

	@Override
	public String inputPin(MPosInputPinDataIn inputPinParameter) {
		return "";
	}
	
	public static byte[] hexDecode(String data) {
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	for (int i = 0; i < data.length(); i += 2) {
	String onebyte = data.substring(i, i + 2);
	int b = Integer.parseInt(onebyte, 16) & 0xff;
	out.write(b);
	}
	return out.toByteArray();
	}

	@Override
	public MPosDeviceInfo getDeviceInfo() {
		final CountDownLatch startSignal = new CountDownLatch(1);
		mpos.getDeviceInfo(new GetDeviceInfoListener() {
			
			@Override
			public void onError(int errCode, String errDesc) {
				startSignal.countDown();
			}
			
			@Override
			public void onGetDeviceInfoSucc(MPosDeviceInfo deviceInfo) {
				startSignal.countDown();
				device = deviceInfo;
			}
		});
		try {
			startSignal.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return device;
	}
}
