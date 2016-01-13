package com.dianping.advertisement;

import android.text.TextUtils;
import com.dianping.accountservice.AccountService;
import com.dianping.app.DPApplication;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.locationservice.LocationService;
import com.dianping.model.Location;
import com.dianping.util.DeviceUtils;
import com.dianping.util.Log;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class AdClientUtils
{
  @Deprecated
  public static Map<String, String> getInfo(Map<String, String> paramMap)
  {
    paramMap.put("user_agent", Environment.mapiUserAgent());
    if ((DeviceUtils.mac() != null) && (DeviceUtils.mac().length() > 0))
      paramMap.put("device_id", DeviceUtils.mac());
    while (true)
    {
      paramMap.put("guid_str", Environment.uuid());
      return paramMap;
      paramMap.put("device_id", Environment.imei());
    }
  }

  public static void mergeCategoryInfo(Map<String, String> paramMap, DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    paramMap.put("channel_id", String.valueOf(paramDPObject.getInt("ParentID")));
    paramMap.put("category_id", String.valueOf(paramDPObject.getInt("ID")));
  }

  private static void mergeEnvironment(Map<String, String> paramMap)
  {
    Object localObject = DPApplication.instance();
    paramMap.put("guid_str", Environment.uuid());
    paramMap.put("page_city_id", String.valueOf(((DPApplication)localObject).cityId()));
    paramMap.put("pos", "1");
    DPObject localDPObject = ((DPApplication)localObject).locationService().location();
    if (localDPObject != null)
    {
      paramMap.put("lat", Location.FMT.format(localDPObject.getDouble("Lat")));
      paramMap.put("lng", Location.FMT.format(localDPObject.getDouble("Lng")));
    }
    paramMap.put("dpid", String.valueOf(DeviceUtils.dpid()));
    paramMap.put("user_agent", Environment.mapiUserAgent());
    if ((DeviceUtils.mac() != null) && (DeviceUtils.mac().length() > 0))
    {
      paramMap.put("device_id", DeviceUtils.mac());
      paramMap.put("client_version", Environment.versionName());
      paramMap.put("host", "m.api.dianping.com");
      if (((DPApplication)localObject).accountService().profile() != null)
      {
        paramMap.put("user_id", String.valueOf(((DPApplication)localObject).accountService().profile().getInt("UserID")));
        if (TextUtils.isEmpty(((DPApplication)localObject).accountService().profile().getString("Phone")))
          break label275;
      }
    }
    label275: for (localObject = ((DPApplication)localObject).accountService().profile().getString("Phone"); ; localObject = ((DPApplication)localObject).accountService().profile().getString("PhoneNo"))
    {
      paramMap.put("mobile_number", localObject);
      return;
      paramMap.put("device_id", Environment.imei());
      break;
    }
  }

  public static void mergeShopInfo(Map<String, String> paramMap, DPObject paramDPObject)
  {
    if (paramDPObject == null);
    do
    {
      return;
      paramMap.put("adshop_id", String.valueOf(paramDPObject.getInt("ID")));
      paramDPObject = paramDPObject.getString("ExtraJson");
    }
    while (TextUtils.isEmpty(paramDPObject));
    try
    {
      paramDPObject = new JSONObject(paramDPObject);
      paramMap.put("algo_version", paramDPObject.optString("Adalgoversion"));
      paramMap.put("requestId", paramDPObject.optString("AdrequestId"));
      paramMap.put("slot", paramDPObject.optString("AdslotId"));
      paramMap.put("ad", paramDPObject.optString("AdlaunchId"));
      paramMap.put("ad_type", String.valueOf(paramDPObject.optInt("AdType")));
      return;
    }
    catch (Exception paramMap)
    {
      Log.e("AdClientUtils", "extract parameters from json failed", paramMap);
    }
  }

  public static void report(String paramString, Map<String, String> paramMap)
  {
    mergeEnvironment(paramMap);
    AdClient.report(paramString, paramMap);
  }

  public static void sendAdGa(DPObject paramDPObject, String paramString1, String paramString2)
  {
    Log.d("debug_ga", "act:" + paramString1 + " index:" + paramString2);
    if (paramDPObject == null)
      return;
    HashMap localHashMap = new HashMap();
    String str = paramDPObject.getString("ExtraJson");
    Object localObject = null;
    paramDPObject = localObject;
    if (!TextUtils.isEmpty(str));
    try
    {
      paramDPObject = new JSONObject(str).optString("Feedback");
      localHashMap.put("act", paramString1);
      localHashMap.put("adidx", paramString2);
      Log.d("AdClientUtils", "report:" + localHashMap.toString());
      Log.d("debug_ga_map", localHashMap.toString());
      report(paramDPObject, localHashMap);
      return;
    }
    catch (Exception paramDPObject)
    {
      while (true)
      {
        paramDPObject.printStackTrace();
        paramDPObject = localObject;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.advertisement.AdClientUtils
 * JD-Core Version:    0.6.0
 */