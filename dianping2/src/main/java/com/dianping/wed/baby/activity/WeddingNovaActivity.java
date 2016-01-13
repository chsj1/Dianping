package com.dianping.wed.baby.activity;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import com.dianping.base.app.NovaActivity;
import com.dianping.loader.MyResources;
import com.dianping.loader.MyResources.ResourceOverrideable;

public class WeddingNovaActivity extends NovaActivity
  implements MyResources.ResourceOverrideable
{
  AssetManager assetManager;
  MyResources myResources;
  Resources resources;
  Resources.Theme theme;

  public AssetManager getAssets()
  {
    if (this.assetManager == null)
      return super.getAssets();
    return this.assetManager;
  }

  public MyResources getOverrideResources()
  {
    return this.myResources;
  }

  public Resources getResources()
  {
    if (this.resources == null)
      return super.getResources();
    return this.resources;
  }

  public Resources.Theme getTheme()
  {
    if (this.theme == null)
      return super.getTheme();
    return this.theme;
  }

  public void setOverrideResources(MyResources paramMyResources)
  {
    if (paramMyResources == null)
    {
      this.myResources = null;
      this.resources = null;
      this.assetManager = null;
      this.theme = null;
      return;
    }
    this.myResources = paramMyResources;
    this.resources = paramMyResources.getResources();
    this.assetManager = paramMyResources.getAssets();
    paramMyResources = paramMyResources.getResources().newTheme();
    paramMyResources.setTo(getTheme());
    this.theme = paramMyResources;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.baby.activity.WeddingNovaActivity
 * JD-Core Version:    0.6.0
 */