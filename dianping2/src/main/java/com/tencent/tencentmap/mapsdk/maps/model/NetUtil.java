package com.tencent.tencentmap.mapsdk.maps.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.tencent.tencentmap.mapsdk.maps.a.dh;
import com.tencent.tencentmap.mapsdk.maps.a.dl;
import com.tencent.tencentmap.mapsdk.maps.a.eg;
import com.tencent.tencentmap.mapsdk.maps.a.fb;
import java.io.IOException;
import java.net.HttpURLConnection;

@Deprecated
public class NetUtil
{
  public static int DidiTrafficUpdateTime;
  public static UpdateTrafficTimer trafficUpdater = null;

  static
  {
    DidiTrafficUpdateTime = -1;
  }

  public static HttpURLConnection createRangeConnection(String paramString1, String paramString2, long paramLong1, long paramLong2)
    throws IOException
  {
    return dh.a(paramString1, paramString2, paramLong1, paramLong2);
  }

  public static NetResponse2 doGet2(String paramString)
    throws Exception
  {
    paramString = dh.a(paramString, eg.a());
    if (paramString == null)
      return null;
    NetResponse2 localNetResponse2 = new NetResponse2();
    localNetResponse2.bytResponse = paramString.a;
    localNetResponse2.strCharset = paramString.b;
    return localNetResponse2;
  }

  public static NetResponse2 doPost2(String paramString1, String paramString2, byte[] paramArrayOfByte)
    throws Exception
  {
    paramString1 = dh.a(paramString1, paramString2, paramArrayOfByte);
    if (paramString1 == null)
      return null;
    paramString2 = new NetResponse2();
    paramString2.bytResponse = paramString1.a;
    paramString2.strCharset = paramString1.b;
    return paramString2;
  }

  public static int getTrafficUpdateTime()
  {
    return DidiTrafficUpdateTime;
  }

  public static void initNet(Context paramContext)
  {
    fb.b(paramContext);
  }

  public static boolean isWAPFeePage(String paramString)
  {
    return (paramString != null) && (paramString.contains("vnd.wap.wml"));
  }

  public static boolean isWifi(Context paramContext)
  {
    try
    {
      paramContext = ((ConnectivityManager)paramContext.getSystemService("connectivity")).getActiveNetworkInfo();
      if (paramContext != null)
      {
        int i = paramContext.getType();
        if (i == 1)
          return true;
      }
    }
    catch (Exception paramContext)
    {
    }
    return false;
  }

  public static void setTrafficUpdateTimeListener(UpdateTrafficTimer paramUpdateTrafficTimer)
  {
    trafficUpdater = paramUpdateTrafficTimer;
  }

  @Deprecated
  public static class NetResponse2
  {
    public byte[] bytResponse = null;
    public String strCharset = "";
  }

  @Deprecated
  public static abstract interface UpdateTrafficTimer
  {
    public abstract void setTrafficInterval(int paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.tencentmap.mapsdk.maps.model.NetUtil
 * JD-Core Version:    0.6.0
 */