package com.dianping.map.utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Build;
import android.widget.Toast;
import com.dianping.app.CityConfig;
import com.dianping.app.DPApplication;
import com.dianping.archive.ArchiveException;
import com.dianping.archive.DPObject;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.locationservice.LocationService;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.util.Log;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.drawable;
import com.dianping.widget.view.GAHelper;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class MapUtils
{
  public static final int BAIDU_MAP_BIT = 16;
  public static final int BRUT_MAP_BIT = 2;
  public static final String COORD_TYPE_bd09ll = "bd09ll";
  public static final String COORD_TYPE_gcj02 = "gcj02";
  public static final String COORD_TYPE_wgs84 = "wgs84";
  public static final int DEFAULT_ZOOM_LEVEL = 16;
  public static final int GAODE_MAP_BIT = 32;
  public static final int GOOGLE_MAP_BIT = 1;
  public static final String K_LAT = "Latitude";
  public static final String K_LNG = "Longitude";
  public static final int MAPBAR_MAP_BIT = 64;
  private static final int MAPTYPE_AMAP = 4;
  private static final int MAPTYPE_BAIDU = 3;
  private static final int MAPTYPE_GOOGLE = 1;
  private static final int MAPTYPE_MAPBAR = 2;
  private static final int MAPTYPE_SOGOU = 8;
  private static final int MAPTYPE_TENCENT = 6;
  public static final int MAP_BAIDU = 4;
  public static final int MAP_BAIDUSAMSUNG = 7;
  public static final int MAP_BRUT = 3;
  public static final int MAP_GAODE = 1;
  public static final int MAP_GOOGLE = 2;
  public static final int MAP_INNER_MAP = 8;
  public static final int MAP_MAPBAR = 5;
  private static final int MAP_MAX = 9;
  public static final int MAP_SOGOU = 6;
  public static final int MAP_TENCENT = 0;
  public static final int MODE_BUS = 1;
  public static final int MODE_CAR = 2;
  public static final int MODE_NO_NAVI = 0;
  public static final int MODE_WALKING = 3;
  public static final int SOGOU_MAP_BIT = 128;
  private static MapUtils.RouteMapInfo[] SRouteMapInfo = new MapUtils.RouteMapInfo[9];
  public static final int TENCENT_MAP_BIT = 256;

  static
  {
    MapUtils.RouteMapInfo localRouteMapInfo = new MapUtils.RouteMapInfo();
    localRouteMapInfo.type = 0;
    localRouteMapInfo.name = "腾讯地图";
    localRouteMapInfo.pkg = "com.tencent.map";
    localRouteMapInfo.mapType = 6;
    localRouteMapInfo.configBit = 256;
    SRouteMapInfo[0] = localRouteMapInfo;
    localRouteMapInfo = new MapUtils.RouteMapInfo();
    localRouteMapInfo.type = 2;
    localRouteMapInfo.name = "谷歌地图";
    localRouteMapInfo.pkg = "com.google.android.apps.maps";
    localRouteMapInfo.mapType = 1;
    localRouteMapInfo.configBit = 1;
    SRouteMapInfo[2] = localRouteMapInfo;
    localRouteMapInfo = new MapUtils.RouteMapInfo();
    localRouteMapInfo.type = 3;
    localRouteMapInfo.name = "谷歌地图 (Brut)";
    localRouteMapInfo.pkg = "brut.googlemaps";
    localRouteMapInfo.mapType = 1;
    localRouteMapInfo.configBit = 2;
    SRouteMapInfo[3] = localRouteMapInfo;
    localRouteMapInfo = new MapUtils.RouteMapInfo();
    localRouteMapInfo.type = 4;
    localRouteMapInfo.name = "百度地图";
    localRouteMapInfo.pkg = "com.baidu.BaiduMap";
    localRouteMapInfo.mapType = 3;
    localRouteMapInfo.configBit = 16;
    SRouteMapInfo[4] = localRouteMapInfo;
    localRouteMapInfo = new MapUtils.RouteMapInfo();
    localRouteMapInfo.type = 5;
    localRouteMapInfo.name = "图吧地图";
    localRouteMapInfo.pkg = "com.mapbar.android.mapbarmap";
    localRouteMapInfo.mapType = 2;
    localRouteMapInfo.configBit = 64;
    SRouteMapInfo[5] = localRouteMapInfo;
    localRouteMapInfo = new MapUtils.RouteMapInfo();
    localRouteMapInfo.type = 1;
    localRouteMapInfo.name = "高德地图";
    localRouteMapInfo.pkg = "com.autonavi.minimap";
    localRouteMapInfo.mapType = 4;
    localRouteMapInfo.configBit = 32;
    SRouteMapInfo[1] = localRouteMapInfo;
    localRouteMapInfo = new MapUtils.RouteMapInfo();
    localRouteMapInfo.type = 6;
    localRouteMapInfo.name = "搜狗地图";
    localRouteMapInfo.pkg = "com.sogou.map.android.maps";
    localRouteMapInfo.mapType = 8;
    localRouteMapInfo.configBit = 128;
    SRouteMapInfo[6] = localRouteMapInfo;
    localRouteMapInfo = new MapUtils.RouteMapInfo();
    localRouteMapInfo.type = 7;
    localRouteMapInfo.name = "百度地图三星";
    localRouteMapInfo.pkg = "com.baidu.BaiduMap.samsung";
    localRouteMapInfo.mapType = 3;
    localRouteMapInfo.configBit = 16;
    SRouteMapInfo[7] = localRouteMapInfo;
    localRouteMapInfo = new MapUtils.RouteMapInfo();
    localRouteMapInfo.type = 8;
    localRouteMapInfo.name = "在本应用中查看";
    localRouteMapInfo.mapType = 8;
    localRouteMapInfo.configBit = 0;
    SRouteMapInfo[8] = localRouteMapInfo;
    initMapSupport();
  }

  private static Uri getAddressMapUri(double paramDouble1, double paramDouble2, DPObject paramDPObject)
  {
    paramDPObject = new DecimalFormat("###.######");
    StringBuffer localStringBuffer = new StringBuffer("geo:");
    localStringBuffer.append(paramDPObject.format(paramDouble1));
    localStringBuffer.append(',');
    localStringBuffer.append(paramDPObject.format(paramDouble2));
    localStringBuffer.append("?q=");
    localStringBuffer.append(paramDPObject.format(paramDouble1));
    localStringBuffer.append(",");
    localStringBuffer.append(paramDPObject.format(paramDouble2));
    localStringBuffer.append("(商户位置)");
    localStringBuffer.append("?z=15");
    return Uri.parse(localStringBuffer.toString());
  }

  private static String getBaiduMapUri(DPObject paramDPObject, double paramDouble1, double paramDouble2, int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("intent://map/direction?origin=");
    localStringBuilder.append("&destination=latlng:" + paramDouble1 + "," + paramDouble2);
    localStringBuilder.append("|name:" + paramDPObject.getString("Address"));
    if (paramInt == 1)
      paramDPObject = "transit";
    while (true)
    {
      localStringBuilder.append("&mode=" + paramDPObject);
      localStringBuilder.append("&coord_type=wgs84");
      localStringBuilder.append("#Intent;scheme=bdapp;package=" + SRouteMapInfo[4].pkg + ";end");
      return localStringBuilder.toString();
      if (paramInt == 3)
      {
        paramDPObject = "walking";
        continue;
      }
      paramDPObject = "driving";
    }
  }

  private static Intent getGMapIntent(DPObject paramDPObject, double paramDouble1, double paramDouble2, int paramInt)
  {
    return getGoogleMapIntent(paramDPObject, paramDouble1, paramDouble2, paramInt, SRouteMapInfo[2].pkg);
  }

  private static Uri getGaodeMapUri(DPObject paramDPObject, double paramDouble1, double paramDouble2, int paramInt)
  {
    int i = 1;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("androidamap://route?sourceApplication=softname");
    localStringBuilder.append("&dlat=" + paramDouble1 + "&dlon=" + paramDouble2);
    localStringBuilder.append("&dname=" + paramDPObject.getString("Address"));
    localStringBuilder.append("&dev=0&m=0");
    if (paramInt == 1)
      paramInt = i;
    while (true)
    {
      localStringBuilder.append("&t=" + paramInt);
      return Uri.parse(localStringBuilder.toString());
      if (paramInt == 3)
      {
        paramInt = 4;
        continue;
      }
      paramInt = 2;
    }
  }

  private static Intent getGoogleMapIntent(DPObject paramDPObject, double paramDouble1, double paramDouble2, int paramInt, String paramString)
  {
    DecimalFormat localDecimalFormat = new DecimalFormat("###.######");
    String str = paramDPObject.getString("Name");
    StringBuffer localStringBuffer = new StringBuffer("http://maps.google.com/maps?hl=zh&mrt=loc");
    if (paramInt == 1)
      paramDPObject = "transit";
    while (true)
    {
      localStringBuffer.append("&mode=" + paramDPObject);
      localStringBuffer.append("&saddr=&daddr=");
      localStringBuffer.append(localDecimalFormat.format(paramDouble1));
      localStringBuffer.append(',');
      localStringBuffer.append(localDecimalFormat.format(paramDouble2));
      localStringBuffer.append("(").append(URLEncoder.encode(str)).append(")");
      paramDPObject = new Intent("android.intent.action.VIEW", Uri.parse(localStringBuffer.toString()));
      paramDPObject.setPackage(paramString);
      return paramDPObject;
      if (paramInt == 3)
      {
        paramDPObject = "walking";
        continue;
      }
      paramDPObject = "driving";
    }
  }

  private static RequestHandler<MApiRequest, MApiResponse> getMApiRequestHandler(Context paramContext, int paramInt1, int paramInt2, DPObject paramDPObject, ProgressDialog paramProgressDialog)
  {
    return new MapUtils.7(paramProgressDialog, paramDPObject, paramInt1, paramInt2, paramContext);
  }

  private static Intent getMapIntent(DPObject paramDPObject, double paramDouble1, double paramDouble2, int paramInt1, int paramInt2)
  {
    Object localObject = null;
    switch (paramInt1)
    {
    case 3:
    default:
      paramDPObject = getAddressMapUri(paramDouble1, paramDouble2, paramDPObject);
    case 0:
    case 4:
    case 2:
    case 1:
    }
    while (true)
    {
      paramDPObject = new Intent("android.intent.action.VIEW", paramDPObject);
      paramDPObject.setPackage(SRouteMapInfo[paramInt1].pkg);
      return paramDPObject;
      paramDPObject = getTencentMapUri(paramDouble1, paramDouble2, paramDPObject, paramInt2);
      continue;
      try
      {
        paramDPObject = Intent.getIntent(getBaiduMapUri(paramDPObject, paramDouble1, paramDouble2, paramInt2));
        return paramDPObject;
      }
      catch (URISyntaxException paramDPObject)
      {
        paramDPObject.printStackTrace();
        paramDPObject = localObject;
      }
      continue;
      return getGMapIntent(paramDPObject, paramDouble1, paramDouble2, paramInt2);
      paramDPObject = getGaodeMapUri(paramDPObject, paramDouble1, paramDouble2, paramInt2);
    }
  }

  private static ArrayList<MapUtils.RouteMapInfo> getSupportedRouteMaps(Context paramContext)
  {
    ArrayList localArrayList = new ArrayList();
    paramContext = paramContext.getPackageManager();
    MapUtils.RouteMapInfo[] arrayOfRouteMapInfo = SRouteMapInfo;
    int j = arrayOfRouteMapInfo.length;
    int i = 0;
    if (i < j)
    {
      MapUtils.RouteMapInfo localRouteMapInfo = arrayOfRouteMapInfo[i];
      if (isMapDisable(localRouteMapInfo.configBit));
      while (true)
      {
        i += 1;
        break;
        try
        {
          if (localRouteMapInfo.pkg == null)
            continue;
          localRouteMapInfo.icon = paramContext.getApplicationIcon(localRouteMapInfo.pkg);
          localArrayList.add(localRouteMapInfo);
        }
        catch (PackageManager.NameNotFoundException localNameNotFoundException)
        {
        }
      }
    }
    return localArrayList;
  }

  private static Uri getTencentMapUri(double paramDouble1, double paramDouble2, DPObject paramDPObject, int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("sosomap://map/routeplan?");
    Object localObject = (LocationService)DPApplication.instance().getService("location");
    if (((LocationService)localObject).hasLocation());
    try
    {
      localObject = (Location)((LocationService)localObject).location().decodeToObject(Location.DECODER);
      localStringBuilder.append("&from=" + ((Location)localObject).address());
      localStringBuilder.append("&fromcoord=" + ((Location)localObject).offsetLatitude() + "," + ((Location)localObject).offsetLongitude());
      if (paramInt == 1)
      {
        localObject = "bus";
        localStringBuilder.append("&type=" + (String)localObject);
        localStringBuilder.append("&to=" + paramDPObject.getString("Address"));
        localStringBuilder.append("&tocoord=" + paramDouble1 + "," + paramDouble2);
        localStringBuilder.append("&referer=dianping_client");
        return Uri.parse(localStringBuilder.toString());
      }
    }
    catch (ArchiveException str)
    {
      while (true)
      {
        Log.e(localArchiveException.toString());
        continue;
        if (paramInt == 3)
        {
          str = "walking";
          continue;
        }
        String str = "drive";
      }
    }
  }

  private static String getVersion(Context paramContext, MapUtils.RouteMapInfo paramRouteMapInfo)
  {
    try
    {
      paramContext = paramContext.getPackageManager().getPackageInfo(paramRouteMapInfo.pkg, 128).versionName;
      return paramContext;
    }
    catch (PackageManager.NameNotFoundException paramContext)
    {
    }
    return "";
  }

  public static void gotoNavi(Context paramContext, DPObject paramDPObject)
  {
    if ((paramContext == null) || (paramDPObject == null))
      return;
    if (!haveLatAndAddress(paramDPObject))
    {
      showNoAddressToast(paramContext);
      return;
    }
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://mapnavigation").buildUpon().build());
    localIntent.putExtra("city", DPApplication.instance().cityConfig().currentCity().name());
    localIntent.addFlags(268435456);
    localIntent.putExtra("shop", paramDPObject);
    paramContext.startActivity(localIntent);
  }

  public static boolean haveLatAndAddress(DPObject paramDPObject)
  {
    if (paramDPObject == null);
    do
      return false;
    while ((paramDPObject.getDouble("Latitude") == 0.0D) || (paramDPObject.getDouble("Longitude") == 0.0D) || (paramDPObject.getString("Address") == null));
    return true;
  }

  public static void initMapSupport()
  {
    String str = Build.CPU_ABI;
    if ((!TextUtils.isEmpty(str)) && (str.contains("mips")));
    for (boolean bool = true; ; bool = false)
    {
      setMapNotSupport(bool);
      return;
    }
  }

  private static boolean isGoogleMapsInstalled(Context paramContext)
  {
    try
    {
      paramContext.getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0);
      return true;
    }
    catch (PackageManager.NameNotFoundException paramContext)
    {
    }
    return false;
  }

  private static boolean isMapDisable(int paramInt)
  {
    return (ConfigHelper.configMapDisable & paramInt) != 0;
  }

  public static boolean isMapNOTSupported()
  {
    return DPApplication.instance().getSharedPreferences("mapConfig", 0).getBoolean("key_isMapNotSupport", false);
  }

  private static boolean isTencentMapInstall(Context paramContext)
  {
    paramContext = paramContext.getPackageManager();
    try
    {
      paramContext.getApplicationIcon(SRouteMapInfo[0].pkg);
      return true;
    }
    catch (Exception paramContext)
    {
    }
    return false;
  }

  public static void launchMap(Context paramContext, DPObject paramDPObject)
  {
    launchMap(paramContext, paramDPObject, false, 0);
  }

  private static void launchMap(Context paramContext, DPObject paramDPObject, boolean paramBoolean, int paramInt)
  {
    if (!haveLatAndAddress(paramDPObject))
    {
      showNoAddressToast(paramContext);
      return;
    }
    if (paramDPObject.getBoolean("IsForeignShop"))
    {
      GAHelper.instance().contextStatisticsEvent(paramContext, "map_drive", "谷歌地图", 2147483647, "tap");
      naviGoogleMapApi(paramContext, paramDPObject, paramInt);
      return;
    }
    if (isTencentMapInstall(paramContext))
      try
      {
        launchMap(paramContext, paramDPObject, SRouteMapInfo[0].type, paramInt);
        GAHelper.instance().contextStatisticsEvent(paramContext, "map_install", "tencent", 0, "tap");
        return;
      }
      catch (Exception localException)
      {
      }
    ArrayList localArrayList = getSupportedRouteMaps(paramContext);
    if (paramBoolean)
    {
      MapUtils.RouteMapInfo localRouteMapInfo = SRouteMapInfo[8];
      localRouteMapInfo.mapType = paramInt;
      localRouteMapInfo.icon = paramContext.getResources().getDrawable(R.drawable.map_navigation_route);
      localArrayList.add(localRouteMapInfo);
    }
    showSupportedMaps(paramContext, paramInt, localArrayList, paramDPObject);
  }

  private static boolean launchMap(Context paramContext, DPObject paramDPObject, int paramInt1, int paramInt2)
  {
    if (paramInt1 == 8)
      gotoNavi(paramContext, paramDPObject);
    while (true)
    {
      new ArrayList().add(new BasicNameValuePair("shopid", String.valueOf(paramDPObject.getInt("ID"))));
      GAHelper.instance().contextStatisticsEvent(paramContext, "map_drive", SRouteMapInfo[paramInt1].name, 2147483647, "tap");
      return true;
      launchThirdMapInternal(paramContext, paramDPObject, paramInt1, paramInt2);
    }
  }

  public static void launchMapRoute(Context paramContext, DPObject paramDPObject, int paramInt)
  {
    launchMap(paramContext, paramDPObject, false, paramInt);
  }

  private static void launchThirdMapInternal(Context paramContext, DPObject paramDPObject, int paramInt1, int paramInt2)
  {
    ProgressDialog localProgressDialog = new ProgressDialog(paramContext);
    RequestHandler localRequestHandler = getMApiRequestHandler(paramContext, paramInt1, paramInt2, paramDPObject, localProgressDialog);
    showWaitDlg(localProgressDialog, sendMapRequest(paramContext, paramInt1, paramDPObject.getInt("ID"), localRequestHandler), localRequestHandler);
  }

  private static void naviGoogleMapApi(Context paramContext, DPObject paramDPObject, int paramInt)
  {
    if (!isGoogleMapsInstalled(paramContext))
    {
      if (!(paramContext instanceof DPApplication))
      {
        paramDPObject = new AlertDialog.Builder(paramContext);
        paramDPObject.setTitle("提示");
        paramDPObject.setMessage("当前城市的地图只能在谷歌地图中查看哦，是否去下载？");
        paramDPObject.setPositiveButton("下载", new MapUtils.3(paramContext));
        paramDPObject.setNegativeButton("取消", new MapUtils.4());
        paramDPObject.create().show();
      }
      return;
    }
    new ArrayList().add(new BasicNameValuePair("shopid", String.valueOf(paramDPObject.getInt("ID"))));
    launchThirdMapInternal(paramContext, paramDPObject, 2, paramInt);
  }

  private static MApiRequest sendMapRequest(Context paramContext, int paramInt1, int paramInt2, RequestHandler<MApiRequest, MApiResponse> paramRequestHandler)
  {
    paramContext = getVersion(paramContext, SRouteMapInfo[paramInt1]);
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/offsetshoppoint.bin").buildUpon();
    localBuilder.appendQueryParameter("shopid", String.valueOf(paramInt2)).appendQueryParameter("maptype", String.valueOf(SRouteMapInfo[paramInt1].mapType)).appendQueryParameter("mapversion", paramContext);
    paramContext = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.NORMAL);
    DPApplication.instance().mapiService().exec(paramContext, paramRequestHandler);
    return paramContext;
  }

  public static void setMapNotSupport(boolean paramBoolean)
  {
    DPApplication.instance().getSharedPreferences("mapConfig", 0).edit().putBoolean("key_isMapNotSupport", paramBoolean).apply();
  }

  public static void showNoAddressToast(Context paramContext)
  {
    paramContext = Toast.makeText(paramContext, "没有该商户的详细坐标地址，无法展示", 0);
    paramContext.setGravity(17, 0, 0);
    paramContext.show();
  }

  private static void showSupportedMaps(Context paramContext, int paramInt, ArrayList<MapUtils.RouteMapInfo> paramArrayList, DPObject paramDPObject)
  {
    if ((paramArrayList.isEmpty()) || (!"腾讯地图".equals(((MapUtils.RouteMapInfo)paramArrayList.get(0)).name)))
    {
      SRouteMapInfo[0].icon = paramContext.getResources().getDrawable(R.drawable.tencent_map_icon);
      paramArrayList.add(0, SRouteMapInfo[0]);
    }
    if (!(paramContext instanceof DPApplication))
    {
      AlertDialog.Builder localBuilder = new AlertDialog.Builder(paramContext);
      localBuilder.setTitle("请选择地图");
      localBuilder.setAdapter(new MapUtils.1(paramContext, paramArrayList), new MapUtils.2(paramArrayList, paramContext, paramDPObject, paramInt));
      localBuilder.create().show();
    }
  }

  public static void showSupportedMaps(Context paramContext, DPObject paramDPObject, int paramInt)
  {
    if (!haveLatAndAddress(paramDPObject))
    {
      showNoAddressToast(paramContext);
      return;
    }
    if (paramDPObject.getBoolean("IsForeignShop"))
    {
      naviGoogleMapApi(paramContext, paramDPObject, paramInt);
      return;
    }
    showSupportedMaps(paramContext, paramInt, getSupportedRouteMaps(paramContext), paramDPObject);
  }

  private static ProgressDialog showWaitDlg(ProgressDialog paramProgressDialog, MApiRequest paramMApiRequest, RequestHandler<MApiRequest, MApiResponse> paramRequestHandler)
  {
    paramProgressDialog.setOnCancelListener(new MapUtils.5(paramMApiRequest, paramRequestHandler));
    paramProgressDialog.setOnKeyListener(new MapUtils.6());
    paramProgressDialog.setMessage("载入中...");
    paramProgressDialog.show();
    return paramProgressDialog;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.map.utils.MapUtils
 * JD-Core Version:    0.6.0
 */