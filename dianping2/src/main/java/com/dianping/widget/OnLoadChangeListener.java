package com.dianping.widget;

import android.graphics.Bitmap;

public abstract interface OnLoadChangeListener
{
  public abstract void onImageLoadFailed();

  public abstract void onImageLoadStart();

  public abstract void onImageLoadSuccess(Bitmap paramBitmap);
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.OnLoadChangeListener
 * JD-Core Version:    0.6.0
 */