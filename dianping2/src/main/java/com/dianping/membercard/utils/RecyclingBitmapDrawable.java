package com.dianping.membercard.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import com.dianping.util.Log;

public class RecyclingBitmapDrawable extends BitmapDrawable
{
  static final String LOG_TAG = "CountingBitmapDrawable";
  private int mCacheRefCount = 0;
  private int mDisplayRefCount = 0;
  private boolean mHasBeenDisplayed;

  public RecyclingBitmapDrawable(Resources paramResources, Bitmap paramBitmap)
  {
    super(paramResources, paramBitmap);
  }

  private void checkState()
  {
    monitorenter;
    try
    {
      if ((this.mCacheRefCount <= 0) && (this.mDisplayRefCount <= 0) && (this.mHasBeenDisplayed) && (hasValidBitmap()))
      {
        Log.d("CountingBitmapDrawable", "No longer being used or cached so recycling. " + getBitmap().toString());
        getBitmap().recycle();
      }
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }

  private boolean hasValidBitmap()
  {
    monitorenter;
    try
    {
      Bitmap localBitmap = getBitmap();
      if (localBitmap != null)
      {
        bool = localBitmap.isRecycled();
        if (bool);
      }
      for (boolean bool = true; ; bool = false)
        return bool;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public void setIsCached(boolean paramBoolean)
  {
    monitorenter;
    if (paramBoolean);
    try
    {
      this.mCacheRefCount += 1;
      while (true)
      {
        monitorexit;
        checkState();
        return;
        this.mCacheRefCount -= 1;
      }
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public void setIsDisplayed(boolean paramBoolean)
  {
    monitorenter;
    if (paramBoolean);
    try
    {
      this.mDisplayRefCount += 1;
      this.mHasBeenDisplayed = true;
      while (true)
      {
        monitorexit;
        checkState();
        return;
        this.mDisplayRefCount -= 1;
      }
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.utils.RecyclingBitmapDrawable
 * JD-Core Version:    0.6.0
 */