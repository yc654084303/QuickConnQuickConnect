package com.huihuang.utils;

import com.landicorp.mpos.util.ByteUtils;


public class FormatTranUtils {
	/**
	 * 
	 * @param amount 金额，如12.00
	 * 可以任意形式的金额，12〄1�7.0〄1�7.00〄1�7.000
	 * 朄1�7终只保留两位小数
	 * @return 金额的BCD编码，如12.00元为＄1�700〄1�700〄1�700〄1�700〄1�712〄1�700
	 */
	public static byte[] amountStr2Bcd(String amount){
		if(amount == null || "".equals(amount))
			return null;
		byte[] data;
		String amountInt; 
		String[] tempArr = amount.split("\\.");
		if(tempArr.length<2){
			amountInt = amount+"00";
		}else{
			if(tempArr[1].length()==1){
				tempArr[1] = tempArr[1]+"0";
			}else if(tempArr[1].length()>2){
				tempArr[1] = tempArr[1].substring(0, 2); //截取小数点后两位
			}
			amountInt = tempArr[0]+tempArr[1]; 
		}
		int len = amountInt.length();
		for(int i=0;i< 12-len;i++){
			amountInt = "0"+amountInt;
		}
		data=ByteUtils.hexString2ByteArray(amountInt);
		return data;
	} 
	
	 public static byte[] ASCString2BCD_2(String asc) {
		 	char[] sv = asc.toCharArray(); 
			 int ascLen = asc.length();
			 byte[] bcd = new byte[ ascLen/ 2];
			for (int i=0,j=0;i<bcd.length;i++) {
				byte temp1 =(byte) sv[j++] ;
				byte temp2 =(byte) sv[j++] ;
				if((temp1>='A'&&temp1<='F') || (temp1>='a'&&temp1<='f')){
					temp1 = (byte) (temp1 - 'A' + 10);
				}
				if((temp2>='A'&&temp2<='F') || (temp2>='a'&&temp2<='f')){
					temp2 = (byte) (temp2 - 'A' + 10);
				}
				bcd[i]=(byte) ((temp1<<4)|(temp2&0x0F));
		   }
			return bcd; 
		}
		   
}
