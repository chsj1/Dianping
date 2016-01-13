package com.dianping.util.encrypt;

import com.dianping.util.StringUtil;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public class Md5
{
  public static Md5AndBytes md5(InputStream paramInputStream)
  {
    MessageDigest localMessageDigest;
    ByteArrayOutputStream localByteArrayOutputStream;
    try
    {
      localMessageDigest = MessageDigest.getInstance("MD5");
      localMessageDigest.reset();
      localByteArrayOutputStream = new ByteArrayOutputStream();
      byte[] arrayOfByte = new byte[1024];
      while (true)
      {
        int i = paramInputStream.read(arrayOfByte);
        if (i == -1)
          break;
        localByteArrayOutputStream.write(arrayOfByte, 0, i);
      }
    }
    catch (Exception paramInputStream)
    {
      paramInputStream.printStackTrace();
      return new Md5AndBytes("", null);
    }
    paramInputStream = localByteArrayOutputStream.toByteArray();
    localMessageDigest.update(paramInputStream);
    paramInputStream = new Md5AndBytes(StringUtil.byteArrayToHexString(localMessageDigest.digest()), paramInputStream);
    return paramInputStream;
  }

  public static String md5(String paramString)
  {
    try
    {
      MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
      localMessageDigest.reset();
      localMessageDigest.update(paramString.getBytes("UTF-8"));
      paramString = StringUtil.byteArrayToHexString(localMessageDigest.digest());
      return paramString;
    }
    catch (Exception paramString)
    {
      paramString.printStackTrace();
    }
    return "def";
  }

  public static class Md5AndBytes
  {
    public byte[] bytes;
    public String md5;

    public Md5AndBytes(String paramString, byte[] paramArrayOfByte)
    {
      this.md5 = paramString;
      this.bytes = paramArrayOfByte;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.encrypt.Md5
 * JD-Core Version:    0.6.0
 */