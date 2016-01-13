package com.dianping.dataservice.http;

import com.dianping.util.WrapInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class FormInputStream extends WrapInputStream
{
  public static final String DEFAULT_CHARSET = "ISO-8859-1";
  public static final String ISO_8859_1 = "ISO-8859-1";
  private String charsetName;
  private List<NameValuePair> form;

  public FormInputStream(List<NameValuePair> paramList)
  {
    this(paramList, "ISO-8859-1");
  }

  public FormInputStream(List<NameValuePair> paramList, String paramString)
  {
    this.form = paramList;
    this.charsetName = paramString;
  }

  public FormInputStream(String[] paramArrayOfString)
  {
    int j = paramArrayOfString.length / 2;
    ArrayList localArrayList = new ArrayList(j);
    int i = 0;
    while (i < j)
    {
      localArrayList.add(new BasicNameValuePair(paramArrayOfString[(i * 2)], paramArrayOfString[(i * 2 + 1)]));
      i += 1;
    }
    this.form = localArrayList;
    this.charsetName = "ISO-8859-1";
  }

  private String encode()
    throws UnsupportedEncodingException
  {
    StringBuilder localStringBuilder = new StringBuilder();
    Iterator localIterator = this.form.iterator();
    while (localIterator.hasNext())
    {
      NameValuePair localNameValuePair = (NameValuePair)localIterator.next();
      if (localStringBuilder.length() > 0)
        localStringBuilder.append('&');
      localStringBuilder.append(localNameValuePair.getName());
      localStringBuilder.append('=');
      if (localNameValuePair.getValue() == null)
        continue;
      localStringBuilder.append(URLEncoder.encode(localNameValuePair.getValue(), this.charsetName));
    }
    return localStringBuilder.toString();
  }

  public String charsetName()
  {
    return this.charsetName;
  }

  public List<NameValuePair> form()
  {
    return this.form;
  }

  public String toString()
  {
    try
    {
      String str = encode();
      return str;
    }
    catch (Exception localException)
    {
    }
    return "";
  }

  protected InputStream wrappedInputStream()
    throws IOException
  {
    try
    {
      ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(encode().getBytes(this.charsetName));
      return localByteArrayInputStream;
    }
    catch (UnsupportedCharsetException localUnsupportedCharsetException)
    {
    }
    throw new IOException(localUnsupportedCharsetException.getMessage());
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.http.FormInputStream
 * JD-Core Version:    0.6.0
 */