package com.dianping.util.encrypt;

import java.io.PrintStream;
import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class TOTP
{
  private static final int[] DIGITS_POWER = { 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000 };

  public static String generateTOTP(String paramString1, String paramString2, String paramString3)
  {
    return generateTOTP(paramString1, paramString2, paramString3, "HmacSHA1");
  }

  public static String generateTOTP(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    int i = Integer.decode(paramString3).intValue();
    while (paramString2.length() < 16)
      paramString2 = "0" + paramString2;
    paramString2 = hexStr2Bytes(paramString2);
    paramString1 = hmac_sha(paramString4, hexStr2Bytes(paramString1), paramString2);
    int j = paramString1[(paramString1.length - 1)] & 0xF;
    for (paramString1 = Integer.toString(((paramString1[j] & 0x7F) << 24 | (paramString1[(j + 1)] & 0xFF) << 16 | (paramString1[(j + 2)] & 0xFF) << 8 | paramString1[(j + 3)] & 0xFF) % DIGITS_POWER[i]); paramString1.length() < i; paramString1 = "0" + paramString1);
    return paramString1;
  }

  private static byte[] hexStr2Bytes(String paramString)
  {
    paramString = new BigInteger("10" + paramString, 16).toByteArray();
    byte[] arrayOfByte = new byte[paramString.length - 1];
    System.arraycopy(paramString, 1, arrayOfByte, 0, arrayOfByte.length);
    return arrayOfByte;
  }

  private static byte[] hmac_sha(String paramString, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    try
    {
      paramString = Mac.getInstance(paramString);
      paramString.init(new SecretKeySpec(paramArrayOfByte1, "RAW"));
      paramString = paramString.doFinal(paramArrayOfByte2);
      return paramString;
    }
    catch (java.security.GeneralSecurityException paramString)
    {
    }
    throw new UndeclaredThrowableException(paramString);
  }

  public static void main(String[] paramArrayOfString)
  {
    long[] arrayOfLong = new long[6];
    long[] tmp6_5 = arrayOfLong;
    tmp6_5[0] = 59L;
    long[] tmp12_6 = tmp6_5;
    tmp12_6[1] = 1111111109L;
    long[] tmp18_12 = tmp12_6;
    tmp18_12[2] = 1111111111L;
    long[] tmp24_18 = tmp18_12;
    tmp24_18[3] = 1234567890L;
    long[] tmp30_24 = tmp24_18;
    tmp30_24[4] = 2000000000L;
    long[] tmp36_30 = tmp30_24;
    tmp36_30[5] = 20000000000L;
    tmp36_30;
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    localSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    try
    {
      System.out.println("+---------------+-----------------------+------------------+--------+--------+");
      System.out.println("|  Time(sec)    |   Time (UTC format)   | Value of T(Hex)  |  TOTP  | Mode   |");
      System.out.println("+---------------+-----------------------+------------------+--------+--------+");
      int i = 0;
      while (i < arrayOfLong.length)
      {
        for (paramArrayOfString = Long.toHexString((arrayOfLong[i] - 0L) / 30L).toUpperCase(); paramArrayOfString.length() < 16; paramArrayOfString = "0" + paramArrayOfString);
        String str1 = String.format("%1$-11s", new Object[] { Long.valueOf(arrayOfLong[i]) });
        String str2 = localSimpleDateFormat.format(new Date(arrayOfLong[i] * 1000L));
        System.out.print("|  " + str1 + "  |  " + str2 + "  | " + paramArrayOfString + " |");
        System.out.println(generateTOTP("3132333435363738393031323334353637383930", paramArrayOfString, "8", "HmacSHA1") + "| SHA1   |");
        System.out.print("|  " + str1 + "  |  " + str2 + "  | " + paramArrayOfString + " |");
        System.out.println(generateTOTP("3132333435363738393031323334353637383930313233343536373839303132", paramArrayOfString, "8", "HmacSHA256") + "| SHA256 |");
        System.out.print("|  " + str1 + "  |  " + str2 + "  | " + paramArrayOfString + " |");
        System.out.println(generateTOTP("31323334353637383930313233343536373839303132333435363738393031323334353637383930313233343536373839303132333435363738393031323334", paramArrayOfString, "8", "HmacSHA512") + "| SHA512 |");
        System.out.println("+---------------+-----------------------+------------------+--------+--------+");
        i += 1;
      }
    }
    catch (java.lang.Exception paramArrayOfString)
    {
      System.out.println("Error : " + paramArrayOfString);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.encrypt.TOTP
 * JD-Core Version:    0.6.0
 */