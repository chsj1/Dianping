package com.dianping.util.network;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import com.dianping.app.DPApplication;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.util.DeviceUtils;
import com.dianping.util.Log;
import com.dianping.util.PermissionCheckHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NetworkUtils
{
  public static final String NETWORK_TYPE_2G = "2G";
  public static final String NETWORK_TYPE_3G = "3G";
  public static final String NETWORK_TYPE_4G = "4G";
  public static final String NETWORK_TYPE_UNKNOWN = "";
  public static final String NETWORK_TYPE_WIFI = "WIFI";
  public static final String TAG = "NetworkUtils";
  private static WifiManager sWifiManager;

  public static void addWifiInfo(MApiRequest paramMApiRequest, Context paramContext)
  {
    paramContext = DeviceUtils.wifiInfo();
    if (!TextUtils.isEmpty(paramContext))
    {
      paramContext = new NameValuePair(paramContext)
      {
        public String getName()
        {
          return "wifi";
        }

        public String getValue()
        {
          return this.val$wifis;
        }
      };
      paramMApiRequest = paramMApiRequest.headers();
      if (paramMApiRequest != null)
        paramMApiRequest.add(paramContext);
    }
  }

  public static WifiModel curWifi()
  {
    try
    {
      Object localObject = wifiManager().getConnectionInfo();
      if ((localObject != null) && (!TextUtils.isEmpty(((WifiInfo)localObject).getSSID())))
        return new WifiModel(((WifiInfo)localObject).getSSID(), ((WifiInfo)localObject).getBSSID(), ((WifiInfo)localObject).getRssi());
      localObject = new WifiModel("", "", 0);
      return localObject;
    }
    catch (Exception localException)
    {
    }
    return (WifiModel)new WifiModel("", "", 0);
  }

  public static String curWifiInfo()
  {
    return curWifi().toDPString();
  }

  public static String getNetworkType(Context paramContext)
  {
    if (paramContext == null)
      return "";
    try
    {
      paramContext = (ConnectivityManager)paramContext.getSystemService("connectivity");
      if (paramContext == null)
        return "";
      paramContext = paramContext.getActiveNetworkInfo();
      if (paramContext == null)
        return "";
      if (paramContext.getType() == 1)
        return "WIFI";
      if (paramContext.getType() == 0)
        switch (paramContext.getSubtype())
        {
        case 15:
          return "3G";
        default:
        case 1:
        case 2:
        case 4:
        case 7:
        case 11:
        case 3:
        case 5:
        case 6:
        case 8:
        case 9:
        case 10:
        case 12:
        case 14:
        case 13:
        }
    }
    catch (Exception paramContext)
    {
    }
    return "";
    return "";
    return "2G";
    return "3G";
    return "4G";
  }

  public static boolean isConnectingToInternet(Context paramContext)
  {
    int j = 0;
    try
    {
      paramContext = ((ConnectivityManager)paramContext.getSystemService("connectivity")).getActiveNetworkInfo();
      int i = j;
      if (paramContext != null)
      {
        boolean bool = paramContext.isConnected();
        i = j;
        if (bool)
          i = 1;
      }
      return i;
    }
    catch (Exception paramContext)
    {
    }
    return false;
  }

  public static boolean isDisconnected()
  {
    int i = 0;
    try
    {
      NetworkInfo localNetworkInfo = ((ConnectivityManager)DPApplication.instance().getSystemService("connectivity")).getActiveNetworkInfo();
      if (localNetworkInfo != null)
      {
        boolean bool = localNetworkInfo.isConnected();
        if (bool);
      }
      else
      {
        i = 1;
      }
      return i;
    }
    catch (Exception localException)
    {
      Log.e(localException.toString());
    }
    return false;
  }

  public static boolean isWIFIConnection(Context paramContext)
  {
    int i = 1;
    if (paramContext == null)
      return false;
    paramContext = ((ConnectivityManager)paramContext.getSystemService("connectivity")).getActiveNetworkInfo();
    if ((paramContext != null) && (paramContext.getType() == 1));
    while (true)
    {
      return i;
      i = 0;
    }
  }

  public static boolean isWap(Context paramContext)
  {
    Object localObject = ((ConnectivityManager)paramContext.getSystemService("connectivity")).getActiveNetworkInfo();
    if (localObject == null)
      return false;
    if (((NetworkInfo)localObject).getType() == 1)
      return false;
    if (((NetworkInfo)localObject).getType() == 0)
    {
      localObject = ((NetworkInfo)localObject).getExtraInfo();
      if (localObject == null)
        return false;
      localObject = ((String)localObject).toLowerCase();
      if (((String)localObject).contains("cmnet"))
        return false;
      if (((String)localObject).contains("cmwap"))
        return true;
      if (((String)localObject).contains("3gnet"))
        return false;
      if (((String)localObject).contains("3gwap"))
        return true;
      if (((String)localObject).contains("uninet"))
        return false;
      if (((String)localObject).contains("uniwap"))
        return true;
      if (((String)localObject).contains("ctnet"))
        return false;
      if (((String)localObject).contains("ctwap"))
        return true;
      if (((String)localObject).contains("#777"))
      {
        Context localContext = null;
        localObject = null;
        try
        {
          paramContext = paramContext.getContentResolver().query(Uri.parse("content://telephony/carriers/preferapn"), new String[] { "proxy", "port" }, null, null, null);
          localObject = paramContext;
          localContext = paramContext;
          if (paramContext.moveToFirst())
          {
            localObject = paramContext;
            localContext = paramContext;
            int i = paramContext.getString(0).length();
            if (i > 3)
              return true;
          }
          return false;
        }
        catch (Exception paramContext)
        {
          if (localObject != null)
            ((Cursor)localObject).close();
          return false;
        }
        finally
        {
          if (localContext != null)
            localContext.close();
        }
      }
    }
    return false;
  }

  public static String mac()
  {
    try
    {
      String str = wifiManager().getConnectionInfo().getMacAddress();
      return str;
    }
    catch (Exception localException)
    {
      Log.e(localException.toString());
    }
    return "";
  }

  public static String wifiInfo()
  {
    WifiModel localWifiModel = curWifi();
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(localWifiModel);
    try
    {
      if (!PermissionCheckHelper.isPermissionGranted(DPApplication.instance(), "android.permission.ACCESS_FINE_LOCATION"))
        return "";
      Object localObject1 = wifiManager().getScanResults();
      if (localObject1 != null)
      {
        localObject1 = ((List)localObject1).iterator();
        while (((Iterator)localObject1).hasNext())
        {
          Object localObject2 = (ScanResult)((Iterator)localObject1).next();
          localObject2 = new WifiModel(((ScanResult)localObject2).SSID, ((ScanResult)localObject2).BSSID, ((ScanResult)localObject2).level);
          if ((localWifiModel != null) && (((WifiModel)localObject2).equals(localWifiModel)))
            continue;
          localArrayList.add(localObject2);
        }
      }
    }
    catch (SecurityException localSecurityException)
    {
      Log.e(localSecurityException.toString());
      return "";
    }
    return (String)(String)WifiModel.listToString(localArrayList);
  }

  public static WifiManager wifiManager()
  {
    if (sWifiManager == null)
      sWifiManager = (WifiManager)DPApplication.instance().getSystemService("wifi");
    return sWifiManager;
  }

  public static JSONArray wifiScanBySceneMode2JsonArray(Context paramContext)
  {
    JSONArray localJSONArray = new JSONArray();
    if (isWIFIConnection(paramContext));
    try
    {
      Object localObject1 = wifiManager().getConnectionInfo();
      Object localObject2;
      if (localObject1 != null)
      {
        localObject2 = ((WifiInfo)localObject1).getBSSID();
        if (!TextUtils.isEmpty((CharSequence)localObject2))
        {
          JSONObject localJSONObject = new JSONObject();
          localJSONObject.put("ssid", ((WifiInfo)localObject1).getSSID());
          localJSONObject.put("mac", localObject2);
          localJSONObject.put("rssi", ((WifiInfo)localObject1).getRssi());
          localJSONObject.put("connected", true);
          localJSONArray.put(localJSONObject);
        }
      }
      label100: if (!PermissionCheckHelper.isPermissionGranted(paramContext, "android.permission.ACCESS_COARSE_LOCATION"));
      while (true)
      {
        return localJSONArray;
        paramContext = wifiManager().getScanResults();
        if ((paramContext == null) || (paramContext.isEmpty()))
          continue;
        try
        {
          paramContext = paramContext.iterator();
          while (paramContext.hasNext())
          {
            localObject1 = (ScanResult)paramContext.next();
            localObject2 = new JSONObject();
            ((JSONObject)localObject2).put("ssid", ((ScanResult)localObject1).SSID);
            ((JSONObject)localObject2).put("mac", ((ScanResult)localObject1).BSSID);
            ((JSONObject)localObject2).put("rssi", ((ScanResult)localObject1).level);
            localJSONArray.put(localObject2);
          }
        }
        catch (JSONException paramContext)
        {
          return localJSONArray;
        }
      }
    }
    catch (Exception localException)
    {
      break label100;
    }
  }

  public static JSONArray wifiScanResultInfo2JsonArray()
  {
    JSONArray localJSONArray = new JSONArray();
    if (!PermissionCheckHelper.isPermissionGranted(DPApplication.instance(), "android.permission.ACCESS_FINE_LOCATION"));
    while (true)
    {
      return localJSONArray;
      Object localObject = wifiManager().getScanResults();
      if ((localObject == null) || (((List)localObject).isEmpty()))
        continue;
      try
      {
        localObject = ((List)localObject).iterator();
        while (((Iterator)localObject).hasNext())
        {
          ScanResult localScanResult = (ScanResult)((Iterator)localObject).next();
          JSONObject localJSONObject = new JSONObject();
          localJSONObject.put("ssID", localScanResult.SSID);
          localJSONObject.put("mac", localScanResult.BSSID);
          localJSONObject.put("weight", localScanResult.level);
          localJSONArray.put(localJSONObject);
        }
      }
      catch (JSONException localJSONException)
      {
        Log.w("NetworkUtils", "nearWifiInfoToJsonArray fail", localJSONException);
      }
    }
    return (JSONArray)localJSONArray;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.network.NetworkUtils
 * JD-Core Version:    0.6.0
 */