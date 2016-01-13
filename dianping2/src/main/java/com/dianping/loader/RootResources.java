package com.dianping.loader;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.loader.model.FileSpec;

class RootResources extends MyResources
{
  RootResources(FileSpec paramFileSpec, String paramString, Resources paramResources, AssetManager paramAssetManager, MyResources[] paramArrayOfMyResources)
  {
    super(paramFileSpec, paramString, paramResources, paramAssetManager, paramArrayOfMyResources);
  }

  public View inflate(Context paramContext, int paramInt, ViewGroup paramViewGroup, boolean paramBoolean)
  {
    return LayoutInflater.from(paramContext).inflate(paramInt, paramViewGroup, paramBoolean);
  }

  public View inflate(Context paramContext, String paramString, ViewGroup paramViewGroup, boolean paramBoolean)
  {
    int i = this.res.getIdentifier(paramString, "layout", this.packageName);
    if (i == 0)
      throw new Resources.NotFoundException("@layout/" + paramString + " not found in root package");
    return LayoutInflater.from(paramContext).inflate(i, paramViewGroup, paramBoolean);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.loader.RootResources
 * JD-Core Version:    0.6.0
 */