package com.huihuang.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.huichuang.quickconnectpay.R;

import android.content.Context;
import android.os.Environment;

public class CommonUtils {

	/** 读满整个数组 */
	public static final void readBytes(InputStream in, byte[] buffer)
			throws IOException {
		readBytes(in, buffer, 0, buffer.length);
	}

	/** 读满指定长度的字芄1�7 */
	public static final void readBytes(InputStream in, byte[] buffer,
			int offset, int length) throws IOException {
		int sum = 0, readed;
		while (sum < length) {
			readed = in.read(buffer, offset + sum, length - sum);
			if (readed < 0) {
				throw new IOException("End of stream");
			}
			sum += readed;
		}
	}
	public static String copyResToSdcard(Context context){//name为sd卡下制定的路径1�7 
	      Field[] raw = R.raw.class.getFields(); 
	      String path = "";
	      File file =Environment.getExternalStorageDirectory();
	     for (Field r : raw) { 
	          try { 
	              int id=context.getResources().getIdentifier(r.getName(), "raw", context.getPackageName()); 
	                if(r.getName().equals("d086urpjm35x0120140613001")){
	                  path=file.getPath()+"/"+r.getName()+".uns"; 
	                  BufferedOutputStream bufEcrivain = new BufferedOutputStream((new FileOutputStream(new File(path)))); 
	                  BufferedInputStream VideoReader = new BufferedInputStream(context.getResources().openRawResource(id)); 
	                  byte[] buff = new byte[20*1024]; 
	                  int len; 
	                  while( (len = VideoReader.read(buff)) > 0 ){ 
	                      bufEcrivain.write(buff,0,len); 
	                  } 
	                  bufEcrivain.flush(); 
	                  bufEcrivain.close(); 
	                  VideoReader.close(); 
	                } 
	          } catch (Exception e) { 
	              e.printStackTrace(); 
	          } 
	      } 
	    return path;
	 } 
	public static String getTimestamp(){
		SimpleDateFormat sDateFormat =   new SimpleDateFormat("yyyyMMddHHmmss");       
		return sDateFormat.format(new Date());
	}
}
