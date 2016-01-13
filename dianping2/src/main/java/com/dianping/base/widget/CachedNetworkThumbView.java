package com.dianping.base.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.AttributeSet;
import com.dianping.cache.DPCache;
import com.dianping.util.BitmapUtils;
import com.dianping.util.ViewUtils;
import com.dianping.widget.OnLoadChangeListener;
import java.io.IOException;
import java.io.InputStream;

public class CachedNetworkThumbView extends NetworkThumbView
{
  protected Context mContext;

  public CachedNetworkThumbView(Context paramContext)
  {
    super(paramContext);
    this.mContext = paramContext;
  }

  public CachedNetworkThumbView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mContext = paramContext;
  }

  public CachedNetworkThumbView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    this.mContext = paramContext;
  }

  protected InputStream getLocalCacheInputStream(String paramString)
  {
    return null;
  }

  public void setImage(String paramString)
  {
    String str = paramString;
    if (paramString != null)
    {
      str = paramString;
      if (paramString.length() == 0)
        str = null;
    }
    if ((str == null) && (this.url == null));
    int i;
    do
    {
      do
        return;
      while ((str != null) && (str.equals(this.url)));
      i = str.lastIndexOf("/");
    }
    while (i < 0);
    paramString = str.substring(i + 1);
    Object localObject = getLocalCacheInputStream(paramString);
    if (localObject != null)
    {
      setLocalBitmap(BitmapUtils.decodeSampledBitmapFromStream(Bitmap.Config.RGB_565, (InputStream)localObject, ViewUtils.getScreenWidthPixels(this.mContext), ViewUtils.getScreenHeightPixels(this.mContext)));
      try
      {
        ((InputStream)localObject).close();
        return;
      }
      catch (IOException paramString)
      {
        paramString.printStackTrace();
        return;
      }
    }
    localObject = DPCache.getInstance().getBitmap(paramString, null, 31539600000L);
    if (localObject != null)
    {
      setLocalBitmap((Bitmap)localObject);
      return;
    }
    setLoadChangeListener(new OnLoadChangeListener(paramString)
    {
      public void onImageLoadFailed()
      {
      }

      public void onImageLoadStart()
      {
      }

      public void onImageLoadSuccess(Bitmap paramBitmap)
      {
        DPCache.getInstance().put(this.val$icon, null, paramBitmap, 31539600000L);
      }
    });
    super.setImage(str);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.CachedNetworkThumbView
 * JD-Core Version:    0.6.0
 */