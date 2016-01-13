package com.dianping.base.share.util;

import android.content.Intent;

public class QQAio
{
  private static QQAio sInstance;
  private Intent mQQIntent;

  public static QQAio getInstance()
  {
    if (sInstance == null)
      sInstance = new QQAio();
    return sInstance;
  }

  public Intent getQQIntent()
  {
    return this.mQQIntent;
  }

  public void setQQIntent(Intent paramIntent)
  {
    this.mQQIntent = paramIntent;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.share.util.QQAio
 * JD-Core Version:    0.6.0
 */