package com.dianping.main.guide;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.dianping.v1.R.string;
import java.util.Iterator;
import java.util.List;

public class ShortcutUtils
{
  public static void createShortcut(Context paramContext, int paramInt1, int paramInt2)
  {
    Intent localIntent1 = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
    localIntent1.putExtra("duplicate", false);
    localIntent1.putExtra("android.intent.extra.shortcut.NAME", paramContext.getString(paramInt2));
    localIntent1.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", Intent.ShortcutIconResource.fromContext(paramContext.getApplicationContext(), paramInt1));
    Intent localIntent2 = new Intent("android.intent.action.MAIN");
    localIntent2.addCategory("android.intent.category.LAUNCHER");
    localIntent2.addFlags(268435456);
    localIntent2.setComponent(new ComponentName(paramContext.getApplicationContext(), SplashScreenActivity.class));
    localIntent1.putExtra("android.intent.extra.shortcut.INTENT", localIntent2);
    paramContext.sendBroadcast(localIntent1);
  }

  private static String getAuthorityFromPermission(Context paramContext, String paramString)
  {
    if (TextUtils.isEmpty(paramString));
    ProviderInfo[] arrayOfProviderInfo;
    do
    {
      while (!paramContext.hasNext())
      {
        do
        {
          return null;
          paramContext = paramContext.getPackageManager().getInstalledPackages(8);
        }
        while (paramContext == null);
        paramContext = paramContext.iterator();
      }
      arrayOfProviderInfo = ((PackageInfo)paramContext.next()).providers;
    }
    while (arrayOfProviderInfo == null);
    int j = arrayOfProviderInfo.length;
    int i = 0;
    label63: ProviderInfo localProviderInfo;
    if (i < j)
    {
      localProviderInfo = arrayOfProviderInfo[i];
      if (localProviderInfo != null)
        break label88;
    }
    label88: 
    do
    {
      i += 1;
      break label63;
      break;
    }
    while ((!paramString.equals(localProviderInfo.readPermission)) && (!paramString.equals(localProviderInfo.writePermission)));
    return localProviderInfo.authority;
  }

  public static boolean hasShortcut(Context paramContext)
  {
    Object localObject1 = getAuthorityFromPermission(paramContext.getApplicationContext(), "com.android.launcher.permission.READ_SETTINGS");
    if (TextUtils.isEmpty((CharSequence)localObject1))
      return true;
    Object localObject4 = null;
    Object localObject3 = null;
    ContentResolver localContentResolver = paramContext.getContentResolver();
    Uri localUri = Uri.parse("content://" + (String)localObject1 + "/favorites?notify=true");
    localObject1 = localObject3;
    Object localObject2 = localObject4;
    try
    {
      paramContext = paramContext.getString(R.string.app_name);
      localObject1 = localObject3;
      localObject2 = localObject4;
      paramContext = localContentResolver.query(localUri, new String[] { "title" }, "title=?", new String[] { paramContext }, null);
      if (paramContext != null)
      {
        localObject1 = paramContext;
        localObject2 = paramContext;
        int i = paramContext.getCount();
        if (i > 0)
        {
          if (paramContext != null)
            paramContext.close();
          return true;
        }
      }
      if (paramContext != null)
        paramContext.close();
      return false;
    }
    catch (java.lang.Exception paramContext)
    {
      return true;
    }
    finally
    {
      if (localObject2 != null)
        ((Cursor)localObject2).close();
    }
    throw paramContext;
  }

  public static void removeShortcut(Context paramContext, int paramInt)
  {
    Intent localIntent1 = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
    localIntent1.putExtra("android.intent.extra.shortcut.NAME", paramContext.getString(paramInt));
    Intent localIntent2 = new Intent("android.intent.action.MAIN");
    localIntent2.addCategory("android.intent.category.LAUNCHER");
    localIntent2.setComponent(new ComponentName(paramContext.getApplicationContext(), SplashScreenActivity.class));
    localIntent1.putExtra("android.intent.extra.shortcut.INTENT", localIntent2);
    paramContext.sendBroadcast(localIntent1);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.guide.ShortcutUtils
 * JD-Core Version:    0.6.0
 */