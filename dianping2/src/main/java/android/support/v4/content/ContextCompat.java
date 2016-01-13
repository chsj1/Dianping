package android.support.v4.content;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.support.annotation.NonNull;
import android.util.Log;
import java.io.File;

public class ContextCompat
{
  private static final String DIR_ANDROID = "Android";
  private static final String DIR_CACHE = "cache";
  private static final String DIR_DATA = "data";
  private static final String DIR_FILES = "files";
  private static final String DIR_OBB = "obb";
  private static final String TAG = "ContextCompat";

  private static File buildPath(File paramFile, String[] paramArrayOfString)
  {
    int j = paramArrayOfString.length;
    int i = 0;
    String str;
    if (i < j)
    {
      str = paramArrayOfString[i];
      if (paramFile == null)
        paramFile = new File(str);
    }
    while (true)
    {
      i += 1;
      break;
      if (str != null)
      {
        paramFile = new File(paramFile, str);
        continue;
        return paramFile;
      }
    }
  }

  public static int checkSelfPermission(@NonNull Context paramContext, @NonNull String paramString)
  {
    if (paramString == null)
      throw new IllegalArgumentException("permission is null");
    return paramContext.checkPermission(paramString, Process.myPid(), Process.myUid());
  }

  private static File createFilesDir(File paramFile)
  {
    monitorenter;
    File localFile = paramFile;
    try
    {
      if (!paramFile.exists())
      {
        localFile = paramFile;
        if (!paramFile.mkdirs())
        {
          boolean bool = paramFile.exists();
          if (!bool)
            break label37;
        }
      }
      for (localFile = paramFile; ; localFile = null)
      {
        return localFile;
        label37: Log.w("ContextCompat", "Unable to create files subdir " + paramFile.getPath());
      }
    }
    finally
    {
      monitorexit;
    }
    throw paramFile;
  }

  public static final int getColor(Context paramContext, int paramInt)
  {
    if (Build.VERSION.SDK_INT >= 23)
      return ContextCompatApi23.getColor(paramContext, paramInt);
    return paramContext.getResources().getColor(paramInt);
  }

  public static final ColorStateList getColorStateList(Context paramContext, int paramInt)
  {
    if (Build.VERSION.SDK_INT >= 23)
      return ContextCompatApi23.getColorStateList(paramContext, paramInt);
    return paramContext.getResources().getColorStateList(paramInt);
  }

  public static final Drawable getDrawable(Context paramContext, int paramInt)
  {
    if (Build.VERSION.SDK_INT >= 21)
      return ContextCompatApi21.getDrawable(paramContext, paramInt);
    return paramContext.getResources().getDrawable(paramInt);
  }

  public static File[] getExternalCacheDirs(Context paramContext)
  {
    int i = Build.VERSION.SDK_INT;
    if (i >= 19)
      return ContextCompatKitKat.getExternalCacheDirs(paramContext);
    if (i >= 8);
    for (paramContext = ContextCompatFroyo.getExternalCacheDir(paramContext); ; paramContext = buildPath(Environment.getExternalStorageDirectory(), new String[] { "Android", "data", paramContext.getPackageName(), "cache" }))
      return new File[] { paramContext };
  }

  public static File[] getExternalFilesDirs(Context paramContext, String paramString)
  {
    int i = Build.VERSION.SDK_INT;
    if (i >= 19)
      return ContextCompatKitKat.getExternalFilesDirs(paramContext, paramString);
    if (i >= 8);
    for (paramContext = ContextCompatFroyo.getExternalFilesDir(paramContext, paramString); ; paramContext = buildPath(Environment.getExternalStorageDirectory(), new String[] { "Android", "data", paramContext.getPackageName(), "files", paramString }))
      return new File[] { paramContext };
  }

  public static File[] getObbDirs(Context paramContext)
  {
    int i = Build.VERSION.SDK_INT;
    if (i >= 19)
      return ContextCompatKitKat.getObbDirs(paramContext);
    if (i >= 11);
    for (paramContext = ContextCompatHoneycomb.getObbDir(paramContext); ; paramContext = buildPath(Environment.getExternalStorageDirectory(), new String[] { "Android", "obb", paramContext.getPackageName() }))
      return new File[] { paramContext };
  }

  public static boolean startActivities(Context paramContext, Intent[] paramArrayOfIntent)
  {
    return startActivities(paramContext, paramArrayOfIntent, null);
  }

  public static boolean startActivities(Context paramContext, Intent[] paramArrayOfIntent, Bundle paramBundle)
  {
    int i = Build.VERSION.SDK_INT;
    if (i >= 16)
    {
      ContextCompatJellybean.startActivities(paramContext, paramArrayOfIntent, paramBundle);
      return true;
    }
    if (i >= 11)
    {
      ContextCompatHoneycomb.startActivities(paramContext, paramArrayOfIntent);
      return true;
    }
    return false;
  }

  public final File getCodeCacheDir(Context paramContext)
  {
    if (Build.VERSION.SDK_INT >= 21)
      return ContextCompatApi21.getCodeCacheDir(paramContext);
    return createFilesDir(new File(paramContext.getApplicationInfo().dataDir, "code_cache"));
  }

  public final File getNoBackupFilesDir(Context paramContext)
  {
    if (Build.VERSION.SDK_INT >= 21)
      return ContextCompatApi21.getNoBackupFilesDir(paramContext);
    return createFilesDir(new File(paramContext.getApplicationInfo().dataDir, "no_backup"));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     android.support.v4.content.ContextCompat
 * JD-Core Version:    0.6.0
 */