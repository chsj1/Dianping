package com.dianping.dataservice.mapi;

import com.dianping.dataservice.http.BasicHttpRequest;
import com.dianping.util.Log;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class BasicMApiRequest extends BasicHttpRequest
  implements MApiRequest
{
  private CacheType defaultCacheType;
  private boolean disableStatistics;

  public BasicMApiRequest(String paramString1, String paramString2, InputStream paramInputStream, CacheType paramCacheType, boolean paramBoolean, List<NameValuePair> paramList)
  {
    this(paramString1, paramString2, paramInputStream, paramCacheType, paramBoolean, paramList, 0L);
  }

  public BasicMApiRequest(String paramString1, String paramString2, InputStream paramInputStream, CacheType paramCacheType, boolean paramBoolean, List<NameValuePair> paramList, long paramLong)
  {
    super(paramString1, paramString2, paramInputStream, paramList, paramLong);
    this.defaultCacheType = paramCacheType;
    this.disableStatistics = paramBoolean;
  }

  public static InputStream compress(String paramString)
    throws IOException
  {
    byte[] arrayOfByte = paramString.getBytes("UTF-8");
    paramString = new ByteArrayOutputStream();
    GZIPOutputStream localGZIPOutputStream = new GZIPOutputStream(paramString);
    localGZIPOutputStream.write(arrayOfByte);
    localGZIPOutputStream.finish();
    localGZIPOutputStream.close();
    arrayOfByte = paramString.toByteArray();
    paramString.close();
    return new MApiFileInputStream(new ByteArrayInputStream(arrayOfByte));
  }

  public static MApiRequest mapiGet(String paramString, CacheType paramCacheType)
  {
    return new BasicMApiRequest(paramString, "GET", null, paramCacheType, false, null);
  }

  public static MApiRequest mapiPost(String paramString, String[] paramArrayOfString)
  {
    return new BasicMApiRequest(paramString, "POST", new MApiFormInputStream(paramArrayOfString), CacheType.DISABLED, false, null);
  }

  public static MApiRequest mapiPostGzipString(String paramString1, String paramString2, boolean paramBoolean, List<NameValuePair> paramList)
  {
    Object localObject2 = null;
    Object localObject1 = paramList;
    try
    {
      InputStream localInputStream = compress(paramString2);
      paramString2 = paramList;
      if (paramList == null)
      {
        localObject2 = localInputStream;
        localObject1 = paramList;
        paramString2 = new ArrayList(1);
      }
      localObject2 = localInputStream;
      localObject1 = paramString2;
      paramString2.add(new BasicNameValuePair("Content-Encoding", "gzip"));
      localObject2 = localInputStream;
      return new BasicMApiRequest(paramString1, "POST", localObject2, CacheType.DISABLED, paramBoolean, paramString2);
    }
    catch (IOException paramString2)
    {
      while (true)
      {
        Log.w("BasicMApiRequest compress failed");
        paramString2 = (String)localObject1;
      }
    }
  }

  public CacheType defaultCacheType()
  {
    return this.defaultCacheType;
  }

  public boolean disableStatistics()
  {
    return this.disableStatistics;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.mapi.BasicMApiRequest
 * JD-Core Version:    0.6.0
 */