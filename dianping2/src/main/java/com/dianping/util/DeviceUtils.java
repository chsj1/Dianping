package com.dianping.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.StatFs;
import android.os.SystemClock;
import android.provider.Settings.System;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import com.dianping.accountservice.AccountService;
import com.dianping.app.CityConfig;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.dataservice.mapi.impl.DefaultMApiService;
import com.dianping.locationservice.LocationService;
import com.dianping.locationservice.impl286.main.DPLocationService;
import com.dianping.model.City;
import com.dianping.util.encrypt.Des;
import com.dianping.util.network.NetworkUtils;
import com.dianping.util.telephone.CellUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class DeviceUtils
{
  private static final String SYSTEM_BUILD_PROP = "/system/build.prop";
  public static final String TAG = DeviceUtils.class.getSimpleName();
  private static String mAndroidAppExist;
  private static String[] mAppInfo;
  private static int mIsInstall1 = -1;
  private static int mIsInstall2 = -1;
  private static int mIsInstall3 = -1;
  private static String[] mPicInfo;
  private static String mStrAndroidFileExist;
  private static String[] mTimeInfo;
  private static Des sDes;

  public static String androidId(Context paramContext)
  {
    return Settings.System.getString(paramContext.getContentResolver(), "android_id");
  }

  private static String bareDeviceInfo(String paramString)
  {
    long l = System.currentTimeMillis();
    if (mTimeInfo == null)
      mTimeInfo = getAppTimeInfo();
    Object localObject = new ArrayList();
    ((List)localObject).add("android " + Build.VERSION.RELEASE);
    ((List)localObject).add(paramString);
    ((List)localObject).add(source());
    ((List)localObject).add(token());
    ((List)localObject).add(dpid());
    ((List)localObject).add(uuid());
    ((List)localObject).add(curWifiInfo());
    ((List)localObject).add(cellInfo());
    ((List)localObject).add(simState());
    ((List)localObject).add(operatorAndNetwork());
    ((List)localObject).add(roaming());
    ((List)localObject).add(location());
    ((List)localObject).add("");
    ((List)localObject).add(imei());
    ((List)localObject).add("");
    ((List)localObject).add(modelAndBrand());
    ((List)localObject).add(resolution());
    ((List)localObject).add(storage());
    ((List)localObject).add("");
    ((List)localObject).add(rootInfo());
    ((List)localObject).add(mTimeInfo[0]);
    ((List)localObject).add(bootTime());
    ((List)localObject).add(cxTime());
    ((List)localObject).add("");
    ((List)localObject).add(buildInfo());
    ((List)localObject).add("");
    ((List)localObject).add("");
    ((List)localObject).add(mac());
    ((List)localObject).add(procLastModifiedTime());
    ((List)localObject).add(cdpid());
    ((List)localObject).add(String.valueOf(locStatus()));
    ((List)localObject).add(imsi());
    ((List)localObject).add(iccId());
    ((List)localObject).add(msisdn());
    ((List)localObject).add(wifiIp());
    ((List)localObject).add(qemu());
    if (mIsInstall1 == -1)
      mIsInstall1 = isInstalledApp("com.soft.apk008v");
    ((List)localObject).add(String.valueOf(mIsInstall1));
    if (mIsInstall2 == -1)
      mIsInstall2 = isInstalledApp("com.soft.apk008Tool");
    ((List)localObject).add(String.valueOf(mIsInstall2));
    if (mIsInstall3 == -1)
      mIsInstall3 = isInstalledApp("de.robv.android.xposed.installer");
    ((List)localObject).add(String.valueOf(mIsInstall3));
    ((List)localObject).add("");
    ((List)localObject).add("");
    ((List)localObject).add("");
    ((List)localObject).add("");
    ((List)localObject).add("");
    ((List)localObject).add("");
    ((List)localObject).add("");
    if (mStrAndroidFileExist == null)
      mStrAndroidFileExist = cxSdFileExist(".system/008Mode") + "-" + cxSdFileExist(".system/008OK") + "-" + cxSdFileExist(".system/008system") + "-" + cxSdFileExist("iGrimace");
    ((List)localObject).add(mStrAndroidFileExist);
    if (mAndroidAppExist == null)
      mAndroidAppExist = cxInstalledApp("com.soft.controllers") + "-" + cxInstalledApp("com.soft.apk008v") + "-" + cxInstalledApp("com.soft.apk008Tool") + "-" + cxInstalledApp("de.robv.android.xposed.installer") + "-" + cxInstalledApp("com.doubee.ig");
    ((List)localObject).add(mAndroidAppExist);
    ((List)localObject).add("");
    if (mPicInfo == null)
      mPicInfo = getPictureInfo();
    ((List)localObject).add(mPicInfo[0]);
    ((List)localObject).add(mPicInfo[1]);
    ((List)localObject).add(mPicInfo[2]);
    if (mAppInfo == null)
      mAppInfo = getAppCxInfo();
    ((List)localObject).add(mAppInfo[0]);
    ((List)localObject).add(mAppInfo[1]);
    ((List)localObject).add("");
    ((List)localObject).add("");
    ((List)localObject).add(mAppInfo[2]);
    ((List)localObject).add(mTimeInfo[1]);
    String[] arrayOfString = new String[((List)localObject).size()];
    ((List)localObject).toArray(arrayOfString);
    paramString = "";
    int i = 0;
    while (i < arrayOfString.length)
    {
      Log.i(TAG, "info" + i + ": " + arrayOfString[i]);
      localObject = paramString;
      if (i != 0)
        localObject = paramString + "&";
      String str = arrayOfString[i];
      paramString = str;
      if (!TextUtils.isEmpty(str))
        paramString = str.replace("&", "-");
      paramString = (String)localObject + paramString;
      i += 1;
    }
    Log.d("bareDeviceInfo: cost " + (System.currentTimeMillis() - l));
    return (String)paramString;
  }

  public static String bootTime()
  {
    return String.valueOf(SystemClock.elapsedRealtime());
  }

  public static String buildInfo()
  {
    return Build.FINGERPRINT;
  }

  private static long bytes2MillionBytes(long paramLong1, long paramLong2)
  {
    return paramLong1 / 1024 * (paramLong2 / 1024);
  }

  public static String cdpid()
  {
    return DPApplication.instance().getSharedPreferences("cx", 0).getString("cdpid", "");
  }

  public static String cellInfo()
  {
    return CellUtils.cellInfo();
  }

  private static void checkDes(String paramString)
  {
    paramString = paramString.split("\\|");
    Log.i(TAG, "decrypted info: " + desUtil().decryptStr(paramString[1]));
  }

  public static String curWifiInfo()
  {
    return NetworkUtils.curWifiInfo();
  }

  public static String cxInfo(String paramString)
  {
    long l1 = System.currentTimeMillis();
    JSONObject localJSONObject = new JSONObject();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(com.dianping.app.Environment.versionName());
    localStringBuilder.append("|");
    localStringBuilder.append(encryptedDeviceInfo(paramString));
    paramString = localStringBuilder.toString();
    Log.i(TAG, paramString);
    long l2 = System.currentTimeMillis();
    Log.i(TAG, "elapse: " + (l2 - l1));
    try
    {
      localJSONObject.put("cx", paramString);
      if (com.dianping.app.Environment.isDebug())
        checkDes(paramString);
      return localJSONObject.toString();
    }
    catch (JSONException localJSONException)
    {
      while (true)
        Log.e(TAG, localJSONException.toString());
    }
  }

  public static String cxInstalledApp(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    int k = 0;
    Object localObject1 = DPApplication.instance().getPackageManager();
    Object localObject2 = ((PackageManager)localObject1).getInstalledPackages(0);
    int j = k;
    int i;
    if (localObject2 != null)
    {
      i = 0;
      j = k;
      if (i < ((List)localObject2).size())
      {
        if (!paramString.equals(((PackageInfo)((List)localObject2).get(i)).packageName))
          break label196;
        j = 1;
      }
    }
    if (j != 0)
    {
      localStringBuffer.append("1");
      label87: j = 0;
      localObject2 = new Intent("android.intent.action.MAIN");
      ((Intent)localObject2).addCategory("android.intent.category.LAUNCHER");
      localObject1 = ((PackageManager)localObject1).queryIntentActivities((Intent)localObject2, 0);
      i = j;
      if (localObject1 != null)
      {
        localObject1 = ((List)localObject1).iterator();
        while (true)
        {
          i = j;
          if (!((Iterator)localObject1).hasNext())
            break;
          localObject2 = (ResolveInfo)((Iterator)localObject1).next();
          if ((((ResolveInfo)localObject2).activityInfo == null) || (!paramString.equals(((ResolveInfo)localObject2).activityInfo.packageName)))
            continue;
          i = 1;
        }
      }
      if (i == 0)
        break label216;
      localStringBuffer.append("1");
    }
    while (true)
    {
      return localStringBuffer.toString();
      label196: i += 1;
      break;
      localStringBuffer.append("0");
      break label87;
      label216: localStringBuffer.append("0");
    }
  }

  public static String cxSdFileExist(String paramString)
  {
    paramString = new File(android.os.Environment.getExternalStorageDirectory() + "/" + paramString);
    StringBuffer localStringBuffer = new StringBuffer();
    if (paramString.exists())
      localStringBuffer.append("1");
    while (true)
    {
      return localStringBuffer.toString();
      localStringBuffer.append("0");
    }
  }

  public static String cxTime()
  {
    return String.valueOf(System.currentTimeMillis());
  }

  private static Des desUtil()
  {
    if (sDes == null)
      sDes = new Des(ConfigHelper.cxk);
    return sDes;
  }

  public static String dpid()
  {
    String str = null;
    Object localObject = DPApplication.instance().getService("mapi_original");
    if ((localObject instanceof DefaultMApiService))
      str = ((DefaultMApiService)localObject).getDpid();
    do
    {
      return str;
      localObject = DPApplication.instance().getService("mapi");
    }
    while (!(localObject instanceof DefaultMApiService));
    return ((DefaultMApiService)localObject).getDpid();
  }

  private static String encryptedDeviceInfo(String paramString)
  {
    paramString = bareDeviceInfo(paramString);
    return desUtil().encryptStr(paramString);
  }

  public static String firstLaunchTime()
  {
    return String.valueOf(DPApplication.instance().getSharedPreferences("cx", 0).getLong("firstLaunchTime", 0L));
  }

  private static String[] getAppCxInfo()
  {
    String[] arrayOfString = new String[3];
    Object localObject = new Intent("android.intent.action.MAIN");
    ((Intent)localObject).addCategory("android.intent.category.LAUNCHER");
    localObject = DPApplication.instance().getPackageManager().queryIntentActivities((Intent)localObject, 0);
    if (localObject != null)
    {
      arrayOfString[0] = String.valueOf(((List)localObject).size());
      StringBuffer localStringBuffer1 = new StringBuffer();
      StringBuffer localStringBuffer2 = new StringBuffer();
      int i = 0;
      int k = 0;
      int j = 0;
      int m;
      int n;
      if (j < ((List)localObject).size())
      {
        m = k;
        n = i;
        if (((ResolveInfo)((List)localObject).get(j)).activityInfo == null)
          break label278;
        if ((i < 20) || (k < 50));
      }
      else
      {
        if (localStringBuffer1.length() > 0)
          localStringBuffer1.deleteCharAt(localStringBuffer1.length() - 1);
        if (localStringBuffer2.length() > 0)
          localStringBuffer2.deleteCharAt(localStringBuffer2.length() - 1);
        arrayOfString[1] = localStringBuffer1.toString();
        arrayOfString[2] = localStringBuffer2.toString();
        return arrayOfString;
      }
      if ((((ResolveInfo)((List)localObject).get(j)).activityInfo.applicationInfo != null) && ((((ResolveInfo)((List)localObject).get(j)).activityInfo.applicationInfo.flags & 0x1) != 0))
      {
        m = k;
        n = i;
        if (i < 20)
        {
          localStringBuffer1.append(((ResolveInfo)((List)localObject).get(j)).activityInfo.packageName);
          localStringBuffer1.append("-");
          n = i + 1;
          m = k;
        }
      }
      while (true)
      {
        label278: j += 1;
        k = m;
        i = n;
        break;
        m = k;
        n = i;
        if (k >= 50)
          continue;
        localStringBuffer2.append(((ResolveInfo)((List)localObject).get(j)).activityInfo.packageName);
        localStringBuffer2.append("-");
        m = k + 1;
        n = i;
      }
    }
    arrayOfString[0] = "0";
    arrayOfString[1] = "";
    arrayOfString[2] = "";
    return (String)arrayOfString;
  }

  public static String[] getAppTimeInfo()
  {
    String[] arrayOfString = new String[2];
    Object localObject = DPApplication.instance().getPackageManager();
    try
    {
      localObject = ((PackageManager)localObject).getPackageInfo(DPApplication.instance().getPackageName(), 0);
      arrayOfString[0] = String.valueOf(((PackageInfo)localObject).firstInstallTime);
      arrayOfString[1] = String.valueOf(((PackageInfo)localObject).lastUpdateTime);
      return arrayOfString;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      localNameNotFoundException.printStackTrace();
      arrayOfString[0] = "";
      arrayOfString[1] = "";
    }
    return (String)arrayOfString;
  }

  public static String getDeviceInfo()
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("mac", mac());
      localJSONObject.put("imei", imei());
      return localJSONObject.toString();
    }
    catch (JSONException localJSONException)
    {
      while (true)
        Log.e(TAG, localJSONException.toString());
    }
  }

  public static PackageInfo getPackageInfo(Context paramContext)
  {
    try
    {
      paramContext = paramContext.getPackageManager().getPackageInfo(paramContext.getPackageName(), 0);
      return paramContext;
    }
    catch (PackageManager.NameNotFoundException paramContext)
    {
    }
    return null;
  }

  public static String[] getPictureInfo()
  {
    String[] arrayOfString = new String[3];
    arrayOfString[0] = "";
    arrayOfString[1] = "";
    arrayOfString[2] = "";
    File localFile = new File(android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DCIM).getAbsolutePath() + "/Camera");
    File[] arrayOfFile;
    StringBuilder localStringBuilder;
    if (localFile.exists())
    {
      arrayOfFile = localFile.listFiles();
      localStringBuilder = new StringBuilder();
      if (arrayOfFile != null)
        break label218;
    }
    label218: for (int i = 0; ; i = arrayOfFile.length)
    {
      arrayOfString[0] = (String.valueOf(i) + "-" + String.valueOf(localFile.lastModified()));
      if (arrayOfFile != null)
      {
        if (arrayOfFile.length >= 1)
          arrayOfString[1] = (String.valueOf(arrayOfFile[0].lastModified()) + "-" + String.valueOf(arrayOfFile[0].length()));
        if (arrayOfFile.length >= 10)
          arrayOfString[2] = (String.valueOf(arrayOfFile[9].lastModified()) + "-" + String.valueOf(arrayOfFile[9].length()));
      }
      return arrayOfString;
    }
  }

  public static String iccId()
  {
    if (!PermissionCheckHelper.isPermissionGranted(DPApplication.instance(), "android.permission.READ_PHONE_STATE"))
      return "";
    try
    {
      String str = ((TelephonyManager)DPApplication.instance().getSystemService("phone")).getSimSerialNumber();
      return str;
    }
    catch (Throwable localThrowable)
    {
    }
    return "";
  }

  public static String imei()
  {
    return com.dianping.app.Environment.imei();
  }

  public static String imsi()
  {
    if (!PermissionCheckHelper.isPermissionGranted(DPApplication.instance(), "android.permission.READ_PHONE_STATE"))
      return "";
    try
    {
      String str = ((TelephonyManager)DPApplication.instance().getSystemService("phone")).getSubscriberId();
      return str;
    }
    catch (Throwable localThrowable)
    {
    }
    return "";
  }

  public static boolean isAvailable(Context paramContext, String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      return false;
    paramContext = paramContext.getPackageManager().getInstalledPackages(0);
    int i = 0;
    while (i < paramContext.size())
    {
      if (paramString.equalsIgnoreCase(((PackageInfo)paramContext.get(i)).packageName))
        return true;
      i += 1;
    }
    return false;
  }

  public static boolean isCurrentCity()
  {
    int j = DPApplication.instance().cityConfig().currentCity().id();
    DPObject localDPObject = DPApplication.instance().locationService().city();
    if (localDPObject == null);
    for (int i = -1; j == i; i = localDPObject.getInt("ID"))
      return true;
    return false;
  }

  private static boolean isExecutable(String paramString)
  {
    Object localObject = null;
    String str1 = null;
    try
    {
      paramString = Runtime.getRuntime().exec("ls -l " + paramString);
      str1 = paramString;
      localObject = paramString;
      String str2 = new BufferedReader(new InputStreamReader(paramString.getInputStream())).readLine();
      if (str2 != null)
      {
        str1 = paramString;
        localObject = paramString;
        if (str2.length() >= 4)
        {
          str1 = paramString;
          localObject = paramString;
          int i = str2.charAt(3);
          if ((i == 115) || (i == 120))
          {
            if (paramString != null)
              paramString.destroy();
            return true;
          }
        }
      }
      if (paramString != null)
        paramString.destroy();
      return false;
    }
    catch (IOException paramString)
    {
      while (true)
      {
        localObject = str1;
        paramString.printStackTrace();
        if (str1 == null)
          continue;
        str1.destroy();
      }
    }
    finally
    {
      if (localObject != null)
        ((Process)localObject).destroy();
    }
    throw paramString;
  }

  public static int isInstalledApp(String paramString)
  {
    int k = 0;
    List localList = DPApplication.instance().getPackageManager().getInstalledPackages(0);
    int j = k;
    int i;
    if (localList != null)
      i = 0;
    while (true)
    {
      j = k;
      if (i < localList.size())
      {
        if (paramString.equals(((PackageInfo)localList.get(i)).packageName))
          j = 1;
      }
      else
      {
        if (j == 0)
          break;
        return 1;
      }
      i += 1;
    }
    return 0;
  }

  public static int locStatus()
  {
    if (!PermissionCheckHelper.isPermissionGranted(DPApplication.instance(), "android.permission.ACCESS_FINE_LOCATION"));
    while (true)
    {
      return 0;
      if (!DPLocationService.hasPermission())
        continue;
      try
      {
        LocationManager localLocationManager = (LocationManager)DPApplication.instance().getSystemService("location");
        if (localLocationManager.getProvider("gps") == null)
          continue;
        boolean bool = localLocationManager.isProviderEnabled("gps");
        if (bool)
          return 1;
      }
      catch (Throwable localThrowable)
      {
      }
    }
    return 0;
  }

  public static String location()
  {
    DPObject localDPObject = DPApplication.instance().locationService().location();
    if (localDPObject == null)
      return "";
    return localDPObject.getDouble("Lng") + "," + localDPObject.getDouble("Lat");
  }

  public static String mac()
  {
    return NetworkUtils.mac();
  }

  public static String modelAndBrand()
  {
    return Build.MODEL + "@" + Build.BRAND;
  }

  public static String msisdn()
  {
    if (!PermissionCheckHelper.isPermissionGranted(DPApplication.instance(), "android.permission.READ_PHONE_STATE"))
      return "";
    try
    {
      String str = ((TelephonyManager)DPApplication.instance().getSystemService("phone")).getLine1Number();
      return str;
    }
    catch (Throwable localThrowable)
    {
    }
    return "";
  }

  public static String operatorAndNetwork()
  {
    return CellUtils.operatorName() + "@" + CellUtils.networkType();
  }

  public static String procLastModifiedTime()
  {
    try
    {
      File localFile = new File("/system/build.prop");
      if (!localFile.exists())
      {
        Log.w("file not exist");
        return "";
      }
      long l = localFile.lastModified();
      return String.valueOf(l);
    }
    catch (Exception localException)
    {
      Log.e(localException.toString());
    }
    return "";
  }

  private static String qemu()
  {
    try
    {
      Object localObject = Class.forName("android.os.SystemProperties");
      localObject = (String)(String)((Class)localObject).getMethod("get", new Class[] { String.class, String.class }).invoke(localObject, new Object[] { "ro.kernel.qemu", "unknown" });
      return localObject;
    }
    catch (Exception localException)
    {
      Log.e(localException.toString());
    }
    return (String)"unknown";
  }

  public static String resolution()
  {
    DisplayMetrics localDisplayMetrics = DPApplication.instance().getResources().getDisplayMetrics();
    return localDisplayMetrics.widthPixels + "*" + localDisplayMetrics.heightPixels;
  }

  public static String roaming()
  {
    if (CellUtils.isRoaming())
      return "1";
    return "0";
  }

  private static String rootInfo()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    if ((new File("/system/bin/su").exists()) && (isExecutable("/system/bin/su")))
    {
      localStringBuffer.append("1");
      if ((!new File("/system/xbin/su").exists()) || (!isExecutable("/system/xbin/su")))
        break label90;
      localStringBuffer.append("1");
    }
    while (true)
    {
      return localStringBuffer.toString();
      localStringBuffer.append("0");
      break;
      label90: localStringBuffer.append("0");
    }
  }

  public static String screen(Context paramContext)
  {
    paramContext = paramContext.getResources().getDisplayMetrics();
    return paramContext.widthPixels + "x" + paramContext.heightPixels;
  }

  public static String simState()
  {
    return String.valueOf(CellUtils.simState());
  }

  public static String source()
  {
    String str2 = com.dianping.app.Environment.source();
    String str1 = str2;
    if (str2 == null)
      str1 = "";
    return str1;
  }

  public static String storage()
  {
    Object localObject = android.os.Environment.getExternalStorageDirectory();
    if (localObject == null)
      return "";
    localObject = new StatFs(((File)localObject).getPath());
    long l1 = bytes2MillionBytes(((StatFs)localObject).getAvailableBlocks(), ((StatFs)localObject).getBlockSize());
    long l2 = bytes2MillionBytes(((StatFs)localObject).getBlockCount(), ((StatFs)localObject).getBlockSize());
    return (String)(l1 + "@" + l2);
  }

  public static String token()
  {
    String str2 = DPApplication.instance().accountService().token();
    String str1 = str2;
    if (str2 == null)
      str1 = "";
    return str1;
  }

  public static String uuid()
  {
    return com.dianping.app.Environment.uuid();
  }

  public static String wifiInfo()
  {
    return NetworkUtils.wifiInfo();
  }

  public static String wifiIp()
  {
    try
    {
      Object localObject = (WifiManager)DPApplication.instance().getSystemService("wifi");
      if (((WifiManager)localObject).getWifiState() == 3)
      {
        int i = ((WifiManager)localObject).getConnectionInfo().getIpAddress();
        localObject = (i & 0xFF) + "." + (i >> 8 & 0xFF) + "." + (i >> 16 & 0xFF) + "." + (i >> 24 & 0xFF);
        return localObject;
      }
    }
    catch (Throwable localThrowable)
    {
    }
    return (String)"";
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.util.DeviceUtils
 * JD-Core Version:    0.6.0
 */