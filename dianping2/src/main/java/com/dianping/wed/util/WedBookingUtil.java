package com.dianping.wed.util;

import android.text.TextUtils;
import com.dianping.util.DeviceUtils;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class WedBookingUtil
{
  public static String getBookingUrl(String paramString, Map<String, String> paramMap)
  {
    if (TextUtils.isEmpty(paramString))
      return "";
    StringBuilder localStringBuilder = new StringBuilder(paramString);
    if (paramMap != null)
    {
      localStringBuilder.append("?");
      paramString = paramMap.entrySet().iterator();
      while (paramString.hasNext())
      {
        paramMap = (Map.Entry)paramString.next();
        localStringBuilder.append((String)paramMap.getKey()).append("=").append((String)paramMap.getValue()).append("&");
      }
      localStringBuilder.append("dpId=").append(DeviceUtils.dpid());
    }
    while (true)
    {
      return localStringBuilder.toString();
      if (paramString.contains("?"))
      {
        localStringBuilder.append("&dpId=").append(DeviceUtils.dpid());
        continue;
      }
      localStringBuilder.append("?dpId=").append(DeviceUtils.dpid());
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.util.WedBookingUtil
 * JD-Core Version:    0.6.0
 */