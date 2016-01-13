package com.alipay.sdk.encrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Des
{
  private static String a(int paramInt, String paramString1, String paramString2)
  {
    try
    {
      SecretKeySpec localSecretKeySpec = new SecretKeySpec(paramString2.getBytes(), "DES");
      paramString2 = Cipher.getInstance("DES");
      paramString2.init(paramInt, localSecretKeySpec);
      if (paramInt == 2);
      for (paramString1 = Base64.a(paramString1); ; paramString1 = paramString1.getBytes("UTF-8"))
      {
        paramString1 = paramString2.doFinal(paramString1);
        if (paramInt != 2)
          break;
        return new String(paramString1);
      }
      paramString1 = Base64.a(paramString1);
      return paramString1;
    }
    catch (java.lang.Exception paramString1)
    {
    }
    return null;
  }

  public static String a(String paramString1, String paramString2)
  {
    return a(1, paramString1, paramString2);
  }

  public static String b(String paramString1, String paramString2)
  {
    return a(2, paramString1, paramString2);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.alipay.sdk.encrypt.Des
 * JD-Core Version:    0.6.0
 */