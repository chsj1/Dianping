package com.dianping.zeus.utils;

import android.content.Context;
import android.util.Log;

public class ZeusGaWrapper
{
  private static final String TAG = "ZeusGaWrapper";
  private static IZeusGaHelper mGaHelper;

  public static void ga(Context paramContext, String paramString1, String paramString2, int paramInt, String paramString3)
  {
    if (mGaHelper != null)
    {
      mGaHelper.ga(paramContext, paramString1, paramString2, paramInt, paramString3);
      Log.d("ZeusGaWrapper", "element_id =" + paramString1 + ",title=" + paramString2 + ",index=" + paramInt + ",action =" + paramString3);
    }
  }

  public static IZeusGaHelper getZeusGaHelper()
  {
    return mGaHelper;
  }

  public static void pv(long paramLong, String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    if (mGaHelper != null)
    {
      mGaHelper.pv(paramLong, paramString, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
      Log.d("ZeusGaWrapper", "time =" + paramLong + ",command =" + paramString + ",network =" + paramInt1 + ",tunnel =" + paramInt2 + ",code =" + paramInt3 + ",requestBytes =" + paramInt4 + ",responseBytes =" + paramInt5 + ",responseTime =" + paramInt6);
    }
  }

  public static void setZeusGaHelper(IZeusGaHelper paramIZeusGaHelper)
  {
    mGaHelper = paramIZeusGaHelper;
  }

  public static abstract interface IZeusGaHelper
  {
    public abstract void ga(Context paramContext, String paramString1, String paramString2, int paramInt, String paramString3);

    public abstract void pv(long paramLong, String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.zeus.utils.ZeusGaWrapper
 * JD-Core Version:    0.6.0
 */