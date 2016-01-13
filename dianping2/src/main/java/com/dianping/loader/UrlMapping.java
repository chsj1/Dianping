package com.dianping.loader;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Parcelable;
import android.text.TextUtils;
import com.dianping.app.Environment;
import com.dianping.loader.model.FileSpec;
import com.dianping.loader.model.FragmentSpec;
import com.dianping.loader.model.SiteSpec;
import java.util.List;
import java.util.Locale;

public class UrlMapping
{
  protected Context context;
  protected SiteManager siteManager;

  public UrlMapping(Context paramContext, SiteManager paramSiteManager)
  {
    this.context = paramContext;
    this.siteManager = paramSiteManager;
  }

  public Intent urlMap(Intent paramIntent)
  {
    if ((paramIntent.getComponent() != null) || (paramIntent.getBooleanExtra("noUrlMapping", false)));
    do
    {
      Object localObject3;
      Object localObject2;
      Object localObject1;
      do
      {
        do
        {
          do
          {
            do
            {
              return paramIntent;
              localObject3 = paramIntent.getData();
            }
            while ((localObject3 == null) || (((Uri)localObject3).getScheme() == null) || ((!"dianping".equalsIgnoreCase(((Uri)localObject3).getScheme())) && (!"dpinner".equalsIgnoreCase(((Uri)localObject3).getScheme()))));
            localObject2 = (SiteSpec)paramIntent.getParcelableExtra("_site");
            localObject1 = localObject2;
            if (localObject2 == null)
            {
              localObject1 = this.siteManager.site();
              paramIntent.putExtra("_site", (Parcelable)localObject1);
            }
            localObject2 = ((Uri)localObject3).getHost();
          }
          while (TextUtils.isEmpty((CharSequence)localObject2));
          localObject2 = ((SiteSpec)localObject1).getFragment(((String)localObject2).toLowerCase(Locale.US), Environment.versionCode());
        }
        while (localObject2 == null);
        paramIntent.putExtra("_fragment", ((FragmentSpec)localObject2).name());
        paramIntent.putExtra("_parameter", ((FragmentSpec)localObject2).parameter());
        if (TextUtils.isEmpty(((FragmentSpec)localObject2).code()))
        {
          paramIntent.setClass(this.context, MainActivity.class);
          return paramIntent;
        }
        localObject3 = ((SiteSpec)localObject1).getFile(((FragmentSpec)localObject2).code());
      }
      while (localObject3 == null);
      if (MyClassLoader.getClassLoader((SiteSpec)localObject1, (FileSpec)localObject3) != null)
      {
        paramIntent.putExtra("_code", ((FragmentSpec)localObject2).code());
        paramIntent.setClass(this.context, MainActivity.class);
        return paramIntent;
      }
      int j = 0;
      i = j;
      switch (((FragmentSpec)localObject2).skip())
      {
      default:
        i = j;
      case 0:
      case 1:
      case 2:
      }
    }
    while ((i != 0) && (this.context.getPackageManager().queryIntentActivities(paramIntent, 0).size() > 0));
    paramIntent.setClass(this.context, LoaderActivity.class);
    return paramIntent;
    if (this.siteManager.repositoryManager().getNetworkType() == 1);
    for (int i = 1; ; i = 0)
      break;
    i = this.siteManager.repositoryManager().getNetworkType();
    if ((i == 1) || (i == 2));
    for (i = 1; ; i = 0)
      break;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.loader.UrlMapping
 * JD-Core Version:    0.6.0
 */