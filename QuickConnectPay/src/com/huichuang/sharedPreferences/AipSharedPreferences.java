package com.huichuang.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.landicorp.robert.comm.api.CommunicationManagerBase.DeviceCommunicationChannel;
import com.landicorp.robert.comm.api.DeviceInfo;

public class AipSharedPreferences {
    private static final String ALLINPAY = "ALLINPAY_131011";
    private static final String DEVICETYPE = "devicetype";
    private static final String DEVICEIDENTIFIER = "deviceidentifier";
    private static final String DEVICENAME = "devicename";
    private static final String USERNAME = "username";
    private static final String USERPASSWORD = "password";
    private static final String REMEMBERPASSWORD = "remember";


    public static AipSharedPreferences m_spInstance = null;
    private SharedPreferences sharedPreferences;

    private AipSharedPreferences() {

    }

//    public static AipSharedPreferences getInstance() {
//        if (m_spInstance == null) {
//            m_spInstance = new AipSharedPreferences();
//        }
//
//        return m_spInstance;
//    }

    public static AipSharedPreferences getInstance(Context c) {
		if (m_spInstance == null) {
			m_spInstance = new AipSharedPreferences();
			// 使用getApplicationContext()，避免引用Activity而导致内存泄漄1�7 chenjl
			m_spInstance.sharedPreferences = c.getApplicationContext()
					.getSharedPreferences(ALLINPAY, Context.MODE_PRIVATE);
		}

        return m_spInstance;
    }

    public String getUserName(){
    	 return sharedPreferences.getString(USERNAME, "");
    }
    
    public String getUserPassword(){
   	 	return sharedPreferences.getString(USERPASSWORD, "");
    }
    
    public boolean getRememberPassword(){
    	return sharedPreferences.getBoolean(REMEMBERPASSWORD,false);
    }

    public DeviceInfo getDeviceInfo(){
    	DeviceInfo deviceInfo = new DeviceInfo();
    	int type = sharedPreferences.getInt(DEVICETYPE, 0xFF);
    	if(type==0){
    		deviceInfo.setDevChannel(DeviceCommunicationChannel.AUDIOJACK);
    		deviceInfo.setName(sharedPreferences.getString(DEVICENAME, ""));
    		deviceInfo.setIdentifier(sharedPreferences.getString(DEVICEIDENTIFIER, ""));
    	}else if(type==1){
    		if(sharedPreferences.getString(DEVICEIDENTIFIER, "").equals("")){
    			deviceInfo =  null;
    		}else{
    			deviceInfo.setDevChannel(DeviceCommunicationChannel.BLUETOOTH);
    			deviceInfo.setName(sharedPreferences.getString(DEVICENAME, ""));
    			deviceInfo.setIdentifier(sharedPreferences.getString(DEVICEIDENTIFIER, ""));
    		}
    	}else{
    		deviceInfo = null;
    	}
    	return deviceInfo;
    }
    
    public boolean setDeviceInfo(DeviceInfo deviceInfo){
    	if(deviceInfo!=null){
    		Editor edit = sharedPreferences.edit();
        	if(deviceInfo.getDevChannel()==DeviceCommunicationChannel.AUDIOJACK){
        		edit.putInt(DEVICETYPE, 0);
        	}else {
    			edit.putInt(DEVICETYPE, 1);
    		}
        	edit.putString(DEVICENAME, deviceInfo.getName());
        	edit.putString(DEVICEIDENTIFIER, deviceInfo.getIdentifier());
        	return edit.commit();
    	}else{
    		Editor edit = sharedPreferences.edit();
        	edit.putInt(DEVICETYPE, 0xff);
        	edit.putString(DEVICENAME, null);
        	edit.putString(DEVICEIDENTIFIER, null);
        	return edit.commit();
    	}
    	
    }
    
    public boolean setUserName(String userName){
    	Editor edit = sharedPreferences.edit();
    	edit.putString(USERNAME, userName);
    	return edit.commit();
    }
    
    public boolean setUserPassword(String password){
    	Editor edit = sharedPreferences.edit();
    	edit.putString(USERPASSWORD, password);
    	return edit.commit();
    }
    
    public boolean setRememberPassword(boolean remember){
    	Editor edit = sharedPreferences.edit();
    	edit.putBoolean(REMEMBERPASSWORD, remember);
    	return edit.commit();
    }
}
