package com.dianping.dataservice.http;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import com.dianping.util.Log;
import java.net.InetSocketAddress;

public class NetworkInfoHelper
{
  public static final int NETWORK_TYPE_2G = 2;
  public static final int NETWORK_TYPE_3G = 3;
  public static final int NETWORK_TYPE_4G = 4;
  public static final int NETWORK_TYPE_UNKNOWN = 0;
  public static final int NETWORK_TYPE_WIFI = 1;
  private ConnectivityManager connectivityManager;
  private Context context;
  private TelephonyManager teleManager;

  public NetworkInfoHelper(Context paramContext)
  {
    this.context = paramContext;
  }

  public static boolean isNetworkConnected(Context paramContext)
  {
    if (paramContext != null)
    {
      paramContext = ((ConnectivityManager)paramContext.getSystemService("connectivity")).getActiveNetworkInfo();
      if (paramContext != null)
        return paramContext.isAvailable();
    }
    return false;
  }

  protected ConnectivityManager connectivityManager()
  {
    if (this.connectivityManager == null);
    try
    {
      this.connectivityManager = ((ConnectivityManager)this.context.getSystemService("connectivity"));
      return this.connectivityManager;
    }
    catch (Exception localException)
    {
      while (true)
        Log.w("network", "cannot get connectivity manager, maybe the permission is missing in AndroidManifest.xml?", localException);
    }
  }

  public String getDetailNetworkType()
  {
    if (getNetworkType() == 0)
      return "unknown";
    if (getNetworkType() == 1)
      return "wifi";
    return String.valueOf(telephonyManager().getNetworkType());
  }

  public String getNetworkInfo()
  {
    Object localObject = connectivityManager();
    if (localObject == null)
      return "unknown";
    localObject = ((ConnectivityManager)localObject).getActiveNetworkInfo();
    if (localObject == null)
      return "unknown";
    switch (((NetworkInfo)localObject).getType())
    {
    default:
      return ((NetworkInfo)localObject).getTypeName();
    case 1:
      return "wifi";
    case 0:
    }
    return (String)("mobile(" + ((NetworkInfo)localObject).getSubtypeName() + "," + ((NetworkInfo)localObject).getExtraInfo() + ")");
  }

  public int getNetworkType()
  {
    Object localObject = connectivityManager();
    if (localObject == null);
    do
    {
      return 0;
      localObject = ((ConnectivityManager)localObject).getActiveNetworkInfo();
    }
    while (localObject == null);
    switch (((NetworkInfo)localObject).getType())
    {
    default:
      return 0;
    case 0:
      switch (telephonyManager().getNetworkType())
      {
      default:
        return 0;
      case 1:
      case 2:
      case 4:
      case 7:
      case 11:
        return 2;
      case 3:
      case 5:
      case 6:
      case 8:
      case 9:
      case 10:
      case 12:
      case 14:
      case 15:
      case 13:
      }
    case 1:
    }
    return 1;
    return 3;
    return 4;
  }

  public InetSocketAddress getProxy()
  {
    Object localObject = connectivityManager();
    if (localObject == null)
      return null;
    try
    {
      localObject = ((ConnectivityManager)localObject).getActiveNetworkInfo();
      if (localObject == null)
        return null;
      if (((NetworkInfo)localObject).getType() == 1)
        return null;
      if (((NetworkInfo)localObject).getType() == 0)
      {
        localObject = ((NetworkInfo)localObject).getExtraInfo();
        if (localObject == null)
          return null;
        localObject = ((String)localObject).toLowerCase();
        if (((String)localObject).contains("cmnet"))
          return null;
        if (((String)localObject).contains("cmwap"))
          return new InetSocketAddress("10.0.0.172", 80);
        if (((String)localObject).contains("3gnet"))
          return null;
        if (((String)localObject).contains("3gwap"))
          return new InetSocketAddress("10.0.0.172", 80);
        if (((String)localObject).contains("uninet"))
          return null;
        if (((String)localObject).contains("uniwap"))
          return new InetSocketAddress("10.0.0.172", 80);
        if (((String)localObject).contains("ctnet"))
          return null;
        if (((String)localObject).contains("ctwap"))
          return new InetSocketAddress("10.0.0.200", 80);
        if (((String)localObject).contains("#777"))
        {
          Cursor localCursor = this.context.getContentResolver().query(Uri.parse("content://telephony/carriers/preferapn"), new String[] { "proxy", "port" }, null, null, null);
          int i;
          if (localCursor.moveToFirst())
          {
            localObject = localCursor.getString(0);
            i = ((String)localObject).length();
            if (i > 3)
              i = 0;
          }
          try
          {
            int j = Integer.parseInt(localCursor.getString(1));
            i = j;
            label269: if (i > 0);
            while (true)
            {
              localObject = new InetSocketAddress((String)localObject, i);
              return localObject;
              i = 80;
            }
            return null;
          }
          catch (NumberFormatException localNumberFormatException)
          {
            break label269;
          }
        }
      }
    }
    catch (Exception localException)
    {
    }
    return (InetSocketAddress)null;
  }

  protected TelephonyManager telephonyManager()
  {
    if (this.teleManager == null);
    try
    {
      this.teleManager = ((TelephonyManager)this.context.getSystemService("phone"));
      return this.teleManager;
    }
    catch (Exception localException)
    {
      while (true)
        Log.w("network", "cannot get telephony manager, maybe the permission is missing in AndroidManifest.xml?", localException);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.http.NetworkInfoHelper
 * JD-Core Version:    0.6.0
 */