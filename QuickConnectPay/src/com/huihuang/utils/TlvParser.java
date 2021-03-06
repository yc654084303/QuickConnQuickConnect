package com.huihuang.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * 
 * @author chenjl
 * 
 */
public class TlvParser {

	private ByteArrayInputStream mDataStream;
	private byte[] mTagData;
	private int mLength;
	private byte[] mLengthData;
	private byte[] mValueData;

	/** 榛樿鐨凾AG鏄�楄妭锛岄暱搴︽槸2瀛楄妭 */
	public TlvParser(byte[] data) {
		this(4, 2, data);
	}

	public TlvParser(int tagSize, int lengthSize, byte[] data) {
		mTagData = new byte[tagSize];
		mLengthData = new byte[lengthSize];

		mDataStream = new ByteArrayInputStream(data);
	}

	/** 瑙ｆ瀽涓�涓猅LV锛岃繑鍥濾alue鐨勫瓧鑺傛暟缁勶紝鍙互浣跨敤getTag()鏂规硶鑾峰彇褰撳墠鐨凾AG锛屾病鏈夋暟鎹彲瑙ｆ瀽鎴栬В鏋愰敊璇椂杩斿洖null */
	public byte[] parseOneTlv() {
		try {
			// tag
			try {
				CommonUtils.readBytes(mDataStream, mTagData);
			} catch (Exception e) {
				// 鑾峰彇tag澶辫触锛岃繑鍥瀗ull
				clearTlv();
				return null;
			}
			// length
			CommonUtils.readBytes(mDataStream, mLengthData);
			mLength = ByteArrayUtils.toInt(mLengthData);
			// value
			mValueData = new byte[mLength];
			CommonUtils.readBytes(mDataStream, mValueData);
		} catch (IOException e) {
			// Should never happen
			clearTlv();
			e.printStackTrace();
		}
		return mValueData;
	}

	private void clearTlv() {
		mTagData = null;
		mLength = 0;
		mValueData = null;
	}

	/** 鑾峰彇褰撳墠鐨凾AG锛屽搴旀渶杩戜竴娆arseOneTlv()寰楀埌鐨凾LV */
	public byte[] getTag() {
		return mTagData;
	}

	public int getLength() {
		return mLength;
	}

	public byte[] getValue() {
		return mValueData;
	}
}
