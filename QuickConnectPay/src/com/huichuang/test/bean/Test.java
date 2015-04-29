package com.huichuang.test.bean;

import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;


public class Test
{
  private static String DES = "DES/ECB/NoPadding";
  private static String TriDes = "DESede/ECB/NoPadding";

  public static byte[] getByteNot(byte[] args)
  {
    byte[] result = new byte[args.length];

    for (int i = 0; i < args.length; ++i) {
      result[i] = (byte)(args[i] ^ 0xFFFFFFFF);
    }
    return result;
  }

  public static byte[] UnionDesEncrypt(byte[] key, byte[] data)
  {
    try
    {
      KeySpec ks = new DESKeySpec(key);
      SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
      SecretKey ky = kf.generateSecret(ks);

      Cipher c = Cipher.getInstance(DES);
      c.init(1, ky);
      return c.doFinal(data);
    } catch (Exception e) {
      e.printStackTrace();
    }return null;
  }

  public static byte[] UnionDesDecrypt(byte[] key, byte[] data)
  {
    try
    {
      KeySpec ks = new DESKeySpec(key);
      SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
      SecretKey ky = kf.generateSecret(ks);

      Cipher c = Cipher.getInstance(DES);
      c.init(2, ky);
      return c.doFinal(data);
    } catch (Exception e) {
      e.printStackTrace();
    }return null;
  }

  public static byte[] DoubleDesEncrypt(byte[] key, byte[] data)
  {
    byte[] key1 = new byte[8];
    byte[] key2 = new byte[8];

    for (int i = 0; i < key.length; ++i) {
      if (i < 8)
        key1[i] = key[i];
      else {
        key2[(i - 8)] = key[i];
      }
    }

    byte[] result = new byte[data.length];

    result = UnionDesEncrypt(key1, data);

    result = UnionDesDecrypt(key2, result);

    result = UnionDesEncrypt(key1, result);

    return result;
  }

  public static byte[] DoubleDesDecrypt(byte[] key, byte[] data)
  {
    byte[] key1 = new byte[8];
    byte[] key2 = new byte[8];

    for (int i = 0; i < key.length; ++i) {
      if (i < 8)
        key1[i] = key[i];
      else {
        key2[(i - 8)] = key[i];
      }
    }

    byte[] result = new byte[data.length];

    result = UnionDesDecrypt(key1, data);

    result = UnionDesEncrypt(key2, result);

    result = UnionDesDecrypt(key1, result);

    return result;
  }

  public static byte[] Union3DesEncrypt(byte[] key, byte[] data)
  {
    try
    {
      byte[] k = new byte[24];

      int len = data.length;
      if (data.length % 8 != 0) {
        len = data.length - data.length % 8 + 8;
      }
      byte[] needData = (byte[])null;
      if (len != 0) {
        needData = new byte[len];
      }
      for (int i = 0; i < len; ++i) {
        needData[i] = 0;
      }

      System.arraycopy(data, 0, needData, 0, data.length);

      if (key.length == 16) {
        System.arraycopy(key, 0, k, 0, key.length);
        System.arraycopy(key, 0, k, 16, 8);
      } else {
        System.arraycopy(key, 0, k, 0, 24);
      }

      KeySpec ks = new DESedeKeySpec(k);
      SecretKeyFactory kf = SecretKeyFactory.getInstance("DESede");
      SecretKey ky = kf.generateSecret(ks);

      Cipher c = Cipher.getInstance(TriDes);
      c.init(1, ky);
      return c.doFinal(needData);
    } catch (Exception e) {
      e.printStackTrace();
    }return null;
  }

  public static byte[] Union3DesDecrypt(byte[] key, byte[] data)
  {
    try
    {
      byte[] k = new byte[24];

      int len = data.length;
      if (data.length % 8 != 0) {
        len = data.length - data.length % 8 + 8;
      }
      byte[] needData = (byte[])null;
      if (len != 0) {
        needData = new byte[len];
      }
      for (int i = 0; i < len; ++i) {
        needData[i] = 0;
      }

      System.arraycopy(data, 0, needData, 0, data.length);

      if (key.length == 16) {
        System.arraycopy(key, 0, k, 0, key.length);
        System.arraycopy(key, 0, k, 16, 8);
      } else {
        System.arraycopy(key, 0, k, 0, 24);
      }
      KeySpec ks = new DESedeKeySpec(k);
      SecretKeyFactory kf = SecretKeyFactory.getInstance("DESede");
      SecretKey ky = kf.generateSecret(ks);

      Cipher c = Cipher.getInstance(TriDes);
      c.init(2, ky);
      return c.doFinal(needData);
    } catch (Exception e) {
      e.printStackTrace();
    }return null;
  }

  public static String GenRandomKey(String key, String factor)
  {
    byte[] keyByte = TdXOR0823.istr2Bcd(key);
    byte[] factorByte = TdXOR0823.istr2Bcd(factor);

    byte[] temp1 = DoubleDesEncrypt(keyByte, factorByte);

    byte[] factorByteTemp = getByteNot(factorByte);

    byte[] temp2 = DoubleDesEncrypt(keyByte, factorByteTemp);

    String result = "";

    result = TdXOR0823.ibytesToHexString(temp1) + TdXOR0823.ibytesToHexString(temp2);
    return result;
  }

  public static String UnionDecryptData(String key, String data)
  {
    if ((key.length() != 16) && (key.length() != 32) && (key.length() != 48))
    {
      return null;
    }
    if (data.length() % 16 != 0)
    {
      return "";
    }
    int lenOfKey = 0;
    lenOfKey = key.length();
    String strEncrypt = "";
    byte[] sourData = TdXOR0823.istr2Bcd(data);
    switch (lenOfKey)
    {
    case 16:
      byte[] deskey8 = TdXOR0823.istr2Bcd(key);
      byte[] encrypt = UnionDesDecrypt(deskey8, sourData);
      strEncrypt = TdXOR0823.ibytesToHexString(encrypt);
      break;
    case 32:
    case 48:
      String newkey1 = "";
      if (lenOfKey == 32)
      {
        String newkey = key.substring(0, 16);
        newkey1 = key + newkey;
      }
      else {
        newkey1 = key;
      }
      byte[] deskey24 = TdXOR0823.istr2Bcd(newkey1);
      byte[] desEncrypt = Union3DesDecrypt(deskey24, sourData);
      strEncrypt = TdXOR0823.ibytesToHexString(desEncrypt);
    }
    return strEncrypt;
  }

  public static String UnionEncryptData(String key, String data)
  {
    if ((key.length() != 16) && (key.length() != 32) && (key.length() != 48))
    {
      return null;
    }
    if (data.length() % 16 != 0)
    {
      return "";
    }
    int lenOfKey = 0;
    lenOfKey = key.length();
    String strEncrypt = "";
    byte[] sourData = TdXOR0823.istr2Bcd(data);
    switch (lenOfKey)
    {
    case 16:
      byte[] deskey8 = TdXOR0823.istr2Bcd(key);
      byte[] encrypt = UnionDesEncrypt(deskey8, sourData);
      strEncrypt = TdXOR0823.ibytesToHexString(encrypt).toUpperCase();
      break;
    case 32:
    case 48:
      String newkey1 = "";
      if (lenOfKey == 32)
      {
        String newkey = key.substring(0, 16);
        newkey1 = key + newkey;
      }
      else {
        newkey1 = key;
      }
      byte[] deskey24 = TdXOR0823.istr2Bcd(newkey1);
      byte[] desEncrypt = Union3DesEncrypt(deskey24, sourData);
      strEncrypt = TdXOR0823.ibytesToHexString(desEncrypt).toUpperCase();
    }
    return strEncrypt;
  }
  
  public static void main(String[] args) {
	  byte[] deskey8 = TdXOR0823.istr2Bcd("73KeXRXGzYlz5byvWZl5ZEIKx1VPT6cM");
	  System.out.println(TdXOR0823.ibytesToHexString(deskey8).toLowerCase());
	 String s= Test.UnionDecryptData("73KeXRXGzYlz5byvWZl5ZEIKx1VPT6cM", "057E209C68C3E7727DEC7D7D0B99F494");
	 System.out.println(s);
	 String encryStr = Test.UnionEncryptData("73KeXRXGzYlz5byvWZl5ZEIKx1VPT6cM", "33cf6fd78de33021f1447e21cb1cd93b");
	 System.out.println(encryStr);
}
}