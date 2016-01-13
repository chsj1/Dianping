package com.dianping.base.util;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build.VERSION;
import android.os.Handler;
import android.util.Log;
import com.dianping.configservice.impl.ConfigHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class BluetoothHelper
{
  private static final int DEFAULT_CACHE_DURATION = 300000;
  private static final int DEFAULT_SCAN_DURATION = 2000;
  private static HashMap<String, iBeaconClass.iBeacon> ibeaconMap = new HashMap();
  private static boolean isBluetoothScaning = false;
  private static long lastScanTime;

  private static int getAndroidSDKVersion()
  {
    try
    {
      int i = Integer.valueOf(Build.VERSION.SDK).intValue();
      return i;
    }
    catch (NumberFormatException localNumberFormatException)
    {
    }
    return 0;
  }

  public static String getIbeaconList()
  {
    if ((ibeaconMap == null) || (ibeaconMap.size() == 0))
      return "";
    Object localObject = new ArrayList();
    ((List)localObject).addAll(ibeaconMap.values());
    if ((localObject == null) || (((List)localObject).size() == 0))
      return "";
    JSONArray localJSONArray = new JSONArray();
    localObject = ((List)localObject).iterator();
    while (((Iterator)localObject).hasNext())
    {
      iBeaconClass.iBeacon localiBeacon = (iBeaconClass.iBeacon)((Iterator)localObject).next();
      if (localiBeacon == null)
        continue;
      try
      {
        JSONObject localJSONObject = new JSONObject();
        localJSONObject.put("uuid", localiBeacon.proximityUuid);
        localJSONObject.put("major", localiBeacon.major);
        localJSONObject.put("minor", localiBeacon.minor);
        localJSONObject.put("power", localiBeacon.txPower);
        localJSONObject.put("rssi", localiBeacon.rssi);
        localJSONArray.put(localJSONObject);
      }
      catch (Exception localException)
      {
      }
    }
    return (String)localJSONArray.toString();
  }

  @TargetApi(18)
  public static boolean hasBleFeature(Context paramContext)
  {
    Log.i("SceneModeAgent", "hasBleFeature");
    if (paramContext == null)
      return false;
    if ((!paramContext.getPackageManager().hasSystemFeature("android.hardware.bluetooth_le")) || (getAndroidSDKVersion() < 18))
    {
      Log.i("SceneModeAgent", "hasBleFeature !context.getPackageManager().hasSystemFeature(\n                PackageManager.FEATURE_BLUETOOTH_LE) || getAndroidSDKVersion() < 18 return false");
      return false;
    }
    try
    {
      paramContext = ((BluetoothManager)paramContext.getSystemService("bluetooth")).getAdapter();
      if (paramContext == null)
      {
        Log.i("SceneModeAgent", "hasBleFeature mBluetoothAdapter == null return false");
        return false;
      }
    }
    catch (Exception paramContext)
    {
      Log.i("SceneModeAgent", "hasBleFeature catch return false");
      return false;
    }
    if (!paramContext.isEnabled())
    {
      Log.i("SceneModeAgent", "hasBleFeature !mBluetoothAdapter.isEnabled() return false");
      return false;
    }
    Log.i("SceneModeAgent", "hasBleFeature return true");
    return true;
  }

  public static boolean isScaning()
  {
    Log.i("SceneModeAgent", "isBluetoothScaning=" + isBluetoothScaning);
    return isBluetoothScaning;
  }

  @TargetApi(18)
  public static void scanLeDevice(Context paramContext)
  {
    Log.i("SceneModeAgent", "scanLeDevice");
    if (!hasBleFeature(paramContext))
    {
      Log.i("SceneModeAgent", "!hasBleFeature,not scanLeDevice");
      return;
    }
    int i;
    int j;
    BluetoothAdapter localBluetoothAdapter;
    BluetoothHelper.1 local1;
    try
    {
      i = Integer.parseInt(ConfigHelper.bluetoothCacheDuration);
      j = i;
      if (i == 0)
        j = 300000;
      if (System.currentTimeMillis() - lastScanTime < j)
      {
        sendBroadcast(paramContext);
        return;
      }
    }
    catch (Exception localBluetoothAdapter)
    {
      while (true)
        i = 0;
      lastScanTime = System.currentTimeMillis();
      localBluetoothAdapter = ((BluetoothManager)paramContext.getSystemService("bluetooth")).getAdapter();
      if (localBluetoothAdapter == null)
      {
        sendBroadcast(paramContext);
        return;
      }
      local1 = new BluetoothHelper.1();
    }
    try
    {
      i = Integer.parseInt(ConfigHelper.bluetoothScanDuration);
      j = i;
      if (i == 0)
        j = 2000;
      new Handler().postDelayed(new BluetoothHelper.2(localBluetoothAdapter, local1, paramContext), j);
      ibeaconMap.clear();
      isBluetoothScaning = true;
      localBluetoothAdapter.startLeScan(local1);
      Log.i("SceneModeAgent", "startLeScan ");
      return;
    }
    catch (Exception localException2)
    {
      while (true)
        i = 0;
    }
  }

  public static void sendBroadcast(Context paramContext)
  {
    paramContext.sendBroadcast(new Intent("com.dianping.action.SCAN_BLE_FINISH"));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.util.BluetoothHelper
 * JD-Core Version:    0.6.0
 */