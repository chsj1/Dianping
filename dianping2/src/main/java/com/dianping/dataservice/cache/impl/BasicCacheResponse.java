package com.dianping.dataservice.cache.impl;

import com.dianping.dataservice.http.HttpResponse;
import com.dianping.dataservice.impl.BasicResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class BasicCacheResponse extends BasicResponse
  implements HttpResponse
{
  private String headerJsonString;
  private List<NameValuePair> headersList = null;
  private long time;

  public BasicCacheResponse(long paramLong, byte[] paramArrayOfByte, String paramString, Object paramObject)
  {
    super(paramArrayOfByte, paramObject);
    this.time = paramLong;
    this.headerJsonString = paramString;
  }

  private List<NameValuePair> jsonStringToHeaderList(String paramString)
  {
    if (paramString == null)
      paramString = null;
    while (true)
    {
      return paramString;
      ArrayList localArrayList = new ArrayList();
      try
      {
        JSONObject localJSONObject = new JSONObject(paramString);
        Iterator localIterator = localJSONObject.keys();
        while (true)
        {
          paramString = localArrayList;
          if (!localIterator.hasNext())
            break;
          paramString = (String)localIterator.next();
          localArrayList.add(new BasicNameValuePair(paramString, localJSONObject.getString(paramString)));
        }
      }
      catch (org.json.JSONException paramString)
      {
      }
    }
    throw new RuntimeException(paramString);
  }

  public byte[] bytes()
  {
    return (byte[])(byte[])result();
  }

  public String headerJsonString()
  {
    return this.headerJsonString;
  }

  public List<NameValuePair> headers()
  {
    if (this.headersList == null)
      this.headersList = jsonStringToHeaderList(this.headerJsonString);
    return this.headersList;
  }

  public int statusCode()
  {
    return 0;
  }

  public long time()
  {
    return this.time;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.cache.impl.BasicCacheResponse
 * JD-Core Version:    0.6.0
 */