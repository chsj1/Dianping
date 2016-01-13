package com.dianping.util;

import android.text.TextUtils;
import java.math.BigInteger;

public class StringUtil
{
  public static final String AMPERSAND_ENTITY = "&amp;";
  public static final String APOSTROPHE_ENTITY = "&apos;";
  public static final String GREATER_THAN_ENTITY = "&gt;";
  public static final String LESS_THAN_ENTITY = "&lt;";
  private static final String[] P_0;
  public static final String QUOTE_ENTITY = "&quot;";
  private static final String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

  static
  {
    P_0 = new String[] { "", "0", "00", "000", "0000", "00000", "000000", "0000000", "00000000" };
  }

  // ERROR //
  public static String MD5Encode(String paramString)
  {
    // Byte code:
    //   0: new 27	java/lang/String
    //   3: dup
    //   4: aload_0
    //   5: invokespecial 90	java/lang/String:<init>	(Ljava/lang/String;)V
    //   8: astore_0
    //   9: ldc 92
    //   11: invokestatic 98	java/security/MessageDigest:getInstance	(Ljava/lang/String;)Ljava/security/MessageDigest;
    //   14: aload_0
    //   15: invokevirtual 102	java/lang/String:getBytes	()[B
    //   18: invokevirtual 106	java/security/MessageDigest:digest	([B)[B
    //   21: invokestatic 110	com/dianping/util/StringUtil:byteArrayToHexString	([B)Ljava/lang/String;
    //   24: astore_1
    //   25: aload_1
    //   26: areturn
    //   27: astore_0
    //   28: aconst_null
    //   29: areturn
    //   30: astore_1
    //   31: aload_0
    //   32: areturn
    //
    // Exception table:
    //   from	to	target	type
    //   0	9	27	java/lang/Exception
    //   9	25	30	java/lang/Exception
  }

  public static String byteArrayToHexString(byte[] paramArrayOfByte)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    int i = 0;
    while (i < paramArrayOfByte.length)
    {
      localStringBuilder.append(byteToHexString(paramArrayOfByte[i]));
      i += 1;
    }
    return localStringBuilder.toString();
  }

  private static String byteToHexString(byte paramByte)
  {
    int i = paramByte;
    if (paramByte < 0)
      i = paramByte + 256;
    return hexDigits[(i >> 4)] + hexDigits[(i & 0xF)];
  }

  public static String fixHex(long paramLong, int paramInt)
  {
    String str = Long.toHexString(paramLong);
    int i = paramInt - str.length();
    Object localObject = str;
    if (i > 0)
    {
      if (i < P_0.length)
        localObject = P_0[i] + str;
    }
    else
      return localObject;
    localObject = new StringBuffer(16);
    paramInt = 0;
    while (paramInt < i)
    {
      ((StringBuffer)localObject).append('0');
      paramInt += 1;
    }
    ((StringBuffer)localObject).append(str);
    return (String)((StringBuffer)localObject).toString();
  }

  public static boolean isAllPunctuation(String paramString)
  {
    if (TextUtils.isEmpty(paramString));
    do
    {
      return true;
      paramString = paramString.trim().replaceAll("\\p{Punct}|\\$|￥|~|～", "").trim();
    }
    while ((paramString == null) || (paramString.length() == 0));
    return false;
  }

  public static String join(long[] paramArrayOfLong, String paramString)
  {
    StringBuffer localStringBuffer;
    int i;
    int j;
    switch (paramArrayOfLong.length)
    {
    default:
      localStringBuffer = new StringBuffer(paramArrayOfLong.length * 20);
      localStringBuffer.append(paramArrayOfLong[0]);
      i = 1;
      j = paramArrayOfLong.length;
    case 0:
    case 1:
    case 2:
    case 3:
    case 4:
    case 5:
    }
    while (i < j)
    {
      localStringBuffer.append(paramString);
      localStringBuffer.append(paramArrayOfLong[i]);
      i += 1;
      continue;
      return "";
      return String.valueOf(paramArrayOfLong[0]);
      return String.valueOf(paramArrayOfLong[0]) + paramString + String.valueOf(paramArrayOfLong[1]);
      return String.valueOf(paramArrayOfLong[0]) + paramString + String.valueOf(paramArrayOfLong[1]) + paramString + String.valueOf(paramArrayOfLong[2]);
      return String.valueOf(paramArrayOfLong[0]) + paramString + String.valueOf(paramArrayOfLong[1]) + paramString + String.valueOf(paramArrayOfLong[2]) + paramString + String.valueOf(paramArrayOfLong[3]);
      return String.valueOf(paramArrayOfLong[0]) + paramString + String.valueOf(paramArrayOfLong[1]) + paramString + String.valueOf(paramArrayOfLong[2]) + paramString + String.valueOf(paramArrayOfLong[3]) + paramString + String.valueOf(paramArrayOfLong[4]);
    }
    return localStringBuffer.toString();
  }

  public static long parseLong(String paramString)
  {
    try
    {
      long l = Long.parseLong(paramString);
      return l;
    }
    catch (NumberFormatException localNumberFormatException)
    {
    }
    return new BigInteger(paramString).longValue();
  }

  public static boolean validateLength(String paramString, int paramInt)
  {
    return validateLength(paramString, 0, paramInt);
  }

  public static boolean validateLength(String paramString, int paramInt1, int paramInt2)
  {
    String str = paramString;
    if (paramString == null)
      str = "";
    if (str.length() > paramInt2);
    while (true)
    {
      return false;
      try
      {
        paramString = str.getBytes("utf-8");
        if (paramString.length > paramInt2)
          continue;
        paramInt2 = paramString.length;
        if (paramInt2 >= paramInt1)
          return true;
      }
      catch (java.io.UnsupportedEncodingException paramString)
      {
      }
    }
    return false;
  }

  public static String xmlEscapeAttr(Object paramObject)
  {
    paramObject = new StringBuffer(paramObject.toString());
    int i = 0;
    int j = paramObject.length();
    if (i < j)
    {
      switch (paramObject.charAt(i))
      {
      default:
      case '<':
      case '>':
      case '&':
      case '\'':
      case '"':
      }
      while (true)
      {
        i += 1;
        break;
        paramObject.replace(i, i + 1, "&lt;");
        j += 3;
        i += 3;
        continue;
        paramObject.replace(i, i + 1, "&gt;");
        j += 3;
        i += 3;
        continue;
        paramObject.replace(i, i + 1, "&amp;");
        j += 4;
        i += 4;
        continue;
        paramObject.replace(i, i + 1, "&apos;");
        j += 5;
        i += 5;
        continue;
        paramObject.replace(i, i + 1, "&quot;");
        j += 5;
        i += 5;
      }
    }
    return paramObject.toString();
  }

  public static String xmlEscapeBody(Object paramObject)
  {
    paramObject = new StringBuffer(paramObject.toString());
    int i = 0;
    int j = paramObject.length();
    if (i < j)
    {
      switch (paramObject.charAt(i))
      {
      default:
      case '<':
      case '>':
      case '&':
      }
      while (true)
      {
        i += 1;
        break;
        paramObject.replace(i, i + 1, "&lt;");
        j += 3;
        i += 3;
        continue;
        paramObject.replace(i, i + 1, "&gt;");
        j += 3;
        i += 3;
        continue;
        paramObject.replace(i, i + 1, "&amp;");
        j += 4;
        i += 4;
      }
    }
    return paramObject.toString();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.StringUtil
 * JD-Core Version:    0.6.0
 */