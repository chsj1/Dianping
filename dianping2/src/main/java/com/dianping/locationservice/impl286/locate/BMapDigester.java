package com.dianping.locationservice.impl286.locate;

import com.dianping.util.Log;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public class BMapDigester
{
  private static final char[] _fldif = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/.".toCharArray();
  private static final char[] a = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102 };

  private static char[] _mthif(byte[] paramArrayOfByte)
  {
    char[] arrayOfChar1 = new char[(paramArrayOfByte.length + 2) / 3 * 4];
    int j = 0;
    int i = 0;
    if (j < paramArrayOfByte.length)
    {
      int n = 0;
      int k = 0;
      int i1 = (paramArrayOfByte[j] & 0xFF) << 8;
      int m = i1;
      if (j + 1 < paramArrayOfByte.length)
      {
        m = i1 | paramArrayOfByte[(j + 1)] & 0xFF;
        k = 1;
      }
      i1 = m << 8;
      m = i1;
      if (j + 2 < paramArrayOfByte.length)
      {
        m = i1 | paramArrayOfByte[(j + 2)] & 0xFF;
        n = 1;
      }
      char[] arrayOfChar2 = _fldif;
      if (n != 0)
      {
        n = 63 - (m & 0x3F);
        label130: arrayOfChar1[(i + 3)] = arrayOfChar2[n];
        m >>= 6;
        arrayOfChar2 = _fldif;
        if (k == 0)
          break label238;
      }
      label238: for (k = 63 - (m & 0x3F); ; k = 64)
      {
        arrayOfChar1[(i + 2)] = arrayOfChar2[k];
        k = m >> 6;
        arrayOfChar1[(i + 1)] = _fldif[(63 - k & 0x3F)];
        arrayOfChar1[(i + 0)] = _fldif[(63 - (k >> 6) & 0x3F)];
        j += 3;
        i += 4;
        break;
        n = 64;
        break label130;
      }
    }
    return arrayOfChar1;
  }

  public static String digest(String paramString)
  {
    while (true)
    {
      Object localObject2;
      int k;
      byte[] arrayOfByte;
      int n;
      int j;
      try
      {
        Object localObject1 = digest((paramString + "webgis").getBytes("UTF-8"));
        localObject2 = paramString.getBytes("UTF-8");
        paramString = new byte[localObject2.length + 2];
        i = 0;
        if (i >= localObject2.length)
          continue;
        paramString[i] = localObject2[i];
        i += 1;
        continue;
        paramString[localObject2.length] = (byte)(Integer.parseInt(String.copyValueOf(localObject1, 10, 2), 16) & 0xFF);
        paramString[(localObject2.length + 1)] = (byte)(Integer.parseInt(String.copyValueOf(localObject1, 20, 2), 16) & 0xFF);
        localObject2 = "" + (char)(Integer.parseInt(String.copyValueOf(localObject1, 6, 2), 16) & 0xFF);
        localObject2 = (String)localObject2 + (char)(Integer.parseInt(String.copyValueOf(localObject1, 16, 2), 16) & 0xFF);
        localObject1 = (String)localObject2 + (char)(Integer.parseInt(String.copyValueOf(localObject1, 26, 2), 16) & 0xFF);
        localObject2 = digest(((String)localObject1 + "webgis").getBytes("iso-8859-1"));
        k = paramString.length;
        int m = ((String)localObject1).length();
        arrayOfByte = new byte[k + m];
        i = 0;
        if (i >= (k + 31) / 32)
          break label412;
        n = i * 32;
        j = 0;
        break label349;
        if (i >= m)
          continue;
        arrayOfByte[(k + i)] = (byte)((String)localObject1).charAt(i);
        i += 1;
        continue;
        paramString = new String(_mthif(arrayOfByte));
        return paramString;
      }
      catch (UnsupportedEncodingException paramString)
      {
        Log.e(paramString.toString());
        return "UnsupportedEncodingException";
      }
      label349: 
      while ((j < 32) && (n + j < k))
      {
        arrayOfByte[(n + j)] = (byte)(localObject2[j] & 0xFF ^ paramString[(n + j)] & 0xFF);
        j += 1;
      }
      i += 1;
      continue;
      label412: int i = 0;
    }
  }

  private static char[] digest(byte[] paramArrayOfByte)
  {
    char[] arrayOfChar = new char[32];
    try
    {
      MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
      localMessageDigest.update(paramArrayOfByte);
      paramArrayOfByte = localMessageDigest.digest();
      int i = 0;
      int j = 0;
      while (i < 16)
      {
        int k = paramArrayOfByte[i];
        int m = j + 1;
        arrayOfChar[j] = a[(k >>> 4 & 0xF)];
        j = m + 1;
        arrayOfChar[m] = a[(k & 0xF)];
        i += 1;
      }
    }
    catch (Exception paramArrayOfByte)
    {
      Log.e(paramArrayOfByte.toString());
    }
    return arrayOfChar;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.locationservice.impl286.locate.BMapDigester
 * JD-Core Version:    0.6.0
 */