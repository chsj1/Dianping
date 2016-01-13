package com.dianping.base.tuan.utils;

import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.http.message.BasicNameValuePair;

public class UrlBuilder
{
  private Uri.Builder mBuilder;
  private String mHashUrl;
  private String mNormalUrl;
  private Map<String, String> mParams;

  public static List<BasicNameValuePair> convertToNameValuePair(String paramString)
  {
    paramString = paramString.split("&");
    ArrayList localArrayList = new ArrayList(paramString.length);
    if (paramString != null)
    {
      int j = paramString.length;
      int i = 0;
      while (i < j)
      {
        String[] arrayOfString = paramString[i].split("=");
        if ((arrayOfString != null) && (arrayOfString.length == 2))
          localArrayList.add(new BasicNameValuePair(arrayOfString[0], arrayOfString[1]));
        i += 1;
      }
    }
    return localArrayList;
  }

  public static UrlBuilder createBuilder(String paramString)
  {
    UrlBuilder localUrlBuilder = new UrlBuilder();
    localUrlBuilder.mBuilder = Uri.parse(paramString).buildUpon();
    localUrlBuilder.mParams = new HashMap();
    return localUrlBuilder;
  }

  public UrlBuilder addParam(String paramString, Object paramObject)
  {
    if ((paramObject != null) && (!TextUtils.isEmpty(paramObject.toString())))
    {
      this.mParams.put(paramString, paramObject.toString());
      this.mNormalUrl = null;
      this.mHashUrl = null;
    }
    return this;
  }

  public UrlBuilder addParam(BasicNameValuePair paramBasicNameValuePair)
  {
    return addParam(paramBasicNameValuePair.getName(), paramBasicNameValuePair.getValue());
  }

  public UrlBuilder addParams(String paramString)
  {
    addParams(convertToNameValuePair(paramString));
    return this;
  }

  public UrlBuilder addParams(List<BasicNameValuePair> paramList)
  {
    paramList = paramList.iterator();
    while (paramList.hasNext())
      addParam((BasicNameValuePair)paramList.next());
    return this;
  }

  public UrlBuilder addParams(Map<String, Object> paramMap)
  {
    paramMap = new HashMap(paramMap).entrySet().iterator();
    while (paramMap.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)paramMap.next();
      addParam(localEntry.getKey().toString(), localEntry.getValue().toString());
    }
    return this;
  }

  public UrlBuilder appendPath(String paramString)
  {
    this.mBuilder.appendEncodedPath(paramString);
    return this;
  }

  public String buildEncodedUrl()
  {
    return buildEncodedUrl(null);
  }

  public String buildEncodedUrl(String paramString)
  {
    return Uri.encode(buildUrl(), paramString);
  }

  public String buildHtmlHashUrl()
  {
    if (this.mHashUrl == null)
    {
      StringBuilder localStringBuilder = new StringBuilder(this.mBuilder.toString());
      int i = 1;
      Iterator localIterator = this.mParams.entrySet().iterator();
      if (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        if (i != 0)
          localStringBuilder.append("#");
        while (true)
        {
          localStringBuilder.append((String)localEntry.getKey() + "=" + (String)localEntry.getValue());
          i = 0;
          break;
          localStringBuilder.append("&");
        }
      }
      this.mHashUrl = localStringBuilder.toString();
    }
    return this.mHashUrl;
  }

  public String buildUrl()
  {
    if (this.mNormalUrl == null)
    {
      Uri.Builder localBuilder = new Uri.Builder().encodedPath(this.mBuilder.toString());
      Iterator localIterator = this.mParams.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        localBuilder.appendQueryParameter((String)localEntry.getKey(), (String)localEntry.getValue());
      }
      this.mNormalUrl = localBuilder.toString();
    }
    return this.mNormalUrl;
  }

  public Uri buildWebSchema()
  {
    return buildWebSchema("dianping://web", "url", false);
  }

  public Uri buildWebSchema(String paramString1, String paramString2, boolean paramBoolean)
  {
    if (paramBoolean);
    for (String str = URLEncoder.encode(buildHtmlHashUrl()); ; str = buildEncodedUrl())
      return Uri.parse(paramString1 + "?" + paramString2 + "=" + str);
  }

  public Uri buildWebSchema(boolean paramBoolean)
  {
    return buildWebSchema("dianping://web", "url", paramBoolean);
  }

  public String toString()
  {
    return buildUrl();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.utils.UrlBuilder
 * JD-Core Version:    0.6.0
 */