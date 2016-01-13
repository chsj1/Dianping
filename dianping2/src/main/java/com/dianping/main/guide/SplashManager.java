package com.dianping.main.guide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import com.dianping.app.DPActivity;
import com.dianping.app.DPApplication;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.dataservice.http.BasicHttpRequest;
import com.dianping.dataservice.http.HttpRequest;
import com.dianping.dataservice.http.HttpResponse;
import com.dianping.dataservice.http.HttpService;
import com.dianping.monitor.MonitorService;
import com.dianping.statistics.StatisticsService;
import com.dianping.util.Log;
import com.dianping.util.ThirdGaUtil.AdvertisementGa;
import com.dianping.util.ViewUtils;
import com.dianping.util.encrypt.Md5;
import com.dianping.util.network.NetworkUtils;
import com.dianping.v1.R.drawable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.http.NameValuePair;
import org.json.JSONObject;

public class SplashManager
{
  private static final String ACITON_SHOW_SPLASH = "com.dianping.action.SHOW_SPLASH";
  private static final String DEFAULT_SPLASH_URL = "http://m1.s1.dpfile.com/sc/splash/${network_type}/${req_time}(${width}X${height}).jpg";
  public static final int DELAY_SHOW_TIME = 2000;
  public static final String SERVER_SPLASH_IMAGE = "server_splash_image.png";
  private static final String SOUFA_PICTURE_NAME = "splash.png";
  public static final String SPLASH_CLICK_GA_URL = "splash_click_ga_url";
  private static final String SPLASH_IMAGE_BEGINTIME_LONG = "splash_begin_time";
  private static final String SPLASH_IMAGE_CLICK_URL = "splash_click_url";
  private static final String SPLASH_IMAGE_OVERDUE_LONG = "splash_overdue_time";
  private static final String SPLASH_IMAGE_PIC_ID_LONG = "splash_pic_id";
  private static final String SPLASH_IMAGE_SHOWTIME_INT = "splash_image_showtime";
  private static final String SPLASH_IMAGE_URL_STRING = "splash_image_url";
  private static final String SPLASH_NEED_DOWNLOAD_BOOL = "need_download_splash_image";
  private static final String SPLASH_SHOW_GA_URL = "splash_show_ga_url";
  private static final String SPLASH_URL_TEMPLATE = "SplashUrlTemplate";
  private static final String TAG = "SplashManager";
  private static final int UP_HEIGHT_LEVEL = 20;
  static Context context;
  private static SplashManager instance = null;
  private static ScreenSize[] splashPicList;
  HttpRequest httpRequest;
  private boolean mNeedMonitor = false;

  static
  {
    context = null;
  }

  private SplashManager(Context paramContext)
  {
    context = paramContext.getApplicationContext();
  }

  private void downloadSplash(String paramString, boolean paramBoolean)
  {
    new AsyncTask(paramBoolean)
    {
      protected Void doInBackground(String[] paramArrayOfString)
      {
        Object localObject = (HttpService)DPApplication.instance().getService("http");
        SplashManager.this.httpRequest = BasicHttpRequest.httpGet(paramArrayOfString[0]);
        Log.i("SplashManager", "splashUrl=" + paramArrayOfString[0]);
        long l1 = SystemClock.uptimeMillis();
        paramArrayOfString = (HttpResponse)((HttpService)localObject).execSync(SplashManager.this.httpRequest);
        try
        {
          l1 = SystemClock.uptimeMillis() - l1;
          i = 0;
          localObject = SplashManager.this.httpRequest.input();
          if (localObject == null)
          {
            i = 0;
            if (!(paramArrayOfString.result() instanceof byte[]))
              break label233;
            j = ((byte[])(byte[])paramArrayOfString.result()).length;
            ((MonitorService)DPApplication.instance().getService("monitor")).pv(0L, "splash.down", 0, 0, paramArrayOfString.statusCode(), i, j, (int)l1);
            Log.i("SplashMonitor", "splash.down " + (int)l1);
            j = 0;
            localObject = paramArrayOfString.headers();
            if (localObject != null)
            {
              i = ((List)localObject).size();
              if (i != 0)
                break label239;
            }
            return null;
          }
        }
        catch (Exception paramArrayOfString)
        {
          while (true)
          {
            int i;
            label233: label239: int k;
            try
            {
              j = ((InputStream)localObject).available();
              i = j;
              continue;
              j = 0;
              continue;
              i = 0;
              if (i >= ((List)localObject).size())
                continue;
              k = j;
              if (!"Content-Type".equals(((NameValuePair)((List)localObject).get(i)).getName()))
                break label748;
              k = j;
              if (!((NameValuePair)((List)localObject).get(i)).getValue().startsWith("image"))
                break label748;
              k = 1;
              break label748;
              if (j == 0)
                continue;
              paramArrayOfString = (byte[])(byte[])paramArrayOfString.result();
              localObject = SplashManager.context.openFileOutput("server_splash_image.png", 0);
              ((FileOutputStream)localObject).write(paramArrayOfString);
              ((FileOutputStream)localObject).close();
              paramArrayOfString = new ExifInterface(new File(SplashManager.context.getFilesDir(), "server_splash_image.png").getAbsolutePath());
              Log.i("SplashManager", "exif=" + paramArrayOfString.getAttribute("UserComment"));
              SharedPreferences.Editor localEditor = DPActivity.preferences(SplashManager.context).edit();
              paramArrayOfString = paramArrayOfString.getAttribute("UserComment");
              if (paramArrayOfString != null)
                continue;
              paramArrayOfString = new JSONObject();
              if (!paramArrayOfString.optBoolean("IsDefault"))
                continue;
              SplashManager.this.resetSplash();
              Log.d("SplashManager", "show default splash");
              SplashManager.access$102(SplashManager.this, this.val$needMonitor);
              break;
              paramArrayOfString = new JSONObject(paramArrayOfString);
              continue;
              i = paramArrayOfString.optInt("PicShowTime", 2000);
              l1 = paramArrayOfString.optLong("StartTime", 9223372036854775807L);
              long l2 = paramArrayOfString.optLong("EndTime", 0L);
              long l3 = paramArrayOfString.optLong("PicID", -1L);
              String str = paramArrayOfString.optString("AdUrl", "");
              localObject = paramArrayOfString.optJSONObject("AdTrafficUrl");
              paramArrayOfString = (String)localObject;
              if (localObject != null)
                continue;
              paramArrayOfString = new JSONObject();
              localObject = paramArrayOfString.optString("ViewUrl", "");
              paramArrayOfString = paramArrayOfString.optString("ClickUrl", "");
              localEditor.putInt("splash_image_showtime", i).putLong("splash_overdue_time", l2).putLong("splash_begin_time", l1).putLong("splash_pic_id", l3).putString("splash_click_url", str).putString("splash_show_ga_url", (String)localObject).putString("splash_click_ga_url", paramArrayOfString).commit();
              l3 = System.currentTimeMillis() / 1000L;
              if ((l2 <= l3) || (l1 >= l3))
                continue;
              SplashManager.context.sendBroadcast(new Intent("com.dianping.action.SHOW_SPLASH"));
              continue;
              paramArrayOfString = paramArrayOfString;
              Log.e("SplashManager", "failed fetch image");
              paramArrayOfString.printStackTrace();
              break;
              SplashManager.this.resetSplash();
              continue;
              Log.d("SplashManager", "the file downloaded isn't an image");
            }
            catch (Exception localException)
            {
            }
            continue;
            label748: i += 1;
            int j = k;
          }
        }
        return (Void)null;
      }
    }
    .execute(new String[] { paramString });
  }

  private int getForggroundHeight(int paramInt1, int paramInt2)
  {
    if (paramInt2 == paramInt1)
      return 0;
    if (ViewUtils.getScreenHeightPixels(context) < paramInt1)
      paramInt1 = (ViewUtils.getScreenHeightPixels(context) + paramInt1) / 2 - paramInt2;
    while (true)
    {
      return paramInt1 - ViewUtils.getScreenHeightPixels(context) / 20;
      paramInt1 -= paramInt2;
    }
  }

  public static SplashManager instance(Context paramContext)
  {
    if (instance == null)
    {
      if (paramContext == null)
      {
        Log.e("You can't init SplashManager with a null context!");
        return null;
      }
      instance = new SplashManager(paramContext);
      splashPicList = new ScreenSize[5];
      splashPicList[0] = new ScreenSize(750, 1334);
      splashPicList[1] = new ScreenSize(720, 1280);
      splashPicList[2] = new ScreenSize(640, 1136);
      splashPicList[3] = new ScreenSize(640, 960);
      splashPicList[4] = new ScreenSize(480, 800);
    }
    return instance;
  }

  private boolean isBetterScreenSize(ScreenSize paramScreenSize1, ScreenSize paramScreenSize2)
  {
    int i = paramScreenSize1.ratio * paramScreenSize2.width - paramScreenSize2.ratio * paramScreenSize1.width;
    if (i > 0);
    do
    {
      return true;
      if (i < 0)
        return false;
    }
    while (paramScreenSize2.delta < paramScreenSize1.delta);
    return false;
  }

  private void resetSplash()
  {
    Object localObject = DPActivity.preferences(context).edit();
    ((SharedPreferences.Editor)localObject).remove("splash_overdue_time");
    ((SharedPreferences.Editor)localObject).remove("splash_image_url");
    ((SharedPreferences.Editor)localObject).putBoolean("need_download_splash_image", false);
    ((SharedPreferences.Editor)localObject).remove("splash_image_showtime");
    ((SharedPreferences.Editor)localObject).remove("splash_begin_time");
    ((SharedPreferences.Editor)localObject).commit();
    localObject = new File(context.getFilesDir(), "server_splash_image.png");
    if (((File)localObject).exists())
      ((File)localObject).delete();
  }

  private ScreenSize selectBestScreenSize()
  {
    Object localObject1 = splashPicList[0];
    int i = 1;
    while (i < splashPicList.length)
    {
      Object localObject2 = localObject1;
      if (isBetterScreenSize((ScreenSize)localObject1, splashPicList[i]))
        localObject2 = splashPicList[i];
      i += 1;
      localObject1 = localObject2;
    }
    return (ScreenSize)(ScreenSize)localObject1;
  }

  private Bitmap toConformBitmap(Bitmap paramBitmap1, Bitmap paramBitmap2)
  {
    if ((paramBitmap1 == null) || (paramBitmap2 == null))
      localBitmap = null;
    int i;
    int j;
    int k;
    int m;
    do
    {
      return localBitmap;
      i = paramBitmap1.getWidth();
      j = paramBitmap1.getHeight();
      k = paramBitmap2.getWidth();
      m = paramBitmap2.getHeight();
      localBitmap = paramBitmap2;
    }
    while (j - m < 100);
    Bitmap localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.ARGB_8888);
    Canvas localCanvas = new Canvas(localBitmap);
    localCanvas.drawBitmap(paramBitmap1, 0.0F, 0.0F, null);
    localCanvas.drawBitmap(paramBitmap2, (i - k) / 2.0F, getForggroundHeight(j, m), null);
    localCanvas.save(31);
    localCanvas.restore();
    return localBitmap;
  }

  public Bitmap getLoacalBitmapByAssets(Context paramContext, String paramString)
  {
    try
    {
      BitmapFactory.Options localOptions = new BitmapFactory.Options();
      localOptions.inDensity = 320;
      localOptions.inTargetDensity = context.getResources().getDisplayMetrics().densityDpi;
      paramContext = BitmapFactory.decodeStream(paramContext.getResources().getAssets().open(paramString), null, localOptions);
      return paramContext;
    }
    catch (IOException paramContext)
    {
    }
    return null;
  }

  public Bitmap getLocalSplashImage()
  {
    Object localObject4 = null;
    Object localObject3 = null;
    Bitmap localBitmap = null;
    Object localObject5 = null;
    Object localObject1 = localBitmap;
    try
    {
      FileInputStream localFileInputStream = context.openFileInput("server_splash_image.png");
      localObject1 = localObject5;
      if (localFileInputStream != null)
      {
        localObject3 = localFileInputStream;
        localObject1 = localBitmap;
        localObject4 = localFileInputStream;
        localBitmap = BitmapFactory.decodeStream(localFileInputStream);
        localObject3 = localFileInputStream;
        localObject1 = localBitmap;
        localObject4 = localFileInputStream;
        ((StatisticsService)((DPApplication)context).getService("statistics")).event("index5", "index5_splash", "", 0, null);
        localObject1 = localBitmap;
      }
      localObject4 = localObject1;
      if (localFileInputStream != null);
      try
      {
        localFileInputStream.close();
        localObject4 = localObject1;
        if (localObject4 != null)
        {
          localObject1 = DPActivity.preferences(context).getString("splash_show_ga_url", "");
          if (!TextUtils.isEmpty((CharSequence)localObject1))
            new AdvertisementGa().sendAdGA((String)localObject1);
        }
        return localObject4;
      }
      catch (IOException localIOException1)
      {
        while (true)
        {
          localIOException1.printStackTrace();
          localObject4 = localObject1;
        }
      }
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      while (true)
      {
        localObject4 = localIOException1;
        localFileNotFoundException.printStackTrace();
        localObject4 = localObject1;
        if (localIOException1 == null)
          continue;
        try
        {
          localIOException1.close();
          localObject4 = localObject1;
        }
        catch (IOException localIOException2)
        {
          localIOException2.printStackTrace();
          localObject4 = localObject1;
        }
      }
    }
    finally
    {
      if (localObject4 == null);
    }
    try
    {
      localObject4.close();
      throw localObject2;
    }
    catch (IOException localIOException3)
    {
      while (true)
        localIOException3.printStackTrace();
    }
  }

  public Bitmap getShoufaSplashImage()
  {
    Bitmap localBitmap1 = null;
    Bitmap localBitmap2 = getLoacalBitmapByAssets(context, "splash.png");
    if (localBitmap2 != null)
      localBitmap1 = toConformBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_splash_screen), localBitmap2);
    return localBitmap1;
  }

  public void getSplash(boolean paramBoolean)
  {
    Object localObject1 = DPActivity.preferences(context);
    String str2 = new SimpleDateFormat("yyyyMMddHH").format(new Date());
    long l1 = ((SharedPreferences)localObject1).getLong("splash_begin_time", 9223372036854775807L);
    long l2 = ((SharedPreferences)localObject1).getLong("splash_overdue_time", 0L);
    long l3 = System.currentTimeMillis() / 1000L;
    if ((l3 > l1) && (l3 < l2))
    {
      context.sendBroadcast(new Intent("com.dianping.action.SHOW_SPLASH"));
      Log.d("SplashManager", "load local splash");
      return;
    }
    Log.d("SplashManager", "local splash is invalid and download new splash");
    resetSplash();
    String str1 = context.getSharedPreferences("com.dianping.mapidebugagent", 0).getString("splashUrl", null);
    localObject1 = str1;
    if (TextUtils.isEmpty(str1))
    {
      str1 = ConfigHelper.SplashUrlTemplate;
      localObject1 = str1;
      if (TextUtils.isEmpty(str1))
        localObject1 = "http://m1.s1.dpfile.com/sc/splash/${network_type}/${req_time}(${width}X${height}).jpg";
    }
    if ("2G".equals(NetworkUtils.getNetworkType(context)));
    for (str1 = "2g"; ; str1 = "3g")
    {
      localObject2 = ConfigHelper.SplashPicSizeList;
      if (localObject2 == null)
        break;
      localObject2 = ((String)localObject2).split(",");
      if (localObject2.length <= 0)
        break;
      splashPicList = new ScreenSize[localObject2.length];
      int i = 0;
      while (i < localObject2.length)
      {
        int k = 0;
        int j = 0;
        localObject3 = localObject2[i].split("\\*");
        if (localObject3.length > 0)
          j = Integer.parseInt(localObject3[0]);
        if (localObject3.length > 1)
          k = Integer.parseInt(localObject3[1]);
        splashPicList[i] = new ScreenSize(k, j);
        i += 1;
      }
    }
    Object localObject2 = selectBestScreenSize();
    Object localObject3 = ConfigHelper.spDefaultKey;
    str2 = Md5.md5(str2 + "|" + (String)localObject3);
    downloadSplash(((String)localObject1).replace("${network_type}", str1).replace("${req_time}", str2).replace("${width}", String.valueOf(((ScreenSize)localObject2).width)).replace("${height}", String.valueOf(((ScreenSize)localObject2).height)), paramBoolean);
  }

  public String getSplashClickUrl()
  {
    return DPActivity.preferences(context).getString("splash_click_url", "");
  }

  public boolean getSplashNeedMonitor()
  {
    return this.mNeedMonitor;
  }

  public int getSplashShowTime()
  {
    if (!needShowSplashImage());
    int i;
    do
    {
      return 2000;
      i = DPActivity.preferences(context).getInt("splash_image_showtime", 2000);
    }
    while (i <= 0);
    return i;
  }

  public boolean needShowSplashImage()
  {
    SharedPreferences localSharedPreferences = DPActivity.preferences(context);
    if (localSharedPreferences.getLong("lastShowSplashPicId", -1L) != localSharedPreferences.getLong("splash_pic_id", -1L));
    SimpleDateFormat localSimpleDateFormat;
    do
    {
      return true;
      localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }
    while (!localSimpleDateFormat.format(Long.valueOf(localSharedPreferences.getLong("lastShowSplashTime", 0L))).equals(localSimpleDateFormat.format(Long.valueOf(System.currentTimeMillis()))));
    return false;
  }

  public void saveShowSplashImage()
  {
    SharedPreferences localSharedPreferences = DPActivity.preferences(context);
    long l = localSharedPreferences.getLong("splash_pic_id", -1L);
    localSharedPreferences.edit().putLong("lastShowSplashTime", System.currentTimeMillis()).putLong("lastShowSplashPicId", l).apply();
  }

  static class ScreenSize
  {
    public int delta;
    public int height;
    public int ratio;
    public int width;

    ScreenSize(int paramInt1, int paramInt2)
    {
      this.width = paramInt1;
      this.height = paramInt2;
      int i = ViewUtils.getScreenWidthPixels(SplashManager.context);
      int j = ViewUtils.getScreenHeightPixels(SplashManager.context);
      this.ratio = Math.abs(paramInt1 * j - paramInt2 * i);
      this.delta = (Math.abs(paramInt1 - i) + Math.abs(paramInt2 - j));
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.guide.SplashManager
 * JD-Core Version:    0.6.0
 */