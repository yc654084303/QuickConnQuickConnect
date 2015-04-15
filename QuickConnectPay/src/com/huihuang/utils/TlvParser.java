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

	/** 默认的TAG昄1�7�节，长度是2字节 */
	public TlvParser(byte[] data) {
		this(4, 2, data);
	}

	public TlvParser(int tagSize, int lengthSize, byte[] data) {
		mTagData = new byte[tagSize];
		mLengthData = new byte[lengthSize];

		mDataStream = new ByteArrayInputStream(data);
	}

	/** 解析丄1�7个TLV，返回Value的字节数组，可以使用getTag()方法获取当前的TAG，没有数据可解析或解析错误时返回null */
	public byte[] parseOneTlv() {
		try {
			// tag
			try {
				CommonUtils.readBytes(mDataStream, mTagData);
			} catch (Exception e) {
				// 获取tag失败，返回null
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

	/** 获取当前的TAG，对应最近一次parseOneTlv()得到的TLV */
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
