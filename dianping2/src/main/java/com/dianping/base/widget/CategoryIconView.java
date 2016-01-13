package com.dianping.base.widget;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.AttributeSet;
import com.dianping.util.Log;
import java.io.IOException;
import java.io.InputStream;

public class CategoryIconView extends CachedNetworkThumbView
{
  public CategoryIconView(Context paramContext)
  {
    super(paramContext);
  }

  public CategoryIconView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public CategoryIconView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  protected InputStream getLocalCacheInputStream(String paramString)
  {
    try
    {
      InputStream localInputStream = this.mContext.getAssets().open("home_icons/" + paramString);
      return localInputStream;
    }
    catch (IOException localIOException)
    {
      Log.d("home_icons not exist:" + paramString);
    }
    return null;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.CategoryIconView
 * JD-Core Version:    0.6.0
 */