package com.dianping.util;

public class URLBase64
{
  private static final char[] base = new char[64];
  private static final int[] reverse = new int[''];

  static
  {
    int i = 65;
    int j = 0;
    while (i <= 90)
    {
      base[j] = i;
      reverse[i] = j;
      i = (char)(i + 1);
      j += 1;
    }
    i = 97;
    while (i <= 122)
    {
      base[j] = i;
      reverse[i] = j;
      i = (char)(i + 1);
      j += 1;
    }
    i = 48;
    while (i <= 57)
    {
      base[j] = i;
      reverse[i] = j;
      i = (char)(i + 1);
      j += 1;
    }
    base[j] = '-';
    int[] arrayOfInt = reverse;
    int k = j + 1;
    arrayOfInt[45] = j;
    base[k] = '_';
    reverse[95] = k;
  }

  public static byte[] decode(String paramString)
  {
    int i = (paramString.length() + 3) / 4 * 4;
    int j = paramString.length();
    byte[] arrayOfByte = new byte[i * 3 / 4 - (i - j)];
    j = 0;
    i = 0;
    while (true)
    {
      int n;
      int k;
      int m;
      if (i < paramString.length())
      {
        n = reverse[paramString.charAt(i)];
        k = reverse[paramString.charAt(i + 1)];
        m = j + 1;
        arrayOfByte[j] = (byte)((n << 2 | k >> 4) & 0xFF);
        if (m < arrayOfByte.length)
          break label95;
      }
      label95: 
      do
      {
        return arrayOfByte;
        n = reverse[paramString.charAt(i + 2)];
        j = m + 1;
        arrayOfByte[m] = (byte)((k << 4 | n >> 2) & 0xFF);
      }
      while (j >= arrayOfByte.length);
      arrayOfByte[j] = (byte)((n << 6 | reverse[paramString.charAt(i + 3)]) & 0xFF);
      i += 4;
      j += 1;
    }
  }

  public static String encode(byte[] paramArrayOfByte)
  {
    return encode(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public static String encode(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    char[] arrayOfChar = new char[(paramInt2 + 2) / 3 * 4];
    int j = 0;
    int i = 0;
    if (j < paramInt2)
    {
      int k = j + 1;
      int i1 = paramArrayOfByte[(paramInt1 + j)];
      label56: int n;
      if (k < paramInt2)
      {
        j = k + 1;
        k = paramArrayOfByte[(paramInt1 + k)];
        if (j >= paramInt2)
          break label202;
        m = j + 1;
        n = paramArrayOfByte[(paramInt1 + j)];
        j = m;
      }
      label202: for (int m = n; ; m = 0)
      {
        n = i + 1;
        arrayOfChar[i] = base[(i1 >> 2 & 0x3F)];
        i = n + 1;
        arrayOfChar[n] = base[((i1 << 4 | (k & 0xFF) >> 4) & 0x3F)];
        n = i + 1;
        arrayOfChar[i] = base[((k << 2 | (m & 0xFF) >> 6) & 0x3F)];
        i = n + 1;
        arrayOfChar[n] = base[(m & 0x3F)];
        break;
        m = 0;
        j = k;
        k = m;
        break label56;
      }
    }
    switch (paramInt2 % 3)
    {
    default:
      return new String(arrayOfChar, 0, i);
    case 1:
    case 2:
    }
    for (paramInt1 = i - 1; ; paramInt1 = i)
    {
      i = paramInt1 - 1;
      break;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.URLBase64
 * JD-Core Version:    0.6.0
 */