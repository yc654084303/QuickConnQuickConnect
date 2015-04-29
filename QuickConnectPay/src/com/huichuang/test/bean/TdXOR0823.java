package com.huichuang.test.bean;


public class TdXOR0823
{
  private static String DES = "DES/ECB/NoPadding";

  public static String PinEncrypt(String account, String passwd)
  {
    String result = "";
    String accountTemp1 = "";
    int passwdLen = passwd.length();
    if (passwdLen == 0)
      passwd = "FFFFFF";
    else if (passwdLen < 6) {
      for (int i = 0; i < 6 - passwdLen; ++i) {
        passwd = passwd + "F";
      }
    }
    String passwdTemp1 = "0" + passwdLen + passwd + "FFFFFFFF";
    if ((account != null) && (!"".equals(account))) {
      int len = account.length();
      String accountTemp = account.substring(len - 13, len - 1);
      accountTemp1 = "0000" + accountTemp;
    }

    if (accountTemp1.equals("")) {
      result = passwdTemp1;
    }
    else
    {
      byte[] accountByte = istr2Bcd(accountTemp1);
      byte[] passwdByte = istr2Bcd(passwdTemp1);

      byte[] resultByte = new byte[8];

      for (int i = 0; i < 8; ++i) {
        resultByte[i] = (byte)(accountByte[i] ^ passwdByte[i]);
      }
      result = ibytesToHexString(resultByte);
    }

    return result.toUpperCase();
  }
  
  public static String iByte8XORData(String macData) {
    byte[] macByte = istr2Bcd(itoHexString(macData));
    return iByte8XOR(macByte).toUpperCase();
  }

  public static String iByte8XOR(byte[] args) {
    byte[][] data = (byte[][])null;

    int length = args.length;
    int end = length % 8;
    int width = length / 8;
    if (end != 0) {
      ++width;
    }
    data = new byte[width][8];

    int colCount = 0;
    int linCount = 0;

    for (int i = 0; i < length; ++i) {
      data[linCount][colCount] = args[i];
      ++colCount;
      if (colCount == 8) {
        ++linCount;
        colCount = 0;
      }
    }
    if (end > 0) {
      for (int j = end; j < 8; ++j) {
        data[(width - 1)][j] = 0;
      }
    }

    byte[] tempData = data[0];

    for (int k = 1; k < width; ++k) {
      byte[] secData = data[k];
      for (int i = 0; i < 8; ++i) {
        tempData[i] = (byte)(tempData[i] ^ secData[i]);
      }
    }

    String result = ibytesToHexString(tempData);
    return result;
  }

  public static String ibytesToHexString(byte[] src)
  {
    StringBuilder stringBuilder = new StringBuilder();
    if ((src == null) || (src.length <= 0)) {
      return null;
    }
    for (int i = 0; i < src.length; ++i) {
      int v = src[i] & 0xFF;
      String hv = Integer.toHexString(v);
      if (hv.length() < 2) {
        stringBuilder.append(0);
      }
      stringBuilder.append(hv);
    }
    return stringBuilder.toString();
  }

  public static String iBytesToHexStr(byte[] src) {
    StringBuilder stringBuilder = new StringBuilder();
    if ((src == null) || (src.length <= 0)) {
      return null;
    }
    for (int i = 0; i < src.length; ++i) {
      int v = src[i] & 0xFF;
      String hv = Integer.toHexString(v);
      if (hv.length() < 2) {
        stringBuilder.append(0);
      }
      stringBuilder.append(hv);
    }
    return stringBuilder.toString().toUpperCase();
  }

  public static byte[] istr2Bcd(String asc)
  {
    int len = asc.length();
    int mod = len % 2;

    if (mod != 0) {
      asc = "0" + asc;
      len = asc.length();
    }

    byte[] abt = new byte[len];
    if (len >= 2) {
      len /= 2;
    }

    byte[] bbt = new byte[len];
    abt = asc.getBytes();

    for (int p = 0; p < asc.length() / 2; ++p)
    {
      int j;
      if ((abt[(2 * p)] >= 48) && (abt[(2 * p)] <= 57)) {
        j = abt[(2 * p)] - 48;
      }
      else
      {
        if ((abt[(2 * p)] >= 97) && (abt[(2 * p)] <= 122))
          j = abt[(2 * p)] - 97 + 10;
        else
          j = abt[(2 * p)] - 65 + 10;
      }
      int k;
      if ((abt[(2 * p + 1)] >= 48) && (abt[(2 * p + 1)] <= 57)) {
        k = abt[(2 * p + 1)] - 48;
      }
      else
      {
        if ((abt[(2 * p + 1)] >= 97) && (abt[(2 * p + 1)] <= 122))
          k = abt[(2 * p + 1)] - 97 + 10;
        else {
          k = abt[(2 * p + 1)] - 65 + 10;
        }
      }
      int a = (j << 4) + k;
      byte b = (byte)a;
      bbt[p] = b;
    }
    return bbt;
  }

  public static String ibcd2Str(byte[] bytes) {
    StringBuffer temp = new StringBuffer(bytes.length * 2);
    for (int i = 0; i < bytes.length; ++i) {
      temp.append((byte)((bytes[i] & 0xF0) >>> 4));
      temp.append((byte)(bytes[i] & 0xF));
    }
    return (temp.toString().substring(0, 1).equalsIgnoreCase("0")) ? 
      temp.toString().substring(1) : temp.toString();
  }

  public static String iSubMsgBodyHexXOR(byte[] args, int pos, int len)
  {
    len = args.length - len - pos;
    byte[] dest = new byte[len];
    System.arraycopy(args, pos, dest, 0, len);
    String hexStr = iByte8XOR(dest).toUpperCase();
    System.out.println(hexStr);

    return "";
  }

  public static String itoHexString(String s)
  {
    String str = "";
    for (int i = 0; i < s.length(); ++i) {
      int ch = s.charAt(i);
      String s4 = Integer.toHexString(ch);
      str = str + s4;
    }
    return str;
  }

  public static String iPinEncrypt(String account, String passwd)
  {
    String result = "";
    String accountTemp1 = "";
    int passwdLen = passwd.length();
    if (passwdLen == 0)
      passwd = "FFFFFF";
    else if (passwdLen < 6) {
      for (int i = 0; i < 6 - passwdLen; ++i) {
        passwd = passwd + "F";
      }
    }
    String passwdTemp1 = "0" + passwdLen + passwd + "FFFFFFFF";
    if ((account != null) && (!"".equals(account))) {
      int len = account.length();
      String accountTemp = account.substring(len - 13, len - 1);
      accountTemp1 = "0000" + accountTemp;
    }

    if (accountTemp1.equals("")) {
      result = passwdTemp1;
    }
    else
    {
      byte[] accountByte = istr2Bcd(accountTemp1);
      byte[] passwdByte = istr2Bcd(passwdTemp1);

      byte[] resultByte = new byte[8];

      for (int i = 0; i < 8; ++i) {
        resultByte[i] = (byte)(accountByte[i] ^ passwdByte[i]);
      }
      result = ibytesToHexString(resultByte);
    }

    return result.toUpperCase();
  }

  public static String iMac9609(String TMK, String PMK, String macData)
  {
    byte[] tmkByte = istr2Bcd(TMK);
    byte[] pmkByte = istr2Bcd(PMK);

    byte[] MwPmkByte = Test.DoubleDesDecrypt(tmkByte, pmkByte);

    String MwPmkString = ibytesToHexString(MwPmkByte).toUpperCase();

    return iMac9609Mw(MwPmkString, macData);
  }

  public static String iMac9609Mw(String key, String macData)
  {
    String mac = "";

    byte[] keyByte = istr2Bcd(key);

    byte[] dataByte = istr2Bcd(itoHexString(macData));

    byte[][] data = (byte[][])null;

    int length = dataByte.length;
    int end = length % 8;
    int width = length / 8;
    if (end != 0) {
      ++width;
    }
    data = new byte[width][8];

    int colCount = 0;
    int linCount = 0;

    for (int i = 0; i < length; ++i) {
      data[linCount][colCount] = dataByte[i];
      ++colCount;
      if (colCount == 8) {
        ++linCount;
        colCount = 0;
      }
    }
    if (end > 0) {
      for (int j = end; j < 8; ++j) {
        data[(width - 1)][j] = 0;
      }
    }

    byte[] tempData = data[0];

    for (int k = 1; k < width; ++k) {
      byte[] secData = data[k];
      tempData = Test.DoubleDesEncrypt(keyByte, tempData);
      for (int i = 0; i < 8; ++i) {
        tempData[i] = (byte)(tempData[i] ^ secData[i]);
      }
    }

    tempData = Test.DoubleDesEncrypt(keyByte, tempData);

    mac = ibytesToHexString(tempData).toUpperCase();

    return mac;
  }

  public static String iMacEcb(String TMK, String PMK, String macData) {
    byte[] tmkByte = istr2Bcd(TMK);
    byte[] pmkByte = istr2Bcd(PMK);

    byte[] MwPmkByte = Test.DoubleDesDecrypt(tmkByte, pmkByte);

    String MwPmkString = ibytesToHexString(MwPmkByte).toUpperCase();

    return iMacEcbMw(MwPmkString, macData);
  }

  public static String iMacEcbMw(String key, String macData)
  {
    String mac = "";
    byte[] keyByte = istr2Bcd(key);

    byte[] dataByte = istr2Bcd(itoHexString(macData));

    String tempData = iByte8XOR(dataByte).toUpperCase();

    String hexTempData = itoHexString(tempData);

    String leftData = hexTempData.substring(0, 16);

    String rightData = hexTempData.substring(16);

    byte[] leftByte = istr2Bcd(leftData);

    byte[] rightByte = istr2Bcd(rightData);

    byte[] tempByte = Test.DoubleDesEncrypt(keyByte, leftByte);

    for (int i = 0; i < 8; ++i) {
      tempByte[i] = (byte)(tempByte[i] ^ rightByte[i]);
    }

    tempByte = Test.DoubleDesEncrypt(keyByte, tempByte);

    String tempByteString = itoHexString(ibytesToHexString(tempByte).toUpperCase());

    mac = tempByteString.substring(0, 16);

    return mac;
  }

  public static String IMacEcb16Mw(String key, String macData)
  {
    String mac = "";
    byte[] keyByte = istr2Bcd(key);

    byte[] dataByte = istr2Bcd(macData);

    String tempData = iByte8XOR(dataByte).toUpperCase();

    String hexTempData = itoHexString(tempData);

    String leftData = hexTempData.substring(0, 16);

    String rightData = hexTempData.substring(16);

    byte[] leftByte = istr2Bcd(leftData);

    byte[] rightByte = istr2Bcd(rightData);

    byte[] tempByte = Test.UnionDesEncrypt(keyByte, leftByte);

    for (int i = 0; i < 8; ++i) {
      tempByte[i] = (byte)(tempByte[i] ^ rightByte[i]);
    }

    tempByte = Test.UnionDesEncrypt(keyByte, tempByte);

    String tempByteString = itoHexString(ibytesToHexString(tempByte).toUpperCase());

    mac = tempByteString.substring(0, 16).toUpperCase();

    return mac;
  }

  public static String iHex2Int(String data)
  {
    Integer num = Integer.valueOf(Integer.parseInt(data, 16));
    return num.toString();
  }
  
  public static String MacEcb16Mw(String key, String macData)
  {
    String mac = "";
    byte[] keyByte = istr2Bcd(key);

    byte[] dataByte = istr2Bcd(macData);

    String tempData = iByte8XOR(dataByte).toUpperCase();

    String hexTempData = itoHexString(tempData);

    String leftData = hexTempData.substring(0, 16);

    String rightData = hexTempData.substring(16);

    byte[] leftByte = istr2Bcd(leftData);

    byte[] rightByte = istr2Bcd(rightData);

    byte[] tempByte = Test.UnionDesEncrypt(keyByte, leftByte);

    for (int i = 0; i < 8; ++i) {
      tempByte[i] = (byte)(tempByte[i] ^ rightByte[i]);
    }

    tempByte = Test.UnionDesEncrypt(keyByte, tempByte);

    String tempByteString = itoHexString(ibytesToHexString(tempByte).toUpperCase());

    mac = tempByteString.substring(0, 16).toUpperCase();

    return mac;
  }
}