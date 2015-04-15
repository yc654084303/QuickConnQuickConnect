package com.huihuang.utils;

import java.util.Hashtable;

import com.landicorp.mpos.reader.model.LoadKeyParameter;
import com.landicorp.mpos.reader.model.MPocCalculateMacDataIn;
import com.landicorp.mpos.reader.model.MPosAID;
import com.landicorp.mpos.reader.model.MPosAddPublicKeyParameter;
import com.landicorp.mpos.reader.model.MPosEMVStartParameter;
import com.landicorp.mpos.reader.model.MPosInputPinDataIn;
import com.landicorp.mpos.reader.model.MPosTrackParameter;
import com.landicorp.mpos.util.BERTLV;
import com.landicorp.mpos.util.StringUtil;
import com.landicorp.mpos.util.TlvUtils;

/** 创建MPOS驱动库需要的各种参数对象 */
public class MposLibUtils {

	private MposLibUtils() {
	}

	public static final MPosAID parseAid(byte[] version, byte[] data) {
		Hashtable<String, BERTLV> tlvs = TlvUtils.parseTLV(data);
		MPosAID aid = new MPosAID();
		aid.setAidVersion(version);
		aid.setAid(tlvs.get(AidTags.aid).getValueBytes());
		aid.setAsi(tlvs.get(AidTags.asi).getValueBytes()[0]);
		aid.setAppVer(tlvs.get(AidTags.appVer).getValueBytes());
		aid.setTACDefault(tlvs.get(AidTags.TACDefault).getValueBytes());
		aid.setTACOnline(tlvs.get(AidTags.TACOnline).getValueBytes());
		aid.setTACDenial(tlvs.get(AidTags.TACDenial).getValueBytes());
		aid.setFloorLmt(tlvs.get(AidTags.floorLmt).getValueBytes());
		aid.setRandomLmt(tlvs.get(AidTags.randomLmt).getValueBytes());
		aid.setRandomPerMax(tlvs.get(AidTags.randomPerMax).getValueBytes()[0]);
		aid.setRandomPer(tlvs.get(AidTags.randomPer).getValueBytes()[0]);
		aid.setDDOL(tlvs.get(AidTags.DDOL).getValueBytes());
		return aid;
	}

	public static final MPosAddPublicKeyParameter parsePublicKey(
			byte[] version, byte[] data) {
		Hashtable<String, BERTLV> tlvs = TlvUtils.parseTLV(data);
		MPosAddPublicKeyParameter p = new MPosAddPublicKeyParameter();
		p.setPublicVersion(version);
		p.setRid(tlvs.get(PublicKeyTags.rid).getValueBytes());
		p.setIndex(tlvs.get(PublicKeyTags.index).getValueBytes()[0]);
		p.setExpireData(tlvs.get(PublicKeyTags.expireData).getValueBytes());
		p.setMod(tlvs.get(PublicKeyTags.mod).getValueBytes());
		p.setExp(tlvs.get(PublicKeyTags.exp).getValueBytes());
		p.setHash(tlvs.get(PublicKeyTags.hash).getValueBytes());
		return p;
	}

	public static final MPocCalculateMacDataIn createCalculateMacParam(
			byte manufacturer, byte macKeyIndex, byte[] data) {
		MPocCalculateMacDataIn param = new MPocCalculateMacDataIn();
		param.setManufacturerCode(manufacturer);
		param.setMacKeyIndex(macKeyIndex);
		param.setMacDataIn(data);
		return param;
	}

	public static final MPosTrackParameter createMPosTrackParameter(byte firm,
			byte magKeyIndex) {
		MPosTrackParameter p = new MPosTrackParameter();
		p.setManufacturerCode(firm);
		p.setTrackKeyIndex(magKeyIndex);
		p.setTimeout((byte) 30);
		return p;
	}

	public static final MPosInputPinDataIn createMPosInputPinDataIn(
			byte manufacturer, byte pinKeyIndex, byte timeout, byte[] amount,
			String cardNo) {
		MPosInputPinDataIn p = new MPosInputPinDataIn();
		p.setManufacturerCode(manufacturer);
		p.setPinKeyIndex(pinKeyIndex);
		p.setTimeout(timeout);
		p.setAmount(amount);
		// 卡号
		String format = formatPan(cardNo);
		p.setFormatPANBlock(StringUtil.hexStringToBytes(format));
		return p;
	}
	
	private static String formatPan(String pan){
		StringBuilder formatPan = new StringBuilder();
		if (pan.length()<16) {
			for (int i = 0; i < (16-pan.length()); i++) {
				formatPan.append("0");
			}
			formatPan.append(pan);
		}else{
			int end = pan.length();
			int start = pan.length() - 16;
			formatPan.append(pan.substring(start, end));
		}
		return formatPan.toString();
	}

	public static final MPosEMVStartParameter createMPosEMVStartParameter(
			Byte forceOnline, Byte transactionType,
			String authorizedAmountCent, String otherAmountCent, String date,
			String time) {
		MPosEMVStartParameter p = new MPosEMVStartParameter();
		p.setForbidContactCard(false);
		p.setForbidContactlessCard(true);
		p.setForbidMagicCard(true);
		p.setForceOnline(false);
		p.setTransactionType(transactionType);
		p.setAuthorizedAmount(authorizedAmountCent);
		p.setOtherAmount(otherAmountCent);
		p.setDate(date);
		p.setTime(time);
		return p;
	}

	public static final LoadKeyParameter createLoadKeyParameter(
			Byte masterKeyIndex, Byte keyID, byte[] key) {
		LoadKeyParameter p = new LoadKeyParameter();
		p.setMasterKeyIndex(masterKeyIndex);
		p.setKeyID(keyID);
		p.setKey(key);
		return p;
	}

	public static class AidTags {
		// public static final String aidType = ""; 不需要，服务器不会下发，是在M35上使用的
		public static final String aid = "9F06";
		public static final String asi = "DF01";
		public static final String appVer = "9F08";
		public static final String TACDefault = "DF11";
		public static final String TACOnline = "DF12";
		public static final String TACDenial = "DF13";
		public static final String floorLmt = "9F1B";
		public static final String randomLmt = "DF15";
		public static final String randomPerMax = "DF16";
		public static final String randomPer = "DF17";
		public static final String DDOL = "DF14";
		public static final String terminalPinCap = "DF18";
	}

	public static class PublicKeyTags {
		public static final String rid = "9F06";
		public static final String index = "9F22";
		public static final String expireData = "DF05";
		public static final String hashAlgorithmFlag = "DF06";
		public static final String algorithmFlag = "DF07";
		public static final String mod = "DF02";
		public static final String exp = "DF04";
		public static final String hash = "DF03";
		public static final String version = "DF25";
	}
}
