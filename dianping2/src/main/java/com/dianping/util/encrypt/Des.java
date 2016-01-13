package com.dianping.util.encrypt;

import android.util.Base64;
import com.dianping.util.Log;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class Des
{
  private static final String ALGORITHM = "DES/CBC/PKCS7Padding";
  private Key mKey;
  private DESKeySpec mKeySpec;

  public Des(String paramString)
  {
    setKey(paramString);
  }

  private byte[] decryptByte(byte[] paramArrayOfByte)
  {
    try
    {
      Cipher localCipher = Cipher.getInstance("DES/CBC/PKCS7Padding");
      localCipher.init(2, this.mKey, new IvParameterSpec(this.mKeySpec.getKey()));
      paramArrayOfByte = localCipher.doFinal(paramArrayOfByte);
      return paramArrayOfByte;
    }
    catch (Exception paramArrayOfByte)
    {
      Log.e(paramArrayOfByte.toString());
      return null;
    }
    finally
    {
    }
    throw paramArrayOfByte;
  }

  private byte[] encryptByte(byte[] paramArrayOfByte)
  {
    try
    {
      Cipher localCipher = Cipher.getInstance("DES/CBC/PKCS7Padding");
      localCipher.init(1, this.mKey, new IvParameterSpec(this.mKeySpec.getKey()));
      paramArrayOfByte = localCipher.doFinal(paramArrayOfByte);
      return paramArrayOfByte;
    }
    catch (Exception paramArrayOfByte)
    {
      Log.e(paramArrayOfByte.toString());
      return null;
    }
    finally
    {
    }
    throw paramArrayOfByte;
  }

  public String decryptStr(String paramString)
  {
    try
    {
      paramString = new String(decryptByte(Base64.decode(paramString, 2)), "UTF8");
      return paramString;
    }
    catch (Exception paramString)
    {
      Log.e(paramString.toString());
      return "";
    }
    finally
    {
    }
    throw paramString;
  }

  public String encryptStr(String paramString)
  {
    try
    {
      paramString = Base64.encodeToString(encryptByte(paramString.getBytes("UTF8")), 2);
      return paramString;
    }
    catch (Exception paramString)
    {
      Log.e(paramString.toString());
      return "";
    }
    finally
    {
    }
    throw paramString;
  }

  public void setKey(String paramString)
  {
    try
    {
      this.mKeySpec = new DESKeySpec(paramString.getBytes());
      this.mKey = SecretKeyFactory.getInstance("DES").generateSecret(this.mKeySpec);
      return;
    }
    catch (Exception paramString)
    {
      Log.e(paramString.toString());
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.encrypt.Des
 * JD-Core Version:    0.6.0
 */