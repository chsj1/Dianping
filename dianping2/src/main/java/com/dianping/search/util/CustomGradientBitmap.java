package com.dianping.search.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

public class CustomGradientBitmap
{
  public static Bitmap drawableToBitmap(Drawable paramDrawable)
  {
    int i = paramDrawable.getIntrinsicWidth();
    int j = paramDrawable.getIntrinsicHeight();
    if (paramDrawable.getOpacity() != -1);
    for (Object localObject = Bitmap.Config.ARGB_8888; ; localObject = Bitmap.Config.RGB_565)
    {
      localObject = Bitmap.createBitmap(i, j, (Bitmap.Config)localObject);
      Canvas localCanvas = new Canvas((Bitmap)localObject);
      paramDrawable.setBounds(0, 0, i, j);
      paramDrawable.draw(localCanvas);
      return localObject;
    }
  }

  public static int getGradientMainColor(Bitmap paramBitmap)
  {
    if (paramBitmap == null)
      return 0;
    long l3 = 0L;
    long l2 = 0L;
    long l1 = 0L;
    int k = paramBitmap.getHeight();
    int m = paramBitmap.getWidth();
    long l4 = k * m;
    int i = 0;
    while (i < k)
    {
      int j = 0;
      while (j < m)
      {
        int n = paramBitmap.getPixel(j, i);
        l3 += Color.red(n);
        l2 += Color.green(n);
        l1 += Color.blue(n);
        j += 1;
      }
      i += 1;
    }
    paramBitmap = new float[3];
    Color.RGBToHSV((int)(l3 / l4), (int)(l2 / l4), (int)(l1 / l4), paramBitmap);
    paramBitmap[1] = ((paramBitmap[1] - 0.55F) * 0.2F + 0.55F);
    paramBitmap[2] = ((paramBitmap[2] - 0.35F) * 0.2F + 0.35F);
    return Color.HSVToColor(paramBitmap);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.util.CustomGradientBitmap
 * JD-Core Version:    0.6.0
 */