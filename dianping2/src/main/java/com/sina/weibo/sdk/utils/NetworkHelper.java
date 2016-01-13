package com.sina.weibo.sdk.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import java.util.List;

public class NetworkHelper
{
  public static void clearCookies(Context paramContext)
  {
    CookieSyncManager.createInstance(paramContext);
    CookieManager.getInstance().removeAllCookie();
    CookieSyncManager.getInstance().sync();
  }

  public static String generateUA(Context paramContext)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Android");
    localStringBuilder.append("__");
    localStringBuilder.append("weibo");
    localStringBuilder.append("__");
    localStringBuilder.append("sdk");
    localStringBuilder.append("__");
    try
    {
      localStringBuilder.append(paramContext.getPackageManager().getPackageInfo(paramContext.getPackageName(), 16).versionName.replaceAll("\\s+", "_"));
      return localStringBuilder.toString();
    }
    catch (java.lang.Exception paramContext)
    {
      while (true)
        localStringBuilder.append("unknown");
    }
  }

  public static NetworkInfo getActiveNetworkInfo(Context paramContext)
  {
    return ((ConnectivityManager)paramContext.getSystemService("connectivity")).getActiveNetworkInfo();
  }

  public static NetworkInfo getNetworkInfo(Context paramContext, int paramInt)
  {
    return ((ConnectivityManager)paramContext.getSystemService("connectivity")).getNetworkInfo(paramInt);
  }

  public static int getNetworkType(Context paramContext)
  {
    if (paramContext != null)
    {
      paramContext = getActiveNetworkInfo(paramContext);
      if (paramContext != null);
    }
    else
    {
      return -1;
    }
    return paramContext.getType();
  }

  public static NetworkInfo.DetailedState getWifiConnectivityState(Context paramContext)
  {
    paramContext = getNetworkInfo(paramContext, 1);
    if (paramContext == null)
      return NetworkInfo.DetailedState.FAILED;
    return paramContext.getDetailedState();
  }

  public static int getWifiState(Context paramContext)
  {
    paramContext = (WifiManager)paramContext.getSystemService("wifi");
    if (paramContext == null)
      return 4;
    return paramContext.getWifiState();
  }

  public static boolean hasInternetPermission(Context paramContext)
  {
    return (paramContext == null) || (paramContext.checkCallingOrSelfPermission("android.permission.INTERNET") == 0);
  }

  public static boolean isMobileNetwork(Context paramContext)
  {
    if (paramContext != null)
    {
      paramContext = getActiveNetworkInfo(paramContext);
      if (paramContext != null)
        break label15;
    }
    label15: 
    do
      return false;
    while ((paramContext == null) || (paramContext.getType() != 0) || (!paramContext.isConnected()));
    return true;
  }

  public static boolean isNetworkAvailable(Context paramContext)
  {
    int j = 0;
    int i = j;
    if (paramContext != null)
    {
      paramContext = getActiveNetworkInfo(paramContext);
      i = j;
      if (paramContext != null)
      {
        i = j;
        if (paramContext.isConnected())
          i = 1;
      }
    }
    return i;
  }

  public static boolean isWifiValid(Context paramContext)
  {
    if (paramContext != null)
    {
      paramContext = getActiveNetworkInfo(paramContext);
      return (paramContext != null) && (1 == paramContext.getType()) && (paramContext.isConnected());
    }
    return false;
  }

  public static boolean wifiConnection(Context paramContext, String paramString1, String paramString2)
  {
    int k = 0;
    paramContext = (WifiManager)paramContext.getSystemService("wifi");
    String str1 = "\"" + paramString1 + "\"";
    Object localObject = paramContext.getConnectionInfo();
    int j;
    if ((localObject != null) && ((paramString1.equals(((WifiInfo)localObject).getSSID())) || (str1.equals(((WifiInfo)localObject).getSSID()))))
      j = 1;
    do
    {
      do
      {
        return j;
        localObject = paramContext.getScanResults();
        j = k;
      }
      while (localObject == null);
      j = k;
    }
    while (((List)localObject).size() == 0);
    int i = ((List)localObject).size() - 1;
    while (true)
    {
      j = k;
      if (i < 0)
        break;
      String str2 = ((ScanResult)((List)localObject).get(i)).SSID;
      if ((paramString1.equals(str2)) || (str1.equals(str2)))
      {
        paramString1 = new WifiConfiguration();
        paramString1.SSID = str1;
        paramString1.preSharedKey = ("\"" + paramString2 + "\"");
        paramString1.status = 2;
        return paramContext.enableNetwork(paramContext.addNetwork(paramString1), false);
      }
      i -= 1;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.sina.weibo.sdk.utils.NetworkHelper
 * JD-Core Version:    0.6.0
 */