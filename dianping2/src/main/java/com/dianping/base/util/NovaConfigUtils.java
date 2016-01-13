package com.dianping.base.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.net.Uri;
import com.dianping.app.DPActivity;
import com.dianping.app.DPApplication;
import com.dianping.app.Environment;
import com.dianping.base.app.NovaApplication;
import com.dianping.util.DeviceUtils;
import com.dianping.util.network.NetworkUtils;
import com.dianping.v1.R.drawable;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

public class NovaConfigUtils
{
  private static final String FEATURE_CONFIG_FILE = "feature.txt";
  public static final String IS_SHOW_LIST_IMAGE = "isShowListImage";
  public static boolean isShowShopImg = true;
  private HashMap<String, String> featureConfigMap = new HashMap();

  private NovaConfigUtils()
  {
    loadProperties(NovaApplication.instance(), "feature.txt");
  }

  public static int getCategoryIconId(int paramInt)
  {
    switch (paramInt)
    {
    default:
      return R.drawable.ic_category_none;
    case 0:
      return R.drawable.ic_category_0;
    case 2:
      return R.drawable.ic_category_20;
    case 10:
      return R.drawable.ic_category_10;
    case 15:
      return R.drawable.ic_category_15;
    case 20:
      return R.drawable.ic_category_20;
    case 25:
      return R.drawable.ic_category_25;
    case 30:
      return R.drawable.ic_category_30;
    case 35:
      return R.drawable.ic_category_35;
    case 40:
      return R.drawable.ic_category_40;
    case 45:
      return R.drawable.ic_category_45;
    case 50:
      return R.drawable.ic_category_50;
    case 55:
      return R.drawable.ic_category_55;
    case 60:
      return R.drawable.ic_category_60;
    case 65:
      return R.drawable.ic_category_65;
    case 70:
      return R.drawable.ic_category_70;
    case 75:
      return R.drawable.ic_category_75;
    case 80:
      return R.drawable.ic_category_80;
    case 85:
      return R.drawable.ic_category_85;
    case 90:
      return R.drawable.ic_category_90;
    case 160:
      return R.drawable.ic_category_160;
    case 2002:
      return R.drawable.ic_category_2002;
    case 125:
      return R.drawable.ic_category_125;
    case 161:
      return R.drawable.ic_category_161;
    case 188:
      return R.drawable.ic_category_188;
    case 193:
      return R.drawable.ic_category_193;
    case 258:
      return R.drawable.ic_category_258;
    case 27769:
      return R.drawable.ic_category_27769;
    case 27814:
      return R.drawable.ic_category_27814;
    case -2147483648:
    }
    return R.drawable.ic_category_2147483648;
  }

  public static NovaConfigUtils instance()
  {
    return NovaFeatureConfigInner.instance;
  }

  public static boolean isShowImageInMobileNetwork()
  {
    boolean bool2 = true;
    boolean bool1 = bool2;
    if (!DPActivity.preferences().getBoolean("isShowListImage", true))
      if (!NetworkUtils.isWIFIConnection(DPApplication.instance()))
        break label37;
    label37: for (bool1 = bool2; ; bool1 = false)
    {
      isShowShopImg = bool1;
      return isShowShopImg;
    }
  }

  private void loadProperties(Context paramContext, String paramString)
  {
    Properties localProperties = new Properties();
    Context localContext2 = null;
    Context localContext1 = null;
    try
    {
      paramContext = paramContext.getAssets().open(paramString);
      localContext1 = paramContext;
      localContext2 = paramContext;
      localProperties.load(paramContext);
      localContext1 = paramContext;
      localContext2 = paramContext;
      paramString = localProperties.keySet().iterator();
      while (true)
      {
        localContext1 = paramContext;
        localContext2 = paramContext;
        if (!paramString.hasNext())
          break;
        localContext1 = paramContext;
        localContext2 = paramContext;
        Object localObject = paramString.next();
        localContext1 = paramContext;
        localContext2 = paramContext;
        this.featureConfigMap.put((String)localObject, (String)localProperties.get(localObject));
      }
    }
    catch (java.lang.Exception paramContext)
    {
      if (localContext1 != null);
      try
      {
        localContext1.close();
        do
          return;
        while (paramContext == null);
        try
        {
          paramContext.close();
          return;
        }
        catch (IOException paramContext)
        {
          paramContext.printStackTrace();
          return;
        }
      }
      catch (IOException paramContext)
      {
        paramContext.printStackTrace();
        return;
      }
    }
    finally
    {
      if (localContext2 == null);
    }
    try
    {
      localContext2.close();
      throw paramContext;
    }
    catch (IOException paramString)
    {
      while (true)
        paramString.printStackTrace();
    }
  }

  public static String processParam(Context paramContext, String paramString)
  {
    int i = paramString.indexOf('?');
    if (i < 0)
      return paramString;
    String str2 = paramString.substring(0, i + 1);
    String str1 = paramString.substring(i + 1);
    paramString = str1;
    if (str1.contains("agent=!"))
      paramString = str1.replace("agent=!", "agent=android");
    str1 = paramString;
    if (paramString.contains("agent=*"))
      str1 = paramString.replace("agent=*", "agent=android");
    paramString = str1;
    if (str1.contains("version=!"))
      paramString = str1.replace("version=!", "version=" + Environment.versionName());
    str1 = paramString;
    if (paramString.contains("version=*"))
      str1 = paramString.replace("version=*", "version=" + Environment.versionName());
    paramString = str1;
    if (str1.contains("screen=!"))
      paramString = str1.replace("screen=!", "screen=" + DeviceUtils.screen(paramContext));
    str1 = paramString;
    if (paramString.contains("screen=*"))
      str1 = paramString.replace("screen=*", "screen=" + DeviceUtils.screen(paramContext));
    paramContext = str1;
    if (str1.contains("sessionid=!"))
      paramContext = str1.replace("sessionid=!", "sessionid=" + Environment.sessionId());
    paramString = paramContext;
    if (paramContext.contains("sessionid=*"))
      paramString = paramContext.replace("sessionid=*", "sessionid=" + Environment.sessionId());
    paramContext = paramString;
    if (paramString.contains("deviceid=!"))
      paramContext = paramString.replace("deviceid=!", "deviceid=" + DeviceUtils.imei());
    paramString = paramContext;
    if (paramContext.contains("deviceid=*"))
      paramString = paramContext.replace("deviceid=*", "deviceid=" + DeviceUtils.imei());
    paramContext = paramString;
    if (paramString.contains("uuid=!"))
      paramContext = paramString.replace("uuid=!", "uuid=" + DeviceUtils.uuid());
    paramString = paramContext;
    if (paramContext.contains("uuid=*"))
      paramString = paramContext.replace("uuid=*", "uuid=" + DeviceUtils.uuid());
    paramContext = paramString;
    if (paramString.contains("dpid=!"))
      paramContext = paramString.replace("dpid=!", "dpid=" + DeviceUtils.dpid());
    paramString = paramContext;
    if (paramContext.contains("dpid=*"))
      paramString = paramContext.replace("dpid=*", "dpid=" + DeviceUtils.dpid());
    return str2 + paramString;
  }

  public static void showDialogInMobileNetworkWhenFirstStart(DPActivity paramDPActivity)
  {
    if (paramDPActivity == null);
    do
      return;
    while ((!DPActivity.preferences().getBoolean("isFirstTimeInSPLActivity", true)) || (NetworkUtils.isWIFIConnection(paramDPActivity)));
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(paramDPActivity);
    localBuilder.setTitle("提示");
    localBuilder.setMessage("您可以关闭在“2G/3G网络下显示图片”，更省流量！");
    localBuilder.setNegativeButton("设置", new DialogInterface.OnClickListener(paramDPActivity)
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        paramDialogInterface = new Intent("android.intent.action.VIEW", Uri.parse("dianping://imagesetting"));
        this.val$activity.startActivity(paramDialogInterface);
      }
    });
    localBuilder.setPositiveButton("知道了", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
      }
    });
    localBuilder.create().show();
    paramDPActivity = DPActivity.preferences().edit();
    paramDPActivity.putBoolean("isFirstTimeInSPLActivity", false);
    paramDPActivity.commit();
  }

  public boolean disableCheckUpdate()
  {
    return Boolean.parseBoolean((String)this.featureConfigMap.get("disableCheckUpdate"));
  }

  private static class NovaFeatureConfigInner
  {
    static NovaConfigUtils instance = new NovaConfigUtils(null);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.util.NovaConfigUtils
 * JD-Core Version:    0.6.0
 */