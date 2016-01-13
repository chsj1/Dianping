package com.dianping.dataservice;

import com.dianping.util.WrapInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.UnsupportedCharsetException;

public class StringInputStream extends WrapInputStream
{
  public static final String DEFAULT_CHARSET = "UTF-8";
  public static final String UTF_8 = "UTF-8";
  private String charsetName;
  private String data;

  public StringInputStream(String paramString)
  {
    this(paramString, "UTF-8");
  }

  public StringInputStream(String paramString1, String paramString2)
  {
    this.data = paramString1;
    this.charsetName = paramString2;
  }

  public String charsetName()
  {
    return this.charsetName;
  }

  public String data()
  {
    return this.data;
  }

  public String toString()
  {
    return this.data;
  }

  protected InputStream wrappedInputStream()
    throws IOException
  {
    try
    {
      ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(this.data.getBytes(this.charsetName));
      return localByteArrayInputStream;
    }
    catch (UnsupportedCharsetException localUnsupportedCharsetException)
    {
    }
    throw new IOException(localUnsupportedCharsetException.getMessage());
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.StringInputStream
 * JD-Core Version:    0.6.0
 */