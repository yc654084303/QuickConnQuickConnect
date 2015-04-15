package com.huichuang.landiImpl;

import com.landicorp.mpos.reader.BasicReaderListeners.WaitCardType;
import com.landicorp.mpos.reader.model.MPosDeviceInfo;
import com.landicorp.mpos.reader.model.MPosInputPinDataIn;

public abstract class LandiMPOSBlock {
	/**
	 * 阻塞调用apdu
	 * @param apdu apdu倄1�7
	 * @return
	 */
	public   abstract  String  sendApdu(byte[] apdu);
	
	/**
	 * 等待卡操作指令（可以是IC卡，可以是磁条卡，磁条卡的话直接返回磁道密文，IC卡的话，提示为IC卡） 
	 */
	public abstract String waitingCard(WaitCardType type, String amount, int timout);
	
	/**
	 * 输入PIN，获取PIN密文的接叄1�7
	 */
	public abstract String inputPin(MPosInputPinDataIn inputPinParameter);
	/**
	 * 
	 */
	public abstract MPosDeviceInfo getDeviceInfo();
}
