package com.huihuang.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 流水号
 * @author Administrator
 *
 */
public class PrimaryGenerater {
	 
    private static final String SERIAL_NUMBER = "000000"; // 流水号格式
    private static PrimaryGenerater primaryGenerater = null;
 
    private PrimaryGenerater() {
    }
 
    /**
     * 取得PrimaryGenerater的单例实现
     *
     * @return
     */
    public static PrimaryGenerater getInstance() {
        if (primaryGenerater == null) {
            synchronized (PrimaryGenerater.class) {
                if (primaryGenerater == null) {
                    primaryGenerater = new PrimaryGenerater();
                }
            }
        }
        return primaryGenerater;
    }
    /**
     * 生成下一个编号
     */
    public synchronized String generaterNextNumber() {
               int count = SERIAL_NUMBER.length();
               Long t = System.currentTimeMillis() / 1000;
       		String ts = t.toString();
       		String id = ts.substring(ts.length()-6, ts.length());
           return id;
    }
  
}