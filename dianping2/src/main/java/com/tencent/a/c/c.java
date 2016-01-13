package com.tencent.a.c;

import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class c
{
  public static String a(String paramString1, String paramString2)
    throws Exception
  {
    return a(a(b(paramString1.getBytes()), paramString2.getBytes()));
  }

  public static String a(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte == null)
      return "";
    StringBuffer localStringBuffer = new StringBuffer(paramArrayOfByte.length * 2);
    int i = 0;
    while (i < paramArrayOfByte.length)
    {
      a(localStringBuffer, paramArrayOfByte[i]);
      i += 1;
    }
    return localStringBuffer.toString();
  }

  private static void a(StringBuffer paramStringBuffer, byte paramByte)
  {
    paramStringBuffer.append("0123456789ABCDEF".charAt(paramByte >> 4 & 0xF)).append("0123456789ABCDEF".charAt(paramByte & 0xF));
  }

  private static byte[] a(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws Exception
  {
    paramArrayOfByte1 = new SecretKeySpec(paramArrayOfByte1, "AES");
    Cipher localCipher = Cipher.getInstance("AES");
    localCipher.init(1, paramArrayOfByte1);
    return localCipher.doFinal(paramArrayOfByte2);
  }

  private static byte[] b(byte[] paramArrayOfByte)
    throws Exception
  {
    KeyGenerator localKeyGenerator = KeyGenerator.getInstance("AES");
    SecureRandom localSecureRandom = SecureRandom.getInstance("SHA1PRNG");
    localSecureRandom.setSeed(paramArrayOfByte);
    localKeyGenerator.init(128, localSecureRandom);
    return localKeyGenerator.generateKey().getEncoded();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.a.c.c
 * JD-Core Version:    0.6.0
 */