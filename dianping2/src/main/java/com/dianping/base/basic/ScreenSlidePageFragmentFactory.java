package com.dianping.base.basic;

import android.graphics.Bitmap;
import android.os.Bundle;
import com.dianping.archive.DPObject;
import com.dianping.util.Log;
import java.lang.reflect.Constructor;

public class ScreenSlidePageFragmentFactory
{
  public static final String ARG_BITMAP = "bitmap";
  public static final String ARG_CURRENT = "current";
  public static final String ARG_PAGE = "page";

  public static ScreenSlidePageFragment createScreenSlidePageFragment(DPObject paramDPObject, int paramInt, Bitmap paramBitmap, Class paramClass, String paramString)
  {
    Object localObject = null;
    if (paramDPObject == null)
      return null;
    if (paramBitmap != null);
    try
    {
      paramClass = (ScreenSlidePageFragment)paramClass.getConstructor(new Class[0]).newInstance(new Object[0]);
      localObject = paramClass;
      Bundle localBundle = new Bundle();
      localObject = paramClass;
      localBundle.putParcelable("page", paramDPObject);
      localObject = paramClass;
      localBundle.putBoolean("current", true);
      localObject = paramClass;
      localBundle.putParcelable("bitmap", paramBitmap);
      localObject = paramClass;
      localBundle.putInt("position", paramInt);
      localObject = paramClass;
      paramClass.setArguments(localBundle);
      for (paramDPObject = paramClass; ; paramDPObject = paramBitmap)
      {
        localObject = paramDPObject;
        paramDPObject.categoryTag = paramString;
        break;
        paramBitmap = (ScreenSlidePageFragment)paramClass.getConstructor(new Class[0]).newInstance(new Object[0]);
        localObject = paramBitmap;
        paramClass = new Bundle();
        localObject = paramBitmap;
        paramClass.putParcelable("page", paramDPObject);
        localObject = paramBitmap;
        paramClass.putInt("position", paramInt);
        localObject = paramBitmap;
        paramBitmap.setArguments(paramClass);
      }
    }
    catch (Exception paramDPObject)
    {
      Log.w(paramDPObject.toString());
      paramDPObject = (DPObject)localObject;
    }
    return (ScreenSlidePageFragment)paramDPObject;
  }

  public static ScreenSlidePageFragment createScreenSlidePageFragment(DPObject paramDPObject, Bitmap paramBitmap, Class paramClass, String paramString)
  {
    paramString = null;
    if (paramDPObject == null)
      return null;
    if (paramBitmap != null);
    try
    {
      paramClass = (ScreenSlidePageFragment)paramClass.getConstructor(new Class[0]).newInstance(new Object[0]);
      paramString = paramClass;
      Bundle localBundle = new Bundle();
      paramString = paramClass;
      localBundle.putParcelable("page", paramDPObject);
      paramString = paramClass;
      localBundle.putBoolean("current", true);
      paramString = paramClass;
      localBundle.putParcelable("bitmap", paramBitmap);
      paramString = paramClass;
      paramClass.setArguments(localBundle);
      paramString = paramClass;
      break label143;
      paramBitmap = (ScreenSlidePageFragment)paramClass.getConstructor(new Class[0]).newInstance(new Object[0]);
      paramString = paramBitmap;
      paramClass = new Bundle();
      paramString = paramBitmap;
      paramClass.putParcelable("page", paramDPObject);
      paramString = paramBitmap;
      paramBitmap.setArguments(paramClass);
      paramString = paramBitmap;
    }
    catch (Exception paramDPObject)
    {
      Log.w(paramDPObject.toString());
    }
    label143: return paramString;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.basic.ScreenSlidePageFragmentFactory
 * JD-Core Version:    0.6.0
 */