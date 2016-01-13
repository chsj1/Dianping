package com.dianping.base.update;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.widget.Toast;
import com.dianping.app.DPApplication;
import com.dianping.base.update.utils.SHA1Utils;
import com.dianping.base.update.utils.download.DownloadManagerCompat;
import com.dianping.base.update.utils.download.DownloadManagerCompat.QueryCompat;
import com.dianping.configservice.ConfigService;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.util.DateUtil;
import com.dianping.util.Log;
import java.io.File;

public class UpdateManager
{
  private static final boolean DEBUG = true;
  private static int DOWNLOAD_LOCAL_URI_COLUMN_INDEX = 0;
  private static int DOWNLOAD_STATUS_COLUMN_INDEX = 0;
  public static final String FILENAME_PREFIX = "dianping_";
  public static final int FILETYPE_APK = 0;
  public static final int FILETYPE_PATCH = 1;
  public static final int INVALID_DOWNLOADID = -1;
  private static final String TAG = "UpdateManager";
  private static UpdateManager mUpdateManager;
  private static String packageName;
  private static int versionCode = 0;
  private Context appContext;
  private int downloadFileType = 0;
  private DownloadManagerCompat downloadManager;
  private ConfigService mConfigService;
  private SharedPreferences mDownloadPreference;

  private UpdateManager(Context paramContext)
  {
    this.appContext = paramContext.getApplicationContext();
    this.mDownloadPreference = this.appContext.getSharedPreferences("app_update", 0);
    this.mConfigService = ((ConfigService)DPApplication.instance().getService("config"));
    this.downloadManager = DownloadManagerCompat.createInstance(this.appContext);
    packageName = this.appContext.getPackageName();
    try
    {
      versionCode = this.appContext.getPackageManager().getPackageInfo(packageName, 0).versionCode;
      return;
    }
    catch (PackageManager.NameNotFoundException paramContext)
    {
      paramContext.printStackTrace();
    }
  }

  private File downloadApkFile()
  {
    if (queryDownloadStatus(downloadId()) == 8)
    {
      File localFile = new File(this.downloadManager.getUriForDownloadedFile(downloadId()).getPath());
      if (localFile.exists())
        return localFile;
    }
    return null;
  }

  public static UpdateManager instance(Context paramContext)
  {
    if (mUpdateManager == null)
      mUpdateManager = new UpdateManager(paramContext);
    return mUpdateManager;
  }

  private int queryDownloadStatus(long paramLong)
  {
    int j = -1;
    if (paramLong == -1L)
      return -1;
    Object localObject2 = new DownloadManagerCompat.QueryCompat();
    ((DownloadManagerCompat.QueryCompat)localObject2).setFilterById(new long[] { paramLong });
    Object localObject1 = null;
    try
    {
      localObject2 = this.downloadManager.query((DownloadManagerCompat.QueryCompat)localObject2);
      int i = j;
      if (localObject2 != null)
      {
        i = j;
        localObject1 = localObject2;
        if (((Cursor)localObject2).moveToFirst())
        {
          localObject1 = localObject2;
          if (DOWNLOAD_STATUS_COLUMN_INDEX != 0)
          {
            localObject1 = localObject2;
            if (DOWNLOAD_LOCAL_URI_COLUMN_INDEX != 0);
          }
          else
          {
            localObject1 = localObject2;
            DOWNLOAD_LOCAL_URI_COLUMN_INDEX = ((Cursor)localObject2).getColumnIndex("local_uri");
            localObject1 = localObject2;
            DOWNLOAD_STATUS_COLUMN_INDEX = ((Cursor)localObject2).getColumnIndex("status");
          }
          localObject1 = localObject2;
          j = ((Cursor)localObject2).getInt(DOWNLOAD_STATUS_COLUMN_INDEX);
          i = j;
          if (j == 8)
          {
            localObject1 = localObject2;
            boolean bool = new File(Uri.parse(((Cursor)localObject2).getString(DOWNLOAD_LOCAL_URI_COLUMN_INDEX)).getPath()).exists();
            i = j;
            if (!bool)
              i = 16;
          }
        }
      }
      if (localObject2 != null)
        ((Cursor)localObject2).close();
      return i;
    }
    finally
    {
      if (localObject1 != null)
        localObject1.close();
    }
    throw localObject3;
  }

  public boolean canSmartUpdate()
  {
    return ConfigHelper.cansmart;
  }

  public void cancelDownload()
  {
    Intent localIntent = new Intent(this.appContext, UpdateService.class);
    localIntent.setAction("com.dianping.app.SystemUpgradeService.CANCEL");
    this.appContext.startService(localIntent);
  }

  public boolean checkNewVersion()
  {
    Log.d("UpdateManager", "versionCode=" + ConfigHelper.versionCode + " expiredVersionCode=" + ConfigHelper.expiredVersionCode + " current version=" + versionCode);
    if ("om_dt_xtqg".equals(com.dianping.app.Environment.source()))
      if (this.mDownloadPreference.contains("firstRunTime"))
        if (DateUtil.currentTimeMillis() - this.mDownloadPreference.getLong("firstRunTime", 0L) >= 604800000L)
          break label128;
    while (true)
    {
      return false;
      this.mDownloadPreference.edit().putLong("firstRunTime", DateUtil.currentTimeMillis()).commit();
      return false;
      label128: if (ConfigHelper.versionCode > this.mDownloadPreference.getInt("versionCode", 0))
        reset();
      if ((ConfigHelper.expiredVersionCode >= versionCode) || (ConfigHelper.versionCode > versionCode) || ((ConfigHelper.versionCode > versionCode) && (ConfigHelper.forceUpdate)))
        return true;
      if (Build.VERSION.SDK_INT >= 8);
      Object localObject2;
      for (Object localObject1 = android.os.Environment.getExternalStoragePublicDirectory("Download"); ((File)localObject1).exists(); localObject1 = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/Download"))
      {
        localObject1 = ((File)localObject1).listFiles();
        if (localObject1 == null)
          break;
        j = localObject1.length;
        i = 0;
        while (i < j)
        {
          localObject2 = localObject1[i];
          if ((localObject2.getPath().contains("dianping_")) && (localObject2.getPath().endsWith(".apk")))
            localObject2.delete();
          i += 1;
        }
      }
      localObject1 = this.appContext.getFilesDir();
      if (!((File)localObject1).exists())
        continue;
      localObject1 = ((File)localObject1).listFiles();
      if (localObject1 == null)
        continue;
      int j = localObject1.length;
      int i = 0;
      while (i < j)
      {
        localObject2 = localObject1[i];
        if ((localObject2.getPath().contains("dianping_")) && (localObject2.getPath().endsWith(".apk")))
          localObject2.delete();
        i += 1;
      }
    }
  }

  public String destinationPath()
  {
    return this.mDownloadPreference.getString("destinationPath", "");
  }

  public long downloadId()
  {
    return this.mDownloadPreference.getLong("downloadID", -1L);
  }

  public DownloadManagerCompat getDownloadManager()
  {
    return this.downloadManager;
  }

  public String getLocalApkPath()
  {
    try
    {
      String str = this.appContext.getPackageManager().getApplicationInfo(packageName, 8192).sourceDir;
      return str;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      localNameNotFoundException.printStackTrace();
    }
    return "";
  }

  public String getNewApkSha1()
  {
    if (this.downloadFileType == 1)
      return ConfigHelper.smartSha1;
    return ConfigHelper.downloadFileSha1;
  }

  public String getOldApkSha1()
  {
    return ConfigHelper.oldApkSha1;
  }

  public String getPatchSha1()
  {
    return ConfigHelper.smartDownloadFileSha1;
  }

  public void gotoInstall()
  {
    long l = downloadId();
    if (l != -1L)
    {
      Uri localUri = this.downloadManager.getUriForDownloadedFile(l);
      if (localUri != null)
      {
        Intent localIntent = new Intent("android.intent.action.VIEW");
        localIntent.addFlags(268435456);
        localIntent.setDataAndType(localUri, "application/vnd.android.package-archive");
        this.appContext.startActivity(localIntent);
      }
    }
    else
    {
      return;
    }
    Toast.makeText(this.appContext, "无效路径", 0).show();
  }

  public boolean isExpired()
  {
    return ConfigHelper.expiredVersionCode >= versionCode();
  }

  public boolean isUpdateApkFromAutoWifi()
  {
    return this.mDownloadPreference.getBoolean("autowifi", false);
  }

  public boolean isUpdateApkOK()
  {
    return downloadApkFile() != null;
  }

  public int newVersionCode()
  {
    return ConfigHelper.versionCode;
  }

  public void reset()
  {
    long l = downloadId();
    if (l != -1L)
      this.downloadManager.remove(new long[] { l });
    this.mDownloadPreference.edit().clear().commit();
  }

  public void setUpdateApkFromAutoWifi(boolean paramBoolean)
  {
    this.mDownloadPreference.edit().putBoolean("autowifi", paramBoolean).commit();
  }

  public void startDownload(String paramString, int paramInt1, int paramInt2)
  {
    Log.d("UpdateManager", "start to download. url=" + paramString + " type=" + paramInt1);
    if (TextUtils.isEmpty(paramString))
    {
      Log.w("UpdateManager", "download url is empty");
      return;
    }
    if ((isUpdateApkOK()) && (UpdateService.bStopped))
    {
      gotoInstall();
      return;
    }
    this.downloadFileType = paramInt1;
    Intent localIntent = new Intent(this.appContext, UpdateService.class);
    localIntent.setAction("com.dianping.app.SystemUpgradeService.STRAT");
    localIntent.putExtra("url", paramString);
    localIntent.putExtra("type", paramInt1);
    localIntent.putExtra("source", paramInt2);
    this.appContext.startService(localIntent);
  }

  public void startSilentDownload()
  {
    Log.d("UpdateManager", "start silent download");
    if (!isUpdateApkOK())
    {
      boolean bool = canSmartUpdate();
      String str = getOldApkSha1();
      File localFile = new File(getLocalApkPath());
      if ((bool) && (SHA1Utils.testSHA1(localFile, str)))
        startDownload(ConfigHelper.smarturl, 1, 1);
    }
    else
    {
      return;
    }
    startDownload(ConfigHelper.url, 0, 1);
  }

  public int versionCode()
  {
    return versionCode;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.update.UpdateManager
 * JD-Core Version:    0.6.0
 */