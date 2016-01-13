package com.dianping.loader;

class Base64
{
  private static final char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
  private static int[] toInt = new int[''];

  static
  {
    int i = 0;
    while (i < ALPHABET.length)
    {
      toInt[ALPHABET[i]] = i;
      i += 1;
    }
  }

  public static byte[] decode(String paramString)
  {
    int i;
    byte[] arrayOfByte;
    int j;
    if (paramString.endsWith("=="))
    {
      i = 2;
      arrayOfByte = new byte[paramString.length() * 3 / 4 - i];
      j = 0;
      i = 0;
    }
    while (true)
    {
      int n;
      int k;
      int m;
      if (i < paramString.length())
      {
        n = toInt[paramString.charAt(i)];
        k = toInt[paramString.charAt(i + 1)];
        m = j + 1;
        arrayOfByte[j] = (byte)((n << 2 | k >> 4) & 0xFF);
        if (m < arrayOfByte.length)
          break label110;
      }
      label110: 
      do
      {
        return arrayOfByte;
        if (paramString.endsWith("="))
        {
          i = 1;
          break;
        }
        i = 0;
        break;
        n = toInt[paramString.charAt(i + 2)];
        j = m + 1;
        arrayOfByte[m] = (byte)((k << 4 | n >> 2) & 0xFF);
      }
      while (j >= arrayOfByte.length);
      arrayOfByte[j] = (byte)((n << 6 | toInt[paramString.charAt(i + 3)]) & 0xFF);
      i += 4;
      j += 1;
    }
  }

  public static String encode(byte[] paramArrayOfByte)
  {
    int i1 = paramArrayOfByte.length;
    char[] arrayOfChar = new char[(i1 + 2) / 3 * 4];
    int j = 0;
    int i = 0;
    if (j < i1)
    {
      int k = j + 1;
      int i2 = paramArrayOfByte[j];
      label53: int n;
      if (k < i1)
      {
        j = k + 1;
        k = paramArrayOfByte[k];
        if (j >= i1)
          break label187;
        m = j + 1;
        n = paramArrayOfByte[j];
        j = m;
      }
      label187: for (int m = n; ; m = 0)
      {
        n = i + 1;
        arrayOfChar[i] = ALPHABET[(i2 >> 2 & 0x3F)];
        i = n + 1;
        arrayOfChar[n] = ALPHABET[((i2 << 4 | (k & 0xFF) >> 4) & 0x3F)];
        n = i + 1;
        arrayOfChar[i] = ALPHABET[((k << 2 | (m & 0xFF) >> 6) & 0x3F)];
        i = n + 1;
        arrayOfChar[n] = ALPHABET[(m & 0x3F)];
        break;
        m = 0;
        j = k;
        k = m;
        break label53;
      }
    }
    switch (i1 % 3)
    {
    default:
      return new String(arrayOfChar);
    case 1:
      i -= 1;
      arrayOfChar[i] = '=';
    case 2:
    }
    while (true)
    {
      arrayOfChar[(i - 1)] = '=';
      break;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.loader.Base64
 * JD-Core Version:    0.6.0
 */