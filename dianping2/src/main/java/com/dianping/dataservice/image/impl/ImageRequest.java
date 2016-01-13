package com.dianping.dataservice.image.impl;

import com.dianping.dataservice.http.BasicHttpRequest;

public class ImageRequest extends BasicHttpRequest
{
  public static final int TYPE_PHOTO = 2;
  public static final int TYPE_THUMBNAIL = 1;
  public static final int TYPE_UNKNOWN = 0;
  private boolean cacheOnly;
  private boolean disableStatistics;
  private String mModule;
  private int type;

  public ImageRequest(String paramString, int paramInt)
  {
    this(paramString, paramInt, false, false);
  }

  public ImageRequest(String paramString, int paramInt, boolean paramBoolean)
  {
    this(paramString, paramInt, paramBoolean, false);
  }

  public ImageRequest(String paramString, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    super(paramString, "GET", null);
    this.type = paramInt;
    this.cacheOnly = paramBoolean1;
    this.disableStatistics = paramBoolean2;
  }

  public boolean cacheOnly()
  {
    return this.cacheOnly;
  }

  public boolean disableStatistics()
  {
    return this.disableStatistics;
  }

  public String imageModule()
  {
    return this.mModule;
  }

  public void setImageModule(String paramString)
  {
    this.mModule = paramString;
  }

  public int type()
  {
    return this.type;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.image.impl.ImageRequest
 * JD-Core Version:    0.6.0
 */