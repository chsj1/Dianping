package com.tencent.base;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import cloudwns.l.c;
import com.tencent.base.util.ProcessUtils;
import com.tencent.wns.client.inte.WnsService.GlobalListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public final class Global
{
  private static boolean a = false;
  private static Context b;
  private static boolean c = false;
  private static WnsService.GlobalListener d;

  public static final boolean bindService(Intent paramIntent, ServiceConnection paramServiceConnection, int paramInt)
  {
    return getContext().bindService(paramIntent, paramServiceConnection, paramInt);
  }

  public static final int checkCallingOrSelfPermission(String paramString)
  {
    return getContext().checkCallingOrSelfPermission(paramString);
  }

  public static final int checkCallingOrSelfUriPermission(Uri paramUri, int paramInt)
  {
    return getContext().checkCallingOrSelfUriPermission(paramUri, paramInt);
  }

  public static final int checkCallingPermission(String paramString)
  {
    return getContext().checkCallingPermission(paramString);
  }

  public static final int checkCallingUriPermission(Uri paramUri, int paramInt)
  {
    return getContext().checkCallingUriPermission(paramUri, paramInt);
  }

  public static final int checkPermission(String paramString, int paramInt1, int paramInt2)
  {
    return getContext().checkPermission(paramString, paramInt1, paramInt2);
  }

  public static final int checkUriPermission(Uri paramUri, int paramInt1, int paramInt2, int paramInt3)
  {
    return getContext().checkUriPermission(paramUri, paramInt1, paramInt2, paramInt3);
  }

  public static final int checkUriPermission(Uri paramUri, String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3)
  {
    return getContext().checkUriPermission(paramUri, paramString1, paramString2, paramInt1, paramInt2, paramInt3);
  }

  @Deprecated
  public static final void clearWallpaper()
  {
    getContext().clearWallpaper();
  }

  public static final Context createPackageContext(String paramString, int paramInt)
  {
    return getContext().createPackageContext(paramString, paramInt);
  }

  public static final String currentProcessName()
  {
    return ProcessUtils.a(b);
  }

  public static final String[] databaseList()
  {
    return getContext().databaseList();
  }

  public static final boolean deleteDatabase(String paramString)
  {
    return getContext().deleteDatabase(paramString);
  }

  public static final boolean deleteFile(String paramString)
  {
    return getContext().deleteFile(paramString);
  }

  public static final void enforceCallingOrSelfPermission(String paramString1, String paramString2)
  {
    getContext().enforceCallingOrSelfPermission(paramString1, paramString2);
  }

  public static final void enforceCallingOrSelfUriPermission(Uri paramUri, int paramInt, String paramString)
  {
    getContext().enforceCallingOrSelfUriPermission(paramUri, paramInt, paramString);
  }

  public static final void enforceCallingPermission(String paramString1, String paramString2)
  {
    getContext().enforceCallingPermission(paramString1, paramString2);
  }

  public static final void enforceCallingUriPermission(Uri paramUri, int paramInt, String paramString)
  {
    getContext().enforceCallingUriPermission(paramUri, paramInt, paramString);
  }

  public static final void enforcePermission(String paramString1, int paramInt1, int paramInt2, String paramString2)
  {
    getContext().enforcePermission(paramString1, paramInt1, paramInt2, paramString2);
  }

  public static final void enforceUriPermission(Uri paramUri, int paramInt1, int paramInt2, int paramInt3, String paramString)
  {
    getContext().enforceUriPermission(paramUri, paramInt1, paramInt2, paramInt3, paramString);
  }

  public static final void enforceUriPermission(Uri paramUri, String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3, String paramString3)
  {
    getContext().enforceUriPermission(paramUri, paramString1, paramString2, paramInt1, paramInt2, paramInt3, paramString3);
  }

  public static final String[] fileList()
  {
    return getContext().fileList();
  }

  public static final Context getApplicationContext()
  {
    return getContext().getApplicationContext();
  }

  public static final ApplicationInfo getApplicationInfo()
  {
    return getContext().getApplicationInfo();
  }

  public static final AssetManager getAssets()
  {
    return getContext().getAssets();
  }

  public static final File getCacheDir()
  {
    return getContext().getCacheDir();
  }

  public static final ClassLoader getClassLoader()
  {
    return getContext().getClassLoader();
  }

  public static final ContentResolver getContentResolver()
  {
    return getContext().getContentResolver();
  }

  public static final Context getContext()
  {
    if (b == null)
      throw new a("Global's Context is NULL, have your Application in manifest subclasses BaseApplication or Call 'Global.init(this)' in your own Application ? ");
    return b;
  }

  public static final File getDatabasePath(String paramString)
  {
    return getContext().getDatabasePath(paramString);
  }

  public static final File getDir(String paramString, int paramInt)
  {
    return getContext().getDir(paramString, paramInt);
  }

  public static final File getExternalCacheDir()
  {
    return getContext().getExternalCacheDir();
  }

  public static final File getExternalFilesDir(String paramString)
  {
    return getContext().getExternalFilesDir(paramString);
  }

  public static final File getFileStreamPath(String paramString)
  {
    return getContext().getFileStreamPath(paramString);
  }

  public static final File getFilesDir()
  {
    return getContext().getFilesDir();
  }

  public static final WnsService.GlobalListener getGlobalListener()
  {
    return d;
  }

  public static final Looper getMainLooper()
  {
    return getContext().getMainLooper();
  }

  public static final String getPackageCodePath()
  {
    return getContext().getPackageCodePath();
  }

  public static final PackageManager getPackageManager()
  {
    return getContext().getPackageManager();
  }

  public static final String getPackageName()
  {
    return getContext().getPackageName();
  }

  public static final String getPackageResourcePath()
  {
    return getContext().getPackageResourcePath();
  }

  public static final Resources getResources()
  {
    return getContext().getResources();
  }

  public static final SharedPreferences getSharedPreferences(String paramString, int paramInt)
  {
    return getContext().getSharedPreferences(paramString, paramInt);
  }

  public static final SharedPreferences getSharedPreferencesForWns()
  {
    return getSharedPreferences("com.tencent.wns.data", 1);
  }

  public static final Object getSystemService(String paramString)
  {
    return getContext().getSystemService(paramString);
  }

  public static final Resources.Theme getTheme()
  {
    return getContext().getTheme();
  }

  @Deprecated
  public static final Drawable getWallpaper()
  {
    return getContext().getWallpaper();
  }

  @Deprecated
  public static final int getWallpaperDesiredMinimumHeight()
  {
    return getContext().getWallpaperDesiredMinimumHeight();
  }

  @Deprecated
  public static final int getWallpaperDesiredMinimumWidth()
  {
    return getContext().getWallpaperDesiredMinimumWidth();
  }

  public static final void grantUriPermission(String paramString, Uri paramUri, int paramInt)
  {
    getContext().grantUriPermission(paramString, paramUri, paramInt);
  }

  public static final void init(Application paramApplication)
  {
    init(paramApplication, null);
  }

  public static final void init(Application paramApplication, WnsService.GlobalListener paramGlobalListener)
  {
    a = true;
    setContext(paramApplication);
    paramApplication = paramGlobalListener;
    if (paramGlobalListener == null)
      paramApplication = new b();
    setGlobalListener(paramApplication);
  }

  public static final boolean isDebug()
  {
    return c;
  }

  public static boolean isInit()
  {
    return a;
  }

  public static final boolean isMainProcess()
  {
    String str = currentProcessName();
    if (str == null);
    do
      return false;
    while (str.indexOf(':') >= 1);
    return true;
  }

  public static final boolean isRestricted()
  {
    return getContext().isRestricted();
  }

  public static final FileInputStream openFileInput(String paramString)
  {
    return getContext().openFileInput(paramString);
  }

  public static final FileOutputStream openFileOutput(String paramString, int paramInt)
  {
    return getContext().openFileOutput(paramString, paramInt);
  }

  public static final SQLiteDatabase openOrCreateDatabase(String paramString, int paramInt, SQLiteDatabase.CursorFactory paramCursorFactory)
  {
    return getContext().openOrCreateDatabase(paramString, paramInt, paramCursorFactory);
  }

  @Deprecated
  public static final Drawable peekWallpaper()
  {
    return getContext().peekWallpaper();
  }

  public static final Intent registerReceiver(BroadcastReceiver paramBroadcastReceiver, IntentFilter paramIntentFilter)
  {
    return getContext().registerReceiver(paramBroadcastReceiver, paramIntentFilter);
  }

  public static final Intent registerReceiver(BroadcastReceiver paramBroadcastReceiver, IntentFilter paramIntentFilter, String paramString, Handler paramHandler)
  {
    return getContext().registerReceiver(paramBroadcastReceiver, paramIntentFilter, paramString, paramHandler);
  }

  public static final void removeStickyBroadcast(Intent paramIntent)
  {
    getContext().removeStickyBroadcast(paramIntent);
  }

  public static final void revokeUriPermission(Uri paramUri, int paramInt)
  {
    getContext().revokeUriPermission(paramUri, paramInt);
  }

  public static final void sendBroadcast(Intent paramIntent)
  {
    getContext().sendBroadcast(paramIntent);
  }

  public static final void sendBroadcast(Intent paramIntent, String paramString)
  {
    getContext().sendBroadcast(paramIntent, paramString);
  }

  public static final void sendOrderedBroadcast(Intent paramIntent, String paramString)
  {
    getContext().sendOrderedBroadcast(paramIntent, paramString);
  }

  public static final void sendOrderedBroadcast(Intent paramIntent, String paramString1, BroadcastReceiver paramBroadcastReceiver, Handler paramHandler, int paramInt, String paramString2, Bundle paramBundle)
  {
    getContext().sendOrderedBroadcast(paramIntent, paramString1, paramBroadcastReceiver, paramHandler, paramInt, paramString2, paramBundle);
  }

  public static final void sendStickyBroadcast(Intent paramIntent)
  {
    getContext().sendStickyBroadcast(paramIntent);
  }

  public static final void sendStickyOrderedBroadcast(Intent paramIntent, BroadcastReceiver paramBroadcastReceiver, Handler paramHandler, int paramInt, String paramString, Bundle paramBundle)
  {
    getContext().sendStickyOrderedBroadcast(paramIntent, paramBroadcastReceiver, paramHandler, paramInt, paramString, paramBundle);
  }

  public static final void setContext(Context paramContext)
  {
    b = paramContext;
    try
    {
      if ((paramContext.getApplicationInfo().flags & 0x2) != 0);
      for (boolean bool = true; ; bool = false)
      {
        c = bool;
        if (c)
          c.autoTrace(8, "Wns.Global.Runtime", "DEBUG is ON", null);
        return;
      }
    }
    catch (java.lang.Exception paramContext)
    {
      c = false;
    }
  }

  public static final void setGlobalListener(WnsService.GlobalListener paramGlobalListener)
  {
    d = paramGlobalListener;
  }

  public static final void setTheme(int paramInt)
  {
    getContext().setTheme(paramInt);
  }

  @Deprecated
  public static final void setWallpaper(Bitmap paramBitmap)
  {
    getContext().setWallpaper(paramBitmap);
  }

  @Deprecated
  public static final void setWallpaper(InputStream paramInputStream)
  {
    getContext().setWallpaper(paramInputStream);
  }

  public static final void startActivity(Intent paramIntent)
  {
    getContext().startActivity(paramIntent);
  }

  public static final boolean startInstrumentation(ComponentName paramComponentName, String paramString, Bundle paramBundle)
  {
    return getContext().startInstrumentation(paramComponentName, paramString, paramBundle);
  }

  public static final void startIntentSender(IntentSender paramIntentSender, Intent paramIntent, int paramInt1, int paramInt2, int paramInt3)
  {
    getContext().startIntentSender(paramIntentSender, paramIntent, paramInt1, paramInt2, paramInt3);
  }

  public static final ComponentName startService(Intent paramIntent)
  {
    return getContext().startService(paramIntent);
  }

  public static final boolean stopService(Intent paramIntent)
  {
    return getContext().stopService(paramIntent);
  }

  public static final void unbindService(ServiceConnection paramServiceConnection)
  {
    getContext().unbindService(paramServiceConnection);
  }

  public static final void unregisterReceiver(BroadcastReceiver paramBroadcastReceiver)
  {
    getContext().unregisterReceiver(paramBroadcastReceiver);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.base.Global
 * JD-Core Version:    0.6.0
 */